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

package it.erkatta.memodroid.services;

import it.erkatta.memodroid.subjects.MemoryTile;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Handler;
import android.widget.TextView;

public class GameEngine {
	
    /* GAMES VARIABLES */
	private Timer timeUpdateTimer;
    public int elapsedGameTime = 0;
    public int totalMoves = 0;
	private Integer leftTiles;
	public MemoryTile[] movesArray = { null, null };
	public MemoryTile selectedTile;
	Handler commonHandler = new Handler();

	/* LAYOUT VARIABLES */
	private TextView timeTextView;  
	
	public GameEngine(int totalTiles) {
		leftTiles = totalTiles;
	}
	
	public GameEngine(int totalTiles,TextView timeTextView){
		this(totalTiles);
		this.timeTextView = timeTextView;
	}

	/**
	 * Updates the moves array and return the ordinal of the move just done
	 * 
	 * @param tilePosition The Position of the tile in the tile array
	 * @return the ordinal of the move, -1 if you try to do more than 2 moves in a row
	 */
	public int doMove(MemoryTile tileSelected) {
		if (movesArray[0] == null) {
			movesArray[0] = tileSelected;
			return 1;
		}else if (movesArray[1] == null) {
			movesArray[1] = tileSelected;
			return 2;
		}else{
			return -1;
		}
	}	

	/**
	 * Reset the moves array
	 */
	public void clearMoveArray() {
		movesArray[0] = null;
		movesArray[1] = null;
	}

	/**
	 * Removes two tiles from the total amount of tiles left to win the game
	 * 
	 * @return the amount of tiles left to complete the game
	 */
	public Integer tileCoupleRemoved() {
		this.leftTiles = this.leftTiles - 2;
		return this.leftTiles;
	}	

	/**
	 * Starts the game time timer
	 */	
	public void startTimeUpdate(){
		if(timeUpdateTimer == null){
			timeUpdateTimer = new Timer();
		}
		timeUpdateTimer.schedule(new TimerTask() {
			public void run() {
			    commonHandler.post(new Runnable() {
					public void run() {
						elapsedGameTime++;
						timeTextView.setText(getElapsedGameTime());
					}
				});
			}
		}, 0,1000);
	}

	/**
	 * Stops the game time timer
	 */	
	public void stopTimeUpdate(){
		if(timeUpdateTimer != null){
			timeUpdateTimer.cancel();
			timeUpdateTimer = null;
		}
	}	

	/**
	 * Returns the game time formatted into a string
	 * 
	 * @return the string that represent the current game time
	 */
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
	
}
