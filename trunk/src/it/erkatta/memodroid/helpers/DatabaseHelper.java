package it.erkatta.memodroid.helpers;

import java.util.ArrayList;

import it.erkatta.memodroid.subjects.HighScoreRecord;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "memodroidDB";
	private SQLiteDatabase db;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
		this.db = this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE highScores (id INTEGER PRIMARY KEY AUTOINCREMENT,position INTEGER, moves INTEGER,elapsedGameTime INTEGER);");
		//ContentValues cv = new ContentValues();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//android.util.Log.w("DatabaseHelper | onUpgrade","Upgrading database, which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS highScores");
		onCreate(db);
	}
	
	public long insertHighScoreRecord(HighScoreRecord record){
		ContentValues cv =new ContentValues();
		cv.put("position", record.position);
		cv.put("moves", record.moves);
		cv.put("elapsedGameTime", record.elapsedGameTime);
		
		return this.db.insert("highScores", null, cv);
	}
	
	public ArrayList<HighScoreRecord> getAvailableRecords(){
		ArrayList<HighScoreRecord> records = new ArrayList<HighScoreRecord>();
		String[] columns = {"position","moves","elapsedGameTime"};
		Cursor result=db.query("highScores",columns,null,null,null,null,"position ASC");
		
		result.moveToFirst();
		while (!result.isAfterLast()) {
			records.add(new HighScoreRecord(result.getInt(0),result.getInt(1),result.getInt(2)));
			result.moveToNext();
		}
		result.close();
		
		return records;
	}
	
	public void emptyHighScoresTable(){
		this.db.execSQL("DELETE FROM highScores");
	}
}
