package it.erkatta.memodroid;

import it.erkatta.memodroid.core.GameEngine;
import it.erkatta.memodroid.helpers.ResourceHelper;
import it.erkatta.memodroid.helpers.SoundHelper;
import it.erkatta.memodroid.persistence.HighScoreManager;
import it.erkatta.memodroid.subjects.HighScoreRecord;
import it.erkatta.memodroid.subjects.MemoryTile;
import it.erkatta.memodroid.ui.HighScorePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Memodroid Main Activity.
 * 
 * @author Marco Cattarin
 *
 */
public class Memodroid extends Activity implements OnClickListener {
	
	/* LAYOUT OBJECTS */
	public TextView timeTextView;
	public TextView pointTextView;
	public TableLayout tableLayout;
	
	/* DIALOGS */
	private Dialog dialog;
	
	/* SYSTEM SERVICES AND HELPERS*/
	private Vibrator vibrator;
	public ResourceHelper resourceHelper;
	
	/* APPLICATION VARIABLES */
	final Handler commonHandler = new Handler();
	private Timer timeUpdateTimer;
	private boolean vibrationActive = true;
	
	/* APPLICATION RESOURCES */
	public ArrayList<Integer> tileFacesResourcesArray;
	private ArrayList<Integer> availableImages;
	
	/* GAME LOGIC VARIABLES */
	public GameEngine gameEngine;
	public HighScoreManager hsManager;
	private ArrayList<MemoryTile> gameArray;
	private MemoryTile selectedTile;
	private ArrayList<MemoryTile> touchedTiles = new ArrayList<MemoryTile>();

	/* OVERRIDDEN Activity METHODS */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Log.v("Memodroid | onCreate", "SETTING UP VIEWS");
		this.resourceHelper = new ResourceHelper(this.getResources());
		this.vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		this.gameEngine = new GameEngine(this.resourceHelper);
		this.hsManager = new HighScoreManager(this);

		this.setUpViews();
		this.startTimeUpdate();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem commuteVibratorItem = (MenuItem)menu.getItem(1);
		if(this.vibrationActive){
			commuteVibratorItem.setTitle("Vibration Off");
		}
		else{
			commuteVibratorItem.setTitle("Vibration On");
		}
		return super.onPrepareOptionsMenu(menu);
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exitGame:
			this.finish();
			break;
			
		case R.id.newGame:
			this.newGame();
			break;
		
		case R.id.commuteVibrator:
			this.vibrationActive = !this.vibrationActive;
			
			//Intent hsIntent = new Intent(getApplicationContext(), HighScorePage.class);
            //startActivityForResult(hsIntent, 0);
			
			break;
			
		}
		
		return true;
	}	

	/* OVERRIDDEN OnClickListener METHODS */	
	
	/**
	 * onClick event listener for MemoryTile.
	 * This listener contains part of the game logic (actually, almost all of it :P )
	 */
	
	@Override
	public void onClick(View v) {
		
		//Log.d("Memodroid | onclick", "CLICK IS GOING TO BE MANAGED");
		if (touchedTiles.size() >= 2) {
			//Log.d("MemoryGridView", "MAX CLICK REACHED");
			return;
		} else {
			if (v instanceof MemoryTile) {
				//Log.d("MemoryGridView", "SELECTED POSITION: "+ ((MemoryTile) v).position);
				if (touchedTiles.contains((MemoryTile) v) == true) {
					//Log.d("MemoryGridView", "ALREADY TOUCHED");
					return;
				}
				selectedTile = (MemoryTile) v;
			} else {
				//Log.e("Memodroid | onClick", "VIEW IS NOT A MEMORY TILE!");
				return;
			}
			//Log.d("MemoryGridView", "ADD TILE IN TOUCHED ARRAY");
			touchedTiles.add(selectedTile);
		}

		if (selectedTile.getVisibility() == MemoryTile.INVISIBLE
				|| (!selectedTile.getIsBackVisible())) {
			//Log.d("MemoryGridView", "INVISIBLE OR FACE UP");
			// Tile already cleared or showing its face;
			return;
		}
		if(vibrationActive){
			vibrator.vibrate(40);
		}
		final int move = gameEngine.putMemoryTileInTileCouple(selectedTile.position);
		if (move == -1) {
			//Log.d("MemoryGridView", "BLOCKED!");
			return;
		}
		Message msg = new Message();
		msg.obj = selectedTile;

		selectedTile.swap();

		final Handler handler = new Handler();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						if (move == 2) {
							gameEngine.totalMoves++;
							pointTextView.setText(""+gameEngine.totalMoves);
							//Log.d("MemoryGridView", "TWO MOVE DONE");
							MemoryTile precedentMoveSelectedTile = gameArray
									.get(gameEngine.tileCouple[0]);
							if (selectedTile.tileFace
									.equals(precedentMoveSelectedTile.tileFace)) {
								//Log.d("MemoryGridView", "START HIDE");
								selectedTile.hide();
								precedentMoveSelectedTile.hide();
								if (gameEngine.tileCoupleRemoved() == 0) {
									//Toast.makeText(context, "WIN!",Toast.LENGTH_SHORT).show();
									stopTimeUpdate();
									showWinDialog();
								}

							} else {

								selectedTile.swap();
								precedentMoveSelectedTile.swap();

							}
							touchedTiles.remove(selectedTile);
							touchedTiles.remove(precedentMoveSelectedTile);
							gameEngine.clearMoveArray();

						}

					}
				});
			}
		}, 500);

	}	
	
	/* PRIVATE LAYOUTS METHODS */
	
	private void setUpViews() {
		setContentView(R.layout.main);
		gameSetUp();
	}

	private void gameSetUp(){
		this.initLayoutsVariables();
		if(this.gameArray == null){
			this.gameArray = new ArrayList<MemoryTile>();
		}
		else{
			this.gameArray.clear();
		}
		this.pointTextView.setText(""+this.gameEngine.totalMoves);
		this.generateAvailableImagesArray();
		this.initializeTileArray();
		this.generateGameBoard();
	}
	
	/**
	 * Retrieve all layouts objects
	 */
	private void initLayoutsVariables(){
		this.tableLayout = (TableLayout) findViewById(R.id.memoryBoard);
		this.timeTextView = (TextView) findViewById(R.id.timeTextBox);
		this.pointTextView = (TextView) findViewById(R.id.pointTextBox);
	}
	
	/**
	 * Fill the array with all available images
	 */
	private void generateAvailableImagesArray() {
		this.availableImages = this.resourceHelper.getAvailableImages();
		this.availableImages.clear();
		
		this.availableImages.add(R.drawable.android100x100);
		this.availableImages.add(R.drawable.camera100x100);
		this.availableImages.add(R.drawable.cat100x100);
		this.availableImages.add(R.drawable.cd100x100);
		this.availableImages.add(R.drawable.comix100x100);
		this.availableImages.add(R.drawable.dog100x100);
		this.availableImages.add(R.drawable.earth100x100);
		this.availableImages.add(R.drawable.emerald100x100);
		this.availableImages.add(R.drawable.film100x100);
		this.availableImages.add(R.drawable.flagita100x100);
		this.availableImages.add(R.drawable.heart100x100);
		this.availableImages.add(R.drawable.java100x100);
	}

	/**
	 * Fill the tile array shuffling all the available images
	 */
	private synchronized void initializeTileArray() {
		//Log.d("Memodroid | initializeTileArray", "TILE ARRAY INIZIALIZATION");
		if(this.tileFacesResourcesArray == null){
			this.tileFacesResourcesArray = new ArrayList<Integer>();
		}
		else{
			this.tileFacesResourcesArray.clear();
		}
		for (Integer face : this.availableImages) {
			//Log.d("Memodroid | initializeTileArray", "FACE: " + face);
			this.tileFacesResourcesArray.add(face);
			this.tileFacesResourcesArray.add(face);
		}
		Collections.shuffle(this.tileFacesResourcesArray);
	}
	
	/**
	 * Fill the array that represents the game board
	 */
	private void generateGameBoard() {
		//Log.d("Memodroid | generateGameBoard", "GAME BOARD GENERATION STARTED");
		int position = 0;
		for (int i = 0; i < 6; i++) {
			//Log.d("Memodroid | generateGameBoard", "CREATE NEW ROW");
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			tr.setGravity(Gravity.CENTER);
			for (int j = 0; j < 4; j++) {
				//Log.d("Memodroid | generateGameBoard","CREATE NEW TILE USING RESOURCE: "+ this.tileFacesResourcesArray.get(position));
				MemoryTile tile = new MemoryTile(this, R.drawable.tile100x100,this.tileFacesResourcesArray.get(position), position);
				tile.setOnClickListener(this);
				this.gameArray.add(tile);
				//Log.d("Memodroid | generateGameBoard", "ADD TILE TO THE ROW");
				tr.addView(tile);
				position++;
			}
			//Log.d("Memodroid | generateGameBoard", "ADD ROW TO TABLE");
			this.tableLayout.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	/**
	 * Create and display the Win Dialog
	 */
	private void showWinDialog(){
		dialog = new Dialog(this);
		dialog.setContentView(R.layout.win_dialog);
		TextView textNewRecord = (TextView) dialog.findViewById(R.id.textNewRecord);
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		
		HighScoreRecord score = new HighScoreRecord(-1,gameEngine.totalMoves,gameEngine.elapsedGameTime);
		
		if(hsManager.isHighScore(score)){
			hsManager.insertHighScore(score);
			dialog.setTitle("CONGRATULATIONS!");
			textNewRecord.setVisibility(TextView.VISIBLE);
			image.setImageResource(R.drawable.face_smile_big);
			image.setPadding(0, 0, 0, 0);
		}
		else{
			dialog.setTitle("WELL DONE!");
			textNewRecord.setVisibility(TextView.GONE);
			image.setImageResource(R.drawable.face_cool);
			image.setPadding(0, 10, 0, 0);
		}
		TextView textMoves = (TextView) dialog.findViewById(R.id.textMoves);
		textMoves.setText("You won this game in "+this.gameEngine.totalMoves+" moves.");
		TextView textTime = (TextView) dialog.findViewById(R.id.textTime);
		textTime.setText("Total time played: "+gameEngine.getElapsedGameTime());
		/* LISTENER FOR NEW GAME BUTTON*/
		Button newGame = (Button) dialog.findViewById(R.id.buttonNewGame);
		newGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				dialog = null;
				newGame();
			}
		});
		
		/* LISTENER FOR HIGH SCORE BUTTON*/
		Button highScore = (Button) dialog.findViewById(R.id.buttonHighScore);
		highScore.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				dialog = null;
				
				Intent hsIntent = new Intent(v.getContext(), HighScorePage.class);
	            startActivityForResult(hsIntent, 0);
			}
		});
		
		dialog.show();
	}
	
	private void startTimeUpdate(){
		if(timeUpdateTimer == null){
			timeUpdateTimer = new Timer();
		}
		timeUpdateTimer.schedule(new TimerTask() {
			public void run() {
				commonHandler.post(new Runnable() {
					public void run() {
						gameEngine.elapsedGameTime++;
						timeTextView.setText(gameEngine.getElapsedGameTime());
					}
				});
			}
		}, 0,1000);
	}
	
	private void stopTimeUpdate(){
		if(timeUpdateTimer != null){
			timeUpdateTimer.cancel();
			timeUpdateTimer = null;
		}
	}
	
	private void newGame(){
		this.tableLayout.removeAllViews();
		this.gameEngine.resetGameEngine();
		stopTimeUpdate();
		gameSetUp();
		this.startTimeUpdate();
	}

}