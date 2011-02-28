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

package it.erkatta.memodroid;

import it.erkatta.memodroid.persistence.HighScoreManager;
import it.erkatta.memodroid.services.GameEngine;
import it.erkatta.memodroid.subjects.HighScoreRecord;
import it.erkatta.memodroid.subjects.MemoryTile;
import it.erkatta.memodroid.ui.UserNameRequestDialog;
import it.erkatta.memodroid.ui.WinDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Represents the memory game itself
 * 
 * @author Marco Cattarin
 *
 */
public class MemodroidCoreActivity extends Activity {

    /* SYSTEM SERVICES AND HELPERS */
    private Vibrator vibrator;    
    
/* CONSTANTS */
    /* APPLICATION */
    public static final String APPLICATION_PACKAGE = "it.erkatta.memodroid";
    public static final String DEF_TYPE_DRAWABLE = "drawable";
    /* GAME */
    public static final int MAX_N_OF_TILES = 40;
    /* PREFERENCES */
    public static final String PREFERENCE_NAME = "MEMODROID_PREF";
    public static final String PREFERENCE_DIFFICULTY_LEVEL = "difficultyLevel";
    public static final String PREFERENCE_FLIPOVER_WAITING_TIME = "flipoverWaitingTime";
    public static final String PREFERENCE_ENABLE_VIBRATION = "enableVibration";
    public static final String PREFERENCE_TILES_THEME = "tilesTheme";
    public static final String PREFERENCE_TILES_BACK_THEME = "tilesBackTheme";
    public static final String PREFERENCE_SLIDE_MODE = "slideMode";

    /* DIALOGS */
    private UserNameRequestDialog userNameDialog;

    /* GAME VARIABLES */
    private int difficultyLevel;
    private Integer waitingTimeBeforeFlippingOver;    
    private Boolean isVibrationActive;
    private GameEngine gameEngine;    
    private HighScoreManager hsManager;
    private SharedPreferences prefs;
    private HighScoreRecord score;
    private Boolean isSlideModeActive;

    /* LAYOUT VARIABLES */
    private String tileBackThemeName;
    private static ArrayList<Integer> tileFacesResourcesArray;
    private static ArrayList<Integer> availableImages;
    private LinearLayout memoryBoard;
    private TextView movesTextBox;

    /* ANONYMOUS LISTENERS */
    
    /* ONCLICK LISTENERS */
    private View.OnClickListener userDialogConfirmationButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            score.userName = userNameDialog.getUserName();
            hsManager.insertHighScore(score);
            new WinDialog(MemodroidCoreActivity.this, MemodroidCoreActivity.this, true,
                    score.difficultyLevel).show();
        }
    };

    private MemoryTile.OnClickListener tileOnClickListener = new MemoryTile.OnClickListener() {
        public void onClick(View v) {
            MemoryTile tempTile = (MemoryTile) v;
            if (tempTile.getVisibility() == MemoryTile.INVISIBLE || (!tempTile.isBackVisible)) {
                return;
            }
            int move = gameEngine.doMove(tempTile);
            if (move == -1) {
                return;
            }
            gameEngine.selectedTile = tempTile;
            Log.e("Memodroid", "FLIP OVER TILE: " + gameEngine.selectedTile.position);
            gameEngine.selectedTile.flipOver();
            if (move == 2) {
                final Handler handler = new Handler();
                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    public void run() {
                        handler.post(new Runnable() {
                            public void run() {
                                gameEngine.totalMoves++;
                                movesTextBox.setText(new Integer(gameEngine.totalMoves).toString());
                                MemoryTile precedentMoveselectedTile = gameEngine.movesArray[0];
                                if (gameEngine.selectedTile.tileFace
                                        .equals(precedentMoveselectedTile.tileFace)) {
                                    if (isVibrationActive) {
                                        vibrator.vibrate(60);
                                    }
                                    if(isSlideModeActive){
                                        gameEngine.selectedTile.gone();
                                        precedentMoveselectedTile.gone();
                                    }
                                    else{
                                        gameEngine.selectedTile.hide();
                                        precedentMoveselectedTile.hide();
                                    }
                                    
                                    if (gameEngine.tileCoupleRemoved() == 0) {
                                        gameEngine.stopTimeUpdate();
                                        score = new HighScoreRecord(gameEngine.totalMoves,
                                                gameEngine.elapsedGameTime, difficultyLevel,
                                                tileBackThemeName, 0);
                                        hsManager = new HighScoreManager(MemodroidCoreActivity.this);
                                        if (hsManager.isHighScore(score)) {
                                            userNameDialog = new UserNameRequestDialog(
                                                    MemodroidCoreActivity.this,
                                                    userDialogConfirmationButtonListener);
                                            userNameDialog.show();
                                        } else {
                                            new WinDialog(MemodroidCoreActivity.this,
                                                    MemodroidCoreActivity.this, false,
                                                    score.difficultyLevel).show();
                                        }
                                    }
                                } else {
                                    gameEngine.selectedTile.flipOver();
                                    precedentMoveselectedTile.flipOver();
                                }
                                gameEngine.clearMoveArray();
                            }
                        });
                    }
                }, waitingTimeBeforeFlippingOver);
            }
        }
    };

    /* OVERRIDE METHODS (ACTIVITY) */
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        prefs = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE);
        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        isVibrationActive = prefs.getBoolean(PREFERENCE_ENABLE_VIBRATION, false);
        isSlideModeActive = prefs.getBoolean(PREFERENCE_SLIDE_MODE, false);
        waitingTimeBeforeFlippingOver = Integer.parseInt(prefs.getString(
                PREFERENCE_FLIPOVER_WAITING_TIME, "1000"));
        setUpViews();
    }

    /* PRIVATE LAYOUTS METHODS */

    private void setUpViews() {
        setContentView(R.layout.core);
        gameSetUp();
    }

    private void gameSetUp() {
        //TODO: Move in a separate init function
        movesTextBox = (TextView) findViewById(R.id.movesTextBox);
        memoryBoard = (LinearLayout) findViewById(R.id.memoryBoard);
        difficultyLevel = Integer.parseInt(prefs.getString(PREFERENCE_DIFFICULTY_LEVEL, "2"));
        this.loadAvailableImagesArray(prefs.getString(PREFERENCE_TILES_THEME, "animals"));
        this.initializeTileArray();
        this.drawMemoryBoard(difficultyLevel);
        gameEngine.startTimeUpdate();
    }

    private void drawMemoryBoard(int difficultyLevel) {
        tileBackThemeName = prefs.getString(PREFERENCE_TILES_BACK_THEME, "question");
        switch (difficultyLevel) {
        case 0: // 4x3 -- 12 tiles
            drawMemoryBoard(4, 3, tileBackThemeName);
            break;
        case 1: // 5x4 -- 20 tiles
            drawMemoryBoard(5, 4, tileBackThemeName);
            break;
        case 2: // 6x5 -- 30 tiles
            drawMemoryBoard(6, 5, tileBackThemeName);
            break;
        case 3: // 8x5 -- 40 tiles (MAX)
            drawMemoryBoard(8, 5, tileBackThemeName);
            break;
        }
    }

    private void drawMemoryBoard(int rows, int columns, String tileBackThemeName) {
        ArrayList<Integer> subTileFacesResourcesArray = new ArrayList<Integer>(
                tileFacesResourcesArray.subList(0, rows * columns));
        Collections.shuffle(subTileFacesResourcesArray);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay();
        int width = display.getWidth();
        Log.d("Memodroid", "drawMemoryBoard | WIDTH: " + width);
        int height = display.getHeight()
                - ((RelativeLayout) findViewById(R.id.infoBoard)).getHeight();
        Log.d("Memodroid", "drawMemoryBoard | HEIGHT: " + height);

        int sideLenghtOnWidth = (width - (4 * columns)) / columns;
        int sideLenghtOnHeight = (height - (4 * rows)) / rows;
        int sideLength;
        if (sideLenghtOnWidth < sideLenghtOnHeight) {
            sideLength = sideLenghtOnWidth;
        } else {
            sideLength = sideLenghtOnHeight;
        }
        Log.d("Memodroid", "drawMemoryBoard | SIDE LENGHT: " + sideLength);
        int position = 0;
        Integer tilebackResource = getResources().getIdentifier("tileback_" + tileBackThemeName,
                DEF_TYPE_DRAWABLE, APPLICATION_PACKAGE);
        for (int r = 0; r < rows; r++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
            for (int c = 0; c < columns; c++) {
                Log.d("Memodroid", "drawMemoryBoard | POSITION: " + position);
                MemoryTile tile = new MemoryTile(this, tilebackResource,
                        subTileFacesResourcesArray.get(position), position, sideLength, sideLength);
                tile.setOnClickListener(tileOnClickListener);
                row.addView(tile);
                position++;
            }
            memoryBoard.addView(row);
        }
        //Game engine is initialized here 'cause it can calculate dynamically the total number of tiles
        gameEngine = new GameEngine(rows * columns, (TextView) findViewById(R.id.timeTextBox));
    }

    /**
     * Fills the array with all available images
     * 
     * @param tileThemeName the name of the theme used for tile faces
     */
    private void loadAvailableImagesArray(String tileThemeName) {
        if (availableImages == null) {
            availableImages = new ArrayList<Integer>();
        } else {
            availableImages.clear();
        }
        Integer resId;
        int tileFacesQuantity = MAX_N_OF_TILES / 2;
        for (int i = 0; i < tileFacesQuantity; i++) {

            resId = getResources().getIdentifier(tileThemeName + "_" + i, DEF_TYPE_DRAWABLE,
                    APPLICATION_PACKAGE);
            if (resId != 0) {
                availableImages.add(resId);
            } else {// Resource not found
                    // TODO: Use a Toast to inform the user that the theme he
                    // has selected is corrupted
                Log.e("Memodroid", "MemodroidCoreActivity | TILES THEME CORRUPTED OR NOT FOUND!");
            }
        }
        Collections.shuffle(availableImages);
    }

    /**
     * Fills the tile array shuffling all the available images
     * 
     */
    private synchronized void initializeTileArray() {
        if (tileFacesResourcesArray == null) {
            tileFacesResourcesArray = new ArrayList<Integer>();
        } else {
            tileFacesResourcesArray.clear();
        }
        for (Integer face : availableImages) {
            tileFacesResourcesArray.add(face);
            tileFacesResourcesArray.add(face);
        }
    }

}
