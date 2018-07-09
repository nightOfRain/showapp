/*
 * Copyright (C) 2014 Nantian Electronics Information. All Rights Reserved.
 *
 * 本程序中所包含的信息属于机密信息，如无南天的书面许可，任何人都无权复制或利用。
 */
package com.nantian.showapp_Android.updates.framework;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nantian.showapp_Android.updates.framework.util.NTContextUtils;

/**
 * $Id$
 * 
 * 
 * 
 * @author Genty
 * 
 */
public class NTSQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "nt_data_center.db";

	private static final int DATABASE_VERSION = 1;

	public NTSQLiteHelper() {
		super(NTContextUtils.getContext().getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
