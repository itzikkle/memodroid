/*
 * Copyright (C) 2011 Marco Cattarin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.erkatta.memodroid.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Represents the Database used to store all the High Scores
 * 
 */
public class MemodroidDatabaseHelper extends SQLiteOpenHelper {

/* CONSTANTS */
    /* DATABASE */
	private static final String DATABASE_NAME = "memodroidDB";
	private static final int DATABASE_VERSION = 3;
	private static final String CREATE_DB_QUERY = "CREATE TABLE highScores (id INTEGER PRIMARY KEY AUTOINCREMENT,moves INTEGER,elapsedGameTime INTEGER, difficultyLevel INTEGER, tilesTheme STRING, gameType INTEGER, userName STRING);";

	public MemodroidDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/* OVERRIDE METHODS (SQLiteOpenHelper) */	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DB_QUERY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE highScores;");
			db.execSQL(CREATE_DB_QUERY);
	}

}
