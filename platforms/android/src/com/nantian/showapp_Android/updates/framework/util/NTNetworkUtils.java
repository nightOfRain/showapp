package com.nantian.showapp_Android.updates.framework.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nantian.showapp_Android.updates.framework.NTConstants.Spec;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceException;
import com.nantian.showapp_Android.updates.framework.exception.NTServiceSecurityException;
import com.nantian.showapp_Android.updates.framework.util.NTNetworkUtils.HttpRequest.Method;
import com.nantian.showapp_Android.updates.framework.util.NTNetworkUtils.HttpRequest.Parameter;

/**
 * $Id: NTNetworkUtils.java 5013 2014-12-23 07:29:01Z Genty $
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTNetworkUtils {

	private static final String PROTOCOL_HTTPS = "https";

	private static final String PROTOCOL_HTTP = "http";

	private static final String TAG = "NetworkUtils";

	private static final int MAX_BUFFER_SIZE = 1024;

	private static final int MAX_CONNECT_TIMEOUT = 90 * 1000;

	private static final int MAX_READ_TIMEOUT = 90 * 1000;

	private static final String HTTP_POST = "POST";
	
	private static final String HTTP_GET = "GET";

	private static final String CHARSET_PREFIX = "charset=";

	private static volatile HttpURLConnection conn = null;

	private static volatile InputStream is = null;

	private static volatile OutputStream os = null;

	private NTNetworkUtils() {
	}

	private static final TrustManager[] TRUSTMANAGER = new TrustManager[1];

	static {
		TRUSTMANAGER[0] = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate ax509certificate[], String s) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
	}

	/**
	 * 检查当前设备网络是否可用
	 * 
	 * @param 调用此方法的Context
	 * @return true - 有可用的网络连接（3G/GSM、wifi等） false - 没有可用的网络连接，或传入的context为null
	 */
	public static boolean isOnline(Context context) {

		if (context == null) {
			return false;
		}
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前网络类型，wifi或其它
	 * 
	 * @param context
	 * @return 0,1,异常-1
	 */
	public static int getNetworkType(Context context) {
		if (context == null) {
			return -1;
		}
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manager.getActiveNetworkInfo();
		if (netInfo != null) {
			return netInfo.getType();
		}
		return -1;
	}

	/**
	 * 非法请求异常, 消息为服务器返回的异常信息
	 */
	public static class IllegalRequestException extends Exception {

		/**  */
		private static final long serialVersionUID = 4304706895375418310L;

		/** 错误代码 */
		private String code;

		public IllegalRequestException(String detailMessage) {
			super(detailMessage);
		}

		public IllegalRequestException(String code, String detailMessage) {
			super(detailMessage);
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

	}

	/**
	 * Http 请求封装类
	 *
	 */
	public static class HttpRequest {

		public static enum Method {
			GET,

			POST
		}

		/**
		 * Http 请求参数
		 */
		public static class Parameter {

			/** 参数名称 */
			private final String name;

			/** 参数值 */
			private final Object value;

			/** 编码字符集 */
			private final String charset;

			public Parameter(String name, Object value) {
				this(name, value, null);
			}

			public Parameter(String name, Object value, String charset) {
				super();
				if (name == null || name.trim().length() == 0 || value == null) {
					throw new IllegalArgumentException("Parameter name or value can't be null");
				}
				this.name = name.trim();
				this.value = value;
				this.charset = charset;
			}

			private void append(StringBuilder sb, Object value) {
				sb.append(name).append("=");
				Object v = value;
				if (charset == null) {
					sb.append(v.toString());
				} else {
					try {
						sb.append(URLEncoder.encode(v.toString(), charset));
					} catch (UnsupportedEncodingException e) {
						NTLogger.warn(TAG, String.format("Parameter[%s] can't be encoded, use value[%s]", name, v.toString()), e);
						sb.append(v.toString());
					}
				}
			}

			String get() {
				StringBuilder sb = new StringBuilder();
				// 参数是数组
				if (value instanceof Object[]) {
					Object[] values = (Object[]) value;
					for (Object v : values) {
						if (v == null) {
							continue;
						}
						if (sb.length() > 0) {
							sb.append("&");
						}
						append(sb, v);
					}
				} else {
					append(sb, value);
				}
				return sb.toString();
			}

		}

		/** 请求头 */
		private final Map<String, String> headers = new HashMap<String, String>();

		/** 请求参数 */
		private final Map<String, Parameter> parameters = new LinkedHashMap<String, Parameter>();

		/** 请求url */
		private String url = null;

		/** 结果字符集 */
		private String charset;

		/** 请求方法 GET, POST */
		private Method method;

		/** POST字节 */
		private byte[] data;

		public HttpRequest() {
		}

		public HttpRequest(String url) {
			this.url = url;
		}

		public HttpRequest(String url, Map<String, String> headers) {
			this.url = url;
			if (headers != null && !headers.isEmpty()) {
				this.headers.putAll(headers);
			}
		}

		public HttpRequest(String url, Map<String, String> headers, Map<String, Parameter> parameters) {
			this.url = url;
			if (headers != null && !headers.isEmpty()) {
				this.headers.putAll(headers);
			}
			if (parameters != null && !parameters.isEmpty()) {
				this.parameters.putAll(parameters);
			}
		}

		/**
		 * 设置请求头
		 * 
		 * @param key
		 * @param value
		 */
		public void addHeader(String key, String value) {
			if (key == null || value == null) {
				return;
			}
			headers.put(key, value);
		}

		/**
		 * 设置请求头
		 * 
		 * @param headers
		 */
		public void addHeaders(Map<String, String> headers) {
			if (headers != null && !headers.isEmpty()) {
				this.headers.putAll(headers);
			}
		}

		public void addParameter(String name, Object value) {
			addParameter(name, value, null);
		}

		public void addParameter(String name, Object value, String charset) {
			if (name == null || value == null) {
				return;
			}
			parameters.put(name, new Parameter(name, value, charset));
		}

		public String getCharset() {
			return charset;
		}

		/**
		 * @return the headers
		 */
		public Map<String, String> getHeaders() {
			return headers;
		}

		public Method getMethod() {
			if (method == null) {
				method = Method.GET;
			}
			return method;
		}

		/**
		 * <pre>
		 * 获取request的post数据
		 * 注：这里的parameters将以UTF-8的编码进行处理，如果不是UTF-8的参数要自行转成字节数组存放到data中
		 * 
		 * @return 如果method为GET，则返回null，否则返回参数的字节数组+data
		 */
		public byte[] getData() {
			if (getMethod() == Method.GET) {
				return null;
			}
			if (getParameters().length() > 0 && data != null && data.length > 0) {
				byte[] dataParameter = null;
				try {
					dataParameter = (getParameters() + "&").getBytes(Spec.CHARSET);
				} catch (UnsupportedEncodingException e) {
					// TODO 这里不可能发生，若发生则这里的逻辑有问题
					NTLogger.warn(TAG, "Failed to getData()", e);
					return data;
				}
				byte[] datacopy = new byte[dataParameter.length + data.length];

				System.arraycopy(dataParameter, 0, datacopy, 0, dataParameter.length);

				System.arraycopy(data, 0, datacopy, dataParameter.length, data.length);

				return datacopy;
			}
			return data;
		}

		/**
		 * 设置post数据，若使用该方法发送数据，则需避免同时使用addParameter方法添加参数
		 * 
		 * @param data
		 */
		public void setData(byte[] data) {
			this.data = data;
		}

		/**
		 * 获取请求的query串
		 * 
		 * @return
		 */
		public String getParameters() {
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Parameter> e : parameters.entrySet()) {
				String v = e.getValue().get();
				if (sb.length() > 0) {
					sb.append("&");
				}
				sb.append(v);
			}
			return sb.toString();
		}

		/**
		 * 获取请求的完整url
		 * 
		 * @return the url
		 */
		public String getUrl() {
			if (url != null && method == Method.GET) {
				String p = getParameters();
				if (p.length() > 0) {
					if (url.indexOf("?") > -1) {
						return url + "&" + p;
					} else {
						return url + "?" + p;
					}
				}
			}
			return url;
		}

		public void setCharset(String charset) {
			this.charset = charset;
		}

		public void setMethod(Method method) {
			this.method = method;
		}

		/**
		 * @param url the url to set
		 */
		public void setUrl(String url) {
			this.url = url;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("HttpRequest: {\n");
			sb.append("    url:   [").append(url).append("]\n");
			sb.append("    method:[").append(getMethod().name()).append("]\n");
			sb.append("    params:[").append(getParameters()).append("]\n");
			sb.append("}");
			return sb.toString();
		}

	}

	public static class HttpResponse {

		/** 响应码 */
		private int code = 0;

		/** 响应内容 */
		private String content = null;

		/** 响应头 */
		private Map<String, List<String>> headers = null;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Map<String, List<String>> getHeaders() {
			return headers;
		}

		public void setHeaders(Map<String, List<String>> headers) {
			this.headers = headers;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

	}

	/**
	 * POST 请求到http或https网站
	 * 
	 * @param url 目标地址
	 * @param headers 请求的头
	 * @param data 上传的数据
	 * @return 服务器响应内容
	 * @throws Exception
	 * @throws IllegalRequestException 在服务器响应码不为200时的错误信息
	 */
	public static String post(String url, Map<String, String> headers, List<Parameter> parameters) throws Exception, IllegalRequestException {
		return doPostHttpRequest(url, headers, parameters);
	}
	
	/**
	 * POST 请求到http或https网站
	 * 
	 * @param url 目标地址
	 * @param headers 请求的头
	 * @param data 上传的数据
	 * @return 服务器响应内容
	 * @throws Exception
	 * @throws IllegalRequestException 在服务器响应码不为200时的错误信息
	 */
	public static String post(String url, Map<String, String> headers, byte[] data) throws Exception, IllegalRequestException {
		return doHttpRequest(url, HTTP_POST, headers, data);
	}

	/**
	 * Get 请求到http或https网站
	 * 
	 * @param url 目标地址
	 * @param headers 请求的头
	 * @return 服务器响应内容
	 * @throws Exception
	 * @throws IllegalRequestException 在服务器响应码不为200时的错误信息
	 */
	public static String get(String url, Map<String, String> headers) throws Exception, IllegalRequestException {
		return doHttpRequest(url, HTTP_GET, headers, null);
	}

	/**
	 * 执行https请求
	 * 
	 * @param url
	 * @param method
	 * @param headers
	 * @param data
	 * @return
	 * @throws Exception
	 * @throws IllegalRequestException
	 */
	private static String doPostHttpRequest(String url, Map<String, String> headers, List<Parameter> parameters) throws Exception, IllegalRequestException {
		NTLogger.debug(TAG, String.format("Request url[%s]", url));
		HttpRequest request = new HttpRequest();

		request.setMethod(Method.POST);

		request.setUrl(url);

		request.addHeaders(headers);
		for(Parameter p : parameters){
			request.addParameter(p.name, p.value);
		}

		if (headers != null) {
			request.setCharset(headers.get("Accept-Charset"));
		}

		HttpResponse response = send(request);

		return response.getContent();
	}
	
	/**
	 * 执行https请求
	 * 
	 * @param url
	 * @param method
	 * @param headers
	 * @param data
	 * @return
	 * @throws Exception
	 * @throws IllegalRequestException
	 */
	private static String doHttpRequest(String url, String method, Map<String, String> headers, byte[] data) throws Exception, IllegalRequestException {
		NTLogger.debug(TAG, String.format("Request url[%s]", url));
		HttpRequest request = new HttpRequest();

		if (HTTP_POST.equals(method)) {
			request.setMethod(Method.POST);
		} else {
			request.setMethod(Method.GET);
		}

		request.setUrl(url);

		request.setData(data);

		request.addHeaders(headers);

		if (headers != null) {
			request.setCharset(headers.get("Accept-Charset"));
		}

		HttpResponse response = send(request);

		return response.getContent();
	}

	/**
	 * 断开请求连接
	 */
	public static void disconnect() {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {
			}
			os = null;
		}
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
			is = null;
		}
		if (conn != null) {
			conn.disconnect();
			conn = null;
		}
	}

	private static String extractCharset(HttpURLConnection conn) {
		String charset = "";
		String contentType = conn.getContentType();
		if (contentType == null) {
			return Spec.CHARSET;
		}
		String[] values = contentType.split(";");

		for (String value : values) {
			value = value.trim();
			if (value.toLowerCase(Locale.US).startsWith(CHARSET_PREFIX)) {
				charset = value.substring(CHARSET_PREFIX.length());
				break;
			}
		}
		if ("".equals(charset)) {
			charset = Spec.CHARSET;
		}
		return charset;
	}

	/**
	 * 使用UrlEncoder编码
	 * 
	 * @param content
	 * @return
	 */
	public static String encode(String content) {
		try {
			return URLEncoder.encode(content, Spec.CHARSET);
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return content;
	}

	/**
	 * 使用UrlDecoder解码
	 * 
	 * @param content
	 * @return
	 */
	public static String decode(String content) {
		try {
			return URLDecoder.decode(content, Spec.CHARSET);
		} catch (UnsupportedEncodingException e) {
			// ignore
		}
		return content;
	}

	/**
	 * 发送http请求
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse send(HttpRequest request) throws Exception {

		if (request == null || request.getUrl() == null || request.getUrl().length() == 0) {
			NTLogger.error(TAG, String.format("Failed to send(%s)", request));
			throw new IllegalRequestException("请求对象不能为空");
		}

		NTLogger.debug(TAG, request.toString());

		String url = request.getUrl();

		URL resource = null;
		try {
			resource = new URL(url);
			String protocol = resource.getProtocol();
			if (PROTOCOL_HTTP.equals(protocol)) {
				conn = (HttpURLConnection) resource.openConnection();
			} else if (PROTOCOL_HTTPS.equals(protocol)) {
				try {
					SSLContext sslcontext = SSLContext.getInstance("TLS");
					sslcontext.init(null, TRUSTMANAGER, null);
					HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
				} catch (KeyManagementException e) {
					NTLogger.warn(TAG, "Create SSLContext failed", e);
					throw new Exception(e.getCause());
				} catch (NoSuchAlgorithmException e) {
					NTLogger.warn(TAG, "Create SSLContext failed", e);
					throw new Exception(e.getCause());
				}

				conn = (HttpsURLConnection) resource.openConnection();
			} else {
				throw new Exception("Unsupported protocol [" + protocol + "]");
			}
			conn.setConnectTimeout(MAX_CONNECT_TIMEOUT);
			conn.setReadTimeout(MAX_READ_TIMEOUT);
			conn.setRequestMethod(request.getMethod().name());
			byte[] data = null;
			if (Method.POST == request.getMethod()) {
				data = request.getData();
				if(data ==null){
					data = request.getParameters().getBytes();
				}
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			}
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// conn.setChunkedStreamingMode(0);
			conn.setFixedLengthStreamingMode((data == null ? 0 : data.length));

			Map<String, String> headers = request.getHeaders();
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					conn.addRequestProperty(entry.getKey(), value);
				}
			}

			if (data != null && data.length > 0) {
				os = new BufferedOutputStream(conn.getOutputStream());
				os.write(data);
				os.flush();
			}

			HttpResponse response = new HttpResponse();

			byte[] buf = new byte[MAX_BUFFER_SIZE];
			int len = -1;

			int responseCode = conn.getResponseCode();
			response.setCode(responseCode);
			NTLogger.debug(TAG, String.format("Response code[%s]", responseCode));
			NTLogger.debug(TAG, "Response headers:\r\n");
			Map<String, List<String>> fields = conn.getHeaderFields();
			response.setHeaders(fields);
			if (fields != null) {
				for (Entry<String, List<String>> e : fields.entrySet()) {
					NTLogger.debug(TAG, String.format("    %s: %s", e.getKey(), e.getValue()));
				}
			}

			if (responseCode == HttpURLConnection.HTTP_OK) {
				is = new BufferedInputStream(conn.getInputStream());

			} else {
				if (responseCode == 701 || responseCode == 702) {
					throw new NTServiceSecurityException(String.valueOf(responseCode), "请重新登陆", null);
				}
				is = new BufferedInputStream(conn.getErrorStream());
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((len = is.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			NTLogger.debug(TAG, "Response length[" + baos.size() + "]");
			String content = null;
			try {
				String charset = request.getCharset();
				if (charset == null) {
					charset = extractCharset(conn);
				}
				content = new String(baos.toByteArray(), charset);
			} catch (Exception e) {
				content = new String(baos.toByteArray(), Spec.CHARSET);
			}

			NTLogger.debug(TAG, String.format("Response content[%s]", content));
			if (responseCode != HttpURLConnection.HTTP_OK) {
				throw new IllegalRequestException(String.valueOf(responseCode), content);
			}
			response.setContent(content);
			return response;
		} catch (SocketException se) {
			NTLogger.error(TAG, "Failed to send request", se);
			throw new NTServiceException(com.nantian.showapp_Android.updates.framework.NTConstants.Error.NETWORK_ERROR, se);
		} catch (IllegalRequestException e) {
			NTLogger.error(TAG, "Failed to send request", e);
			throw e;
		} catch (NTServiceException e) {
			NTLogger.error(TAG, "Failed to send request", e);
			throw e;
		} catch (Throwable e) {
			NTLogger.error(TAG, "Failed to send request", e);
			throw new NTServiceException(com.nantian.showapp_Android.updates.framework.NTConstants.Error.SYSTEM_ERROR, e);
		} finally {
			disconnect();
		}
	}

	/**
	 * 获取HttpURLConnection
	 */
	public static HttpURLConnection openConnection(String url) {
		URL resource = null;
		try {
			resource = new URL(url);
			String protocol = resource.getProtocol();
			if (PROTOCOL_HTTP.equals(protocol)) {
				conn = (HttpURLConnection) resource.openConnection();
			} else if (PROTOCOL_HTTPS.equals(protocol)) {
				try {
					SSLContext sslcontext = SSLContext.getInstance("TLS");
					sslcontext.init(null, TRUSTMANAGER, new SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
				} catch (KeyManagementException e) {
					NTLogger.warn(TAG, "Create SSLContext failed", e);
					throw new Exception(e.getCause());
				} catch (NoSuchAlgorithmException e) {
					NTLogger.warn(TAG, "Create SSLContext failed", e);
					throw new Exception(e.getCause());
				}
				conn = (HttpsURLConnection) resource.openConnection();
				((HttpsURLConnection)conn).setHostnameVerifier(DO_NOT_VERIFY);

			} else {
				throw new Exception("Unsupported protocol [" + protocol + "]");
			}
			conn.setConnectTimeout(MAX_CONNECT_TIMEOUT);
			conn.setReadTimeout(MAX_READ_TIMEOUT);
			conn.setDoInput(true);
			conn.setUseCaches(false);
		} catch (Exception e) {
		}
		return conn;
	}

	private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {

		@Override
		public boolean verify(String s, SSLSession sslsession) {
			return true;
		}
	};

}
