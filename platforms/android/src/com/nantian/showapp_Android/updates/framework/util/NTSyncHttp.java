package com.nantian.showapp_Android.updates.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Context;

import com.nantian.config.ConfigurationConstant;

public class NTSyncHttp {
		
	private static HttpClient initHttpClient(HttpParams params) {
		InputStream input = null;
		InputStream ins = null;
		try{
			input = mActivity.getAssets().open("client.p12");
			ins = mActivity.getAssets().open("server.bks");
			if(input == null || ins == null){
				// 如果证书不存在，就返回http客户端或者无验证的https客户端
				return initHttpClientByNull(params);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			// 如果获取证书不异常，就返回http客户端或者无验证的https客户端
			return initHttpClientByNull(params);
		}

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
		HttpProtocolParams.setUseExpectContinue(params, true);
		
		try {
			KeyStore trustStore = KeyStore.getInstance("PKCS12");
			KeyStore serverStore = KeyStore.getInstance("bks");

			trustStore.load(input, ConfigurationConstant.SSLPWd.toCharArray());
			serverStore.load(ins, ConfigurationConstant.SSLPWd.toCharArray());

			SSLSocketFactory factory = new SSLSocketFactory(trustStore, ConfigurationConstant.SSLPWd, serverStore);
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", factory, 843));
			
			ClientConnectionManager connMgr = new ThreadSafeClientConnManager(params, schReg);
			
			return new DefaultHttpClient(connMgr, params);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				input = null;
			}
		}
	}
	
	/**
	 * 获取http客户端或者无验证的https客户端
	 * @param params
	 * @return
	 */
    private static HttpClient initHttpClientByNull(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
	
	private static final String TAG = "SyncHttp";
    private static HttpClient httpClient = null;
    
    private static Context mActivity;
	
	public static HttpResponse httpPost(Context activity, String url, List<NameValuePair> params) throws Exception{
		try{
			mActivity = activity;
			URL uri = new URL(url);
			if(uri.getProtocol().equals("https")){
				if(httpClient == null){
					httpClient = initHttpClient(new BasicHttpParams());
				}
			}else{
				if(httpClient == null){
					httpClient = new DefaultHttpClient();
				}
			}
			HttpPost httpRequest = new HttpPost(url);
			
			HttpEntity httpentity = new UrlEncodedFormEntity(params, "utf-8");
			httpRequest.setEntity(httpentity);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 20000);
			
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			return httpResponse;
		} catch (ConnectTimeoutException e) {
			android.util.Log.e(TAG, "httpPost-ConnectTimeout");
			throw new ConnectTimeoutException();
		} catch (SocketTimeoutException e) {
			android.util.Log.e(TAG, "httpPost-ConnectTimeout");
			throw new ConnectTimeoutException();
		} catch (Exception e) {
			android.util.Log.e(TAG, "httpPost-Exception");
			throw new Exception(e);
		} finally {
		}
	}
	
	public static HttpResponse httpGet(Context activity, String url) throws Exception{
		try{
			
			mActivity = activity;
			URL uri = new URL(url);
			if(uri.getProtocol().equals("https")){
				if(httpClient == null){
					httpClient = initHttpClient(new BasicHttpParams());
				}
			}else{
				if(httpClient == null){
					httpClient = new DefaultHttpClient();
				}
			}
			
			HttpGet httpRequest = new HttpGet(url);
			
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), 10000);
			/* 请求超时 */
			HttpConnectionParams.setSoTimeout(httpClient.getParams(), 20000);
			
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			
			return httpResponse;
		} catch (ConnectTimeoutException e) {
			android.util.Log.e(TAG, "httpPost-ConnectTimeout");
			throw new ConnectTimeoutException();
		} catch (SocketTimeoutException e) {
			android.util.Log.e(TAG, "httpPost-ConnectTimeout");
			throw new ConnectTimeoutException();
		} catch (Exception e) {
			android.util.Log.e(TAG, "httpPost-Exception");
			throw new Exception(e);
		} finally {
		}
	}

}
