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
import it.erkatta.memodroid.persistence.HighScoreManager;
import it.erkatta.memodroid.subjects.HighScoreRecord;
import it.erkatta.memodroid.ui.customviews.HighScoreRecordView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * This activity is used to display all the records that belong to a given difficulty level.
 * The information about difficulty level to show is passed to the activity using Intent extras (use "difficultyLevel" as key)
 * 
 * @author Marco Cattarin
 *
 */
public class HighScoresActivity extends Activity {
   
	private String DIFFICULTY_LEVEL_EXTRA_KEY = "difficultyLevel";
	
	private LinearLayout hsContainer;
	private HighScoreManager hsManager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
        hsContainer = (LinearLayout) findViewById(R.id.containerForHighScoreRecords);
        hsManager = new HighScoreManager(getApplicationContext());
        Intent startIntent = getIntent();
        Bundle extras = startIntent.getExtras();
        if(extras != null){
        	fillWithHS(hsManager.getRecords(extras.getInt(DIFFICULTY_LEVEL_EXTRA_KEY)));
        }
    }
    
    private void fillWithHS(ArrayList<HighScoreRecord> hsList){
    	int position=1;
    	for(HighScoreRecord record : hsList){
    		hsContainer.addView(new HighScoreRecordView(this,null,new Integer(position).toString(),new Integer(record.moves).toString(),record.getElapsedGameTime(),getDifficultyLevelName(record.difficultyLevel),record.tilesTheme,record.userName));
    		position++;
    	}
    }
    
	private String getDifficultyLevelName(int difficultyLevel){
    	String difficultyLevelName = "";
		switch(difficultyLevel){
			case 0: //Kid
				difficultyLevelName = getResources().getString(R.string.highScores_difficutlyKid);
				break;
			case 1: //Easy
				difficultyLevelName = getResources().getString(R.string.highScores_difficutlyEasy);
				break;
			case 2: //Normal
				difficultyLevelName = getResources().getString(R.string.highScores_difficutlyNormal);
				break;
			case 3: //Hard
				difficultyLevelName = getResources().getString(R.string.highScores_difficutlyHard);
				break;
    	}
		return difficultyLevelName;
	}    
    
}
