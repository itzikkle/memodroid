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

package it.erkatta.memodroid.subjects;

/**
 * Represents a High Score
 * 
 * @author Marco Cattarin
 *
 */
public class HighScoreRecord implements Comparable<HighScoreRecord>{
	public int id;
	public int moves;
	public int elapsedGameTime;
	public int difficultyLevel;
	public String tilesTheme;
	public int gameType;
	public String userName;

	public HighScoreRecord(int moves, int elapsedGameTime, int difficultyLevel, String tilesTheme, int gameType, String userName){
		this.moves = moves;
		this.elapsedGameTime = elapsedGameTime;
		this.difficultyLevel = difficultyLevel;
		this.tilesTheme = tilesTheme;
		this.gameType = gameType;
		this.userName = userName;
	}	
	
	public HighScoreRecord(int moves, int elapsedGameTime, int difficultyLevel, String tilesTheme, int gameType){
		this(moves,elapsedGameTime,difficultyLevel,tilesTheme,gameType,"");
	}

	/**
	 * Returns the score game time formatted into a string
	 * 
	 * @return the string that represent the score game time
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

	@Override
	public int compareTo(HighScoreRecord another) {
		if(this.moves < another.moves){
			return -1;
		}
		else if(this.moves > another.moves) {
			return 1;
		}
		else{
			if(this.elapsedGameTime < another.elapsedGameTime){
				return -1;
			}
			else if(this.elapsedGameTime > another.elapsedGameTime) {
				return 1;
			}
			else{
				return 0;
			}
		}
	}
	
}
