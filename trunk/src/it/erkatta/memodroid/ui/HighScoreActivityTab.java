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
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Provides tabs managements for High Score main view
 * 
 * @author Marco Cattarin
 *
 */
public class HighScoreActivityTab extends TabActivity {
	
	private String DIFFICULTY_LEVEL_EXTRA_KEY = "difficultyLevel";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.high_scores_tabs);

	    Resources res = getResources();
	    TabHost tabHost = getTabHost();
	    TabHost.TabSpec spec;
	    Intent intent;

	    intent = new Intent().setClass(this, HighScoresActivity.class);
	    intent.putExtra(DIFFICULTY_LEVEL_EXTRA_KEY, 0);
	    spec = tabHost.newTabSpec("kidHS").setIndicator("Kid",res.getDrawable(R.drawable.ic_tab_highscore)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, HighScoresActivity.class);
	    intent.putExtra(DIFFICULTY_LEVEL_EXTRA_KEY, 1);
	    spec = tabHost.newTabSpec("easyHS").setIndicator("Easy",res.getDrawable(R.drawable.ic_tab_highscore)).setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, HighScoresActivity.class);
	    intent.putExtra(DIFFICULTY_LEVEL_EXTRA_KEY, 2);
	    spec = tabHost.newTabSpec("normalHS").setIndicator("Normal",res.getDrawable(R.drawable.ic_tab_highscore)).setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, HighScoresActivity.class);
	    intent.putExtra(DIFFICULTY_LEVEL_EXTRA_KEY, 3);
	    spec = tabHost.newTabSpec("hardHS").setIndicator("Hard",res.getDrawable(R.drawable.ic_tab_highscore)).setContent(intent);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(0);
	}
}
