package com.nantian.showapp_Android.updates.framework.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.nantian.showapp_Android.updates.framework.NTConstants.Spec;

/**
 * $Id: NTConfigUtils.java 4129 2014-10-08 15:54:43Z Genty $
 * 
 * 配置工具
 * 
 * @author Genty
 * 
 */
public class NTConfigUtils {

	private Document document = null;

	private Element root = null;

	private static NTConfigUtils instance = new NTConfigUtils();

	private static final String TAG = "ConfigUtils";

	private NTConfigUtils() {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream in = NTContextUtils.getContext().getApplicationContext().getAssets().open("nt_config.xml");
			document = builder.parse(in);
			root = (Element) document.getElementsByTagName("configs").item(0);
		} catch (Exception e) {
			NTLogger.error(TAG, "Failed to load config", e);
		}
	}

	private String retreive(String category, String id, String tag) {
		try {
			NodeList nodes = document.getElementsByTagName(category);
			if (nodes.getLength() > 0) {
				nodes = nodes.item(0).getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (Node.ELEMENT_NODE != node.getNodeType()) {
						continue;
					}
					Element e = (Element) node;
					if (id.equals(e.getAttribute("id"))) {
						NodeList list = e.getElementsByTagName(tag);

						if (list.getLength() > 0) {
							return list.item(0).getChildNodes().item(0).getNodeValue();
						}
					}
				}
			}
		} catch (Throwable e) {
			NTLogger.error(TAG, String.format("Failed to read config[%s->%s->%s]", category, id, tag), e);
		}
		return null;
	}
	private String retreive(String category, String id, String tag,String child) {
		try {
			NodeList nodes = document.getElementsByTagName(category);
			if (nodes.getLength() > 0) {
				nodes = nodes.item(0).getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					if (Node.ELEMENT_NODE != node.getNodeType()) {
						continue;
					}
					Element e = (Element) node;
					if (id.equals(e.getAttribute("id"))) {
						NodeList list = e.getElementsByTagName(tag);
						
						if (list.getLength() > 0) {
							//list=
							return list.item(0).getChildNodes().item(0).getNodeValue();
						}
					}
				}
			}
		} catch (Throwable e) {
			NTLogger.error(TAG, String.format("Failed to read config[%s->%s->%s]", category, id, tag), e);
		}
		return null;
	}

	private String retreive(String category, String tag) {
		try {
			NodeList nodes = root.getElementsByTagName(category);
			if (nodes.getLength() > 0) {
				nodes = nodes.item(0).getChildNodes();
				for (int i = 0; i < nodes.getLength(); i++) {
					Node node = nodes.item(i);
					NTLogger.debug(TAG, node.getNodeName() + "[" + node.getTextContent() + "]");
					if (node.getNodeName().equals(tag)) {
						return node.getTextContent();
					}
				}
			}
		} catch (Throwable e) {
			NTLogger.error(TAG, String.format("Failed to read config[%s->%s]", category, tag), e);
		}
		return null;
	}

	/**
	 * 获取配置项
	 * 
	 * @param category
	 * @param tag
	 * @return
	 */
	public static String getConfig(String category, String tag) {
		return instance.retreive(category, tag);
	}

	/**
	 * 获取配置项
	 * 
	 * @param path category/id/tag 如servers/product/domain
	 * @return
	 */
	public static String getConfig(String category, String id, String tag) {
		return instance.retreive(category, id, tag);
	}

	/**
	 * 获取编码过的配置项
	 * 
	 * @param category
	 * @param id
	 * @param tag
	 * @return
	 */
	public static String getEncodedConfig(String category, String id, String tag) {
		String text = instance.retreive(category, id, tag);
		if (text != null) {
			text = decode(text);
		}
		return text;
	}

	/**
	 * 获取编码过的配置项
	 * 
	 * @param category
	 * @param tag
	 * @return
	 */
	public static String getEncodedConfig(String category, String tag) {
		String text = instance.retreive(category, tag);
		if (text != null) {
			text = decode(text);
		}
		return text;
	}

	private static String decode(String str) {
		byte[] bytes = NTBase64.decode(str, NTBase64.DEFAULT);
		try {
			return new String(bytes, Spec.CHARSET);
		} catch (UnsupportedEncodingException e) {
			NTLogger.error(TAG, e.getMessage());
		}
		return null;
	}

}
