package it.erkatta.memodroid.subjects;

public class HighScoreRecord implements Comparable<HighScoreRecord>{
	public int position;
	public int moves;
	public int elapsedGameTime;
	
	public HighScoreRecord(int position, int moves, int elapsedGameTime){
		this.position = position;
		this.moves = moves;
		this.elapsedGameTime = elapsedGameTime;
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
