package it.erkatta.memodroid.core;

import it.erkatta.memodroid.helpers.ResourceHelper;
import android.util.Log;

public class GameEngine {

	public int[] tileCouple = { -1, -1 };
	private Integer leftTiles;
	private ResourceHelper resourceHelper;
	public int totalMoves = 0;
	public int elapsedGameTime = 0;

	public GameEngine(ResourceHelper resourceHelper) {
		//Log.d("GameEngine", "CREATION");
		this.resourceHelper = resourceHelper;
		leftTiles = resourceHelper.getTotalTileCount();
	}

	public int putMemoryTileInTileCouple(int tilePosition) {
		//Log.i("GameEngine", "CONTENT OF TILE ARRAY: ["+this.tileCouple[0]+" , "+this.tileCouple[1]+"]");
		if (this.tileCouple[0] == -1) {
			//Log.d("GameEngine", "PUT IN POSITION 0");
			this.tileCouple[0] = tilePosition;
			return 1;
		}else if (this.tileCouple[1] == -1) {
			//Log.d("GameEngine", "PUT IN POSITION 1 (VALUE OF [0] = "+ this.tileCouple[0] + ")");
			this.tileCouple[1] = tilePosition;
			return 2;
		} else{
			return -1;
		}
	}

	public void clearMoveArray() {
		//Log.d("GameEngine", "CLEAR MOVE ARRAY");
		tileCouple[0] = -1;
		tileCouple[1] = -1;
	}

	public Integer tileCoupleRemoved() {
		this.leftTiles = this.leftTiles - 2;
		return this.leftTiles;
	}
	
	public String getElapsedGameTime(){
		int int_mins = (elapsedGameTime/60);
		int int_secs = (elapsedGameTime%60);
		String mins = "";
		String secs = "";
		if(int_mins<10){
			mins="0"+int_mins;
		}
		else{
			mins = ""+int_mins;
		}
		
		if(int_secs<10){
			secs="0"+int_secs;
		}
		else{
			secs = ""+int_secs;
		}
		return mins+":"+secs;
	}
	
	public void resetGameEngine(){
		tileCouple[0] = -1;
		tileCouple[1] = -1;
		leftTiles = resourceHelper.getTotalTileCount();
		totalMoves = 0;
		elapsedGameTime = 0;
	}
}
