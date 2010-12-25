package it.erkatta.memodroid.persistence;

import it.erkatta.memodroid.helpers.DatabaseHelper;
import it.erkatta.memodroid.subjects.HighScoreRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import android.content.Context;

public class HighScoreManager {
	
	private DatabaseHelper dbHelper;
	
	public HighScoreManager(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public ArrayList<HighScoreRecord> getRecords(){
		return dbHelper.getAvailableRecords();
	}
	
	public void insertHighScore(HighScoreRecord record){
		ArrayList<HighScoreRecord> records = dbHelper.getAvailableRecords();
		if(records.size()==10){
			//Record set is full... remove the last one
			records.remove(9);
		}
		records.add(record);
		Collections.sort(records);
		this.updatePositions(records);
		
		dbHelper.emptyHighScoresTable();
		
		for(HighScoreRecord item : records){
			dbHelper.insertHighScoreRecord(item);
		}
	}
	
	public boolean isHighScore(HighScoreRecord score){
		ArrayList<HighScoreRecord> records = dbHelper.getAvailableRecords();
		if(records.size()==10){
			//Record set is full... remove the last one
			if(score.compareTo(records.get(9)) == -1){
				return true;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	private void updatePositions(ArrayList<HighScoreRecord> records){
		int position = 1;
		for(HighScoreRecord item : records){
			item.position = position;
			position++;
		}
	}
	
}
