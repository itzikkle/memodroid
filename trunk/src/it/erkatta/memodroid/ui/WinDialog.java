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

package it.erkatta.memodroid.ui;

import it.erkatta.memodroid.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

/**
 * Dialog displayed once the user has finished the game
 * 
 * @author Marco Cattarin
 *
 */
public class WinDialog extends Dialog {

/* COSTANTS */
    /* INTENT EXTRAS */
    private String DIFFICULTY_LEVEL_EXTRA_KEY = "difficultyLevel";
    
    /* DIALOG VARIABLES */
    private Activity parent;
    private int difficultyLevel;

    /* LAYOUT VARIABLES */
    private Button newGameButton;
    private Button highScoresButton;
    private Button backToMainMenuButton;

    /* ANONYMOUS LISTENERS */
    
    /* ONCLICK LISTENERS */
    private View.OnClickListener newGameButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            getContext().startActivity(new Intent(getContext(), parent.getClass()));
            parent.finish();
            dismiss();
        }
    };

    private View.OnClickListener highScoreButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent startHighScoresActivity = new Intent(getContext(), HighScoresActivity.class);
            startHighScoresActivity.putExtra(DIFFICULTY_LEVEL_EXTRA_KEY, difficultyLevel);
            getContext().startActivity(startHighScoresActivity);
            parent.finish();
            dismiss();
        }
    };

    private View.OnClickListener backToMainMenuButtonOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            parent.finish();
            dismiss();
        }
    };

    /**
     * Create a Dialog window that show the options available after that the user finished the game
     * 
     * @param context the window manager and theme in this context to present its UI. 
     * @param parent the Activity that called this dialog
     * @param newRecord if the score performed by the user is an High Score
     * @param difficultyLevel of the game that the user has just finished
     */
    public WinDialog(Context context, Activity parent, boolean newRecord, int difficultyLevel) {
        super(context);
        setCancelable(false);
        setContentView(R.layout.win_dialog);
        this.parent = parent;
        this.difficultyLevel = difficultyLevel;
        initButtons();
        if (newRecord) {
            setTitle(R.string.win_dialog_title_highScore);
        } else {
            setTitle(R.string.win_dialog_title);
        }

    }

    private void initButtons() {
        newGameButton = (Button) findViewById(R.id.buttonNewGame);
        highScoresButton = (Button) findViewById(R.id.buttonHighScore);
        backToMainMenuButton = (Button) findViewById(R.id.buttonMainMenu);

        newGameButton.setOnClickListener(newGameButtonOnClickListener);
        highScoresButton.setOnClickListener(highScoreButtonOnClickListener);
        backToMainMenuButton.setOnClickListener(backToMainMenuButtonOnClickListener);
    }

}
