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

import it.erkatta.memodroid.subjects.HighScoreRecord;
import it.erkatta.memodroid.ui.UserNameRequestDialog;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HighScoreManager{

	private MemodroidDatabaseHelper dataBaseHelper;
	private SQLiteDatabase dataBase;
	
	public HighScoreManager(Context context) {
		dataBaseHelper = new MemodroidDatabaseHelper(context);
		if(getAllAvailableRecords().size()==0){
			//DataBase need to be populated with default values
			resetHighScores();
		}
	}
	
	private void openDatabase(){
		dataBase=dataBaseHelper.getWritableDatabase();
	}
	
	private void closeDatabase(){
		dataBase.close();
	}

	/**
	 * Remove all the Highscores from the database
	 */
	private void emptyHighScoresTable(){
		dataBase.execSQL("DELETE FROM highScores");
	}	
	
	/**
	 * Method to store a Highscore into the database
	 * 	
	 * @param record The Highscore to insert into the database
	 * @return the row ID of the newly inserted record, or -1 if an error occurred
	 */
	private long insertHighScoreRecord(HighScoreRecord record){
		ContentValues cv =new ContentValues();
		cv.put("moves", record.moves);
		cv.put("elapsedGameTime", record.elapsedGameTime);
		cv.put("difficultyLevel", record.difficultyLevel);
		cv.put("tilesTheme", record.tilesTheme);
		cv.put("gameType", record.gameType);
		cv.put("userName", record.userName);
		return dataBase.insert("highScores", null, cv);
	}

	private void deleteHighScoreRecord(int id){
		dataBase.execSQL("DELETE FROM highScores WHERE id="+id);
	}
	
	/**
	 * Retrieve all the High Scores present in the DataBase
	 * 
	 * @return all the HighScores present in the DataBase
	 */
	public ArrayList<HighScoreRecord> getAllAvailableRecords(){
		openDatabase();
		ArrayList<HighScoreRecord> records = new ArrayList<HighScoreRecord>();
		String[] columns = {"id","moves","elapsedGameTime","difficultyLevel","tilesTheme","gameType","userName"};
		Cursor result=dataBase.query("highScores",columns,null,null,null,null,"moves ASC, elapsedGameTime ASC");
		result.moveToFirst();
		while (!result.isAfterLast()) {
			HighScoreRecord record = new HighScoreRecord(result.getInt(1),result.getInt(2),result.getInt(3),result.getString(4),result.getInt(5),result.getString(6));
			record.id=result.getInt(0);
			records.add(record);
			result.moveToNext();
		}
		result.close();
		closeDatabase();
		return records;
	}
	
	/**
	 * Retrieve all the High Scores present in the DataBase that have the given difficulty level
	 * 
	 * @param difficultyLevel the difficulty on which filter the records
	 * @return all the HighScores present in the DataBase filter by their difficulty level
	 */
	public ArrayList<HighScoreRecord> getRecords(int difficultyLevel){
		openDatabase();
		ArrayList<HighScoreRecord> records = new ArrayList<HighScoreRecord>();
		String[] columns = {"id","moves","elapsedGameTime","difficultyLevel","tilesTheme","gameType","userName"};
		String[] selectionArgs = {new Integer(difficultyLevel).toString()};
		Cursor result=dataBase.query("highScores",columns,"difficultyLevel = ?",selectionArgs,null,null,"moves ASC, elapsedGameTime ASC");
		result.moveToFirst();
		while (!result.isAfterLast()) {
			HighScoreRecord record = new HighScoreRecord(result.getInt(1),result.getInt(2),result.getInt(3),result.getString(4),result.getInt(5),result.getString(6));
			record.id=result.getInt(0);
			records.add(record);
			result.moveToNext();
		}
		result.close();
		closeDatabase();
		return records;
	}	

	/**
	 * Cleans up all the record on the DataBase back to their default
	 */
	public void resetHighScores(){
		openDatabase();
		emptyHighScoresTable();
		insertDummyHighScores();
		closeDatabase();
	}
	
	/**
	 * Inserts the score, if it is an High Score into the DataBase 
	 * 
	 * @param score the score to insert into the DataBase
	 * @return true if the given score is an HighScore, false otherwise
	 */
	public boolean insertHighScore(HighScoreRecord score){
		ArrayList<HighScoreRecord> records = getRecords(score.difficultyLevel);
		openDatabase();
		if(records.size()==10){
			//Record set is full... remove the last one
			HighScoreRecord lastRecord = records.get(9);
			if(score.compareTo(lastRecord) == -1){
				deleteHighScoreRecord(lastRecord.id);
				insertHighScoreRecord(score);
				closeDatabase();
				return true;
			}
			else {
				return false;
			}
		}
		
		insertHighScoreRecord(score);
		closeDatabase();
		return true;
	}
	
	/**
	 * Checks if a score is a High Score
	 * 
	 * @param score the score to check
	 * @return true if the given score is an HighScore, false otherwise
	 */
	public boolean isHighScore(HighScoreRecord score){
		ArrayList<HighScoreRecord> records = getRecords(score.difficultyLevel);
		if(records.size()==10){
			HighScoreRecord lastRecord = records.get(9);
			if(score.compareTo(lastRecord) == -1){
				return true;
			}
			else {
				return false;
			}
		}
		return true;
	}	
	
	
	/* HIGH SCORES DATABASE DEFAULT VALUES */
	
	private void insertDummyHighScores(){
		/* KID MODE DEFAULT*/
		insertHighScoreRecord(new HighScoreRecord(14,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(15,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(16,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(17,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(18,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(19,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(20,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(21,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(22,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(23,180,0,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		/* EASY MODE DEFAULT*/
		insertHighScoreRecord(new HighScoreRecord(20,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(21,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(22,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(23,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(24,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(25,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(26,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(27,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(28,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(29,180,1,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		/* NORMAL MODE DEFAULT*/
		insertHighScoreRecord(new HighScoreRecord(30,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(31,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(32,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(33,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(34,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(35,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(36,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(37,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(38,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(39,180,2,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		/* HARD MODE DEFAULT*/
		insertHighScoreRecord(new HighScoreRecord(50,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(51,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(52,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(53,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(54,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(55,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(56,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(57,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(58,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
		insertHighScoreRecord(new HighScoreRecord(59,180,3,"Classic",0,UserNameRequestDialog.DEFAULT_USERNAME));
	}
}
