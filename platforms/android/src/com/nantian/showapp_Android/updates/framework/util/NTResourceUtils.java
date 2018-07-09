package com.nantian.showapp_Android.updates.framework.util;

import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

/**
 * $Id: NTResourceUtils.java 4246 2014-10-17 16:52:27Z Genty $
 * 
 * 资源处理辅助类，程序通过该类获取资源ID，避免在程序中对R的应用
 * 
 * @author Genty
 * 
 */
public class NTResourceUtils {

	/** 内部类名 layout */
	private static final String CLASS_NAME_LAYOUT = "layout";

	/** 内部类名 string */
	private static final String CLASS_NAME_STRING = "string";

	/** 内部类名 drawable */
	private static final String CLASS_NAME_DRAWABLE = "drawable";

	/** 内部类名 id */
	private static final String CLASS_NAME_ID = "id";

	/** 内部类名 style */
	private static final String CLASS_NAME_STYLE = "style";

	/** 内部类名 dimen */
	private static final String CLASS_NAME_DIMEN = "dimen";

	/** 内部类名 anim */
	private static final String CLASS_NAME_ANIM = "anim";

	private static NTResourceUtils instance = null;

	private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

	private String packageName = null;

	private NTResourceUtils() {
		packageName = NTContextUtils.getContext().getApplicationContext().getPackageName();
	}

	private static synchronized NTResourceUtils getInstance() {
		if (instance == null) {
			instance = new NTResourceUtils();
		}
		return instance;
	}

	/**
	 * 根据drawable名称获取资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getDrawableId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_DRAWABLE, name);
	}

	/**
	 * 根据资源名称获取资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_ID, name);
	}

	/**
	 * 根据样式名称获取样式的资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getStyleId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_STYLE, name);
	}

	/**
	 * 根据布局名称获取布局的资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getLayoutId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_LAYOUT, name);
	}

	/**
	 * 根据类名、资源名获取资源ID
	 * 
	 * @param className
	 * @param name
	 * @return
	 */
	private int getResourseIdByName(String className, String name) {
		if (name == null || name.length() == 0) {
			throw new IllegalArgumentException("Parameter name is empty.");
		}
		int id = 0;
		try {
			Class<?> clazz = classes.get(className);
			if (clazz == null) {
				clazz = Class.forName(packageName + ".R$" + className);
				if (clazz != null) {
					classes.put(className, clazz);
				}
			}
			if (clazz != null) {
				id = clazz.getField(name).getInt(clazz);
			}
		} catch (Exception e) {
			// 只有在插件整合不正确的情况才会发生，所以采用RuntimeException
			throw new RuntimeException("Failed to get resource id", e);
		}
		return id;
	}

	/**
	 * 根据字符串名称获取字符串的资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getStringId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_STRING, name);
	}

	/**
	 * 根据dimen名称获取dimen的资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getDimensionId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_DIMEN, name);
	}

	/**
	 * 根据anim名称获取anim的资源ID
	 * 
	 * @param name
	 * @return
	 */
	public static int getAnimId(String name) {
		return getInstance().getResourseIdByName(CLASS_NAME_ANIM, name);
	}

	public static Drawable getDrawable(String name) {
		return NTContextUtils.getContext().getApplicationContext().getResources().getDrawable(getDrawableId(name));
	}

	/**
	 * 获取Dimension
	 * 
	 * @param name
	 * @return
	 */
	public static float getDimension(String name) {
		return NTContextUtils.getContext().getApplicationContext().getResources().getDimension(getDimensionId(name));
	}

}
