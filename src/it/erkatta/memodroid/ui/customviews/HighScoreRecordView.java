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

package it.erkatta.memodroid.ui.customviews;

import it.erkatta.memodroid.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A widget used to display an High Score
 * 
 * @author Marco Cattarin
 *
 */
public class HighScoreRecordView extends LinearLayout {

	private TextView positionTextView;
	private TextView movesValueTextView;
	private TextView timeValueTextView;
	private TextView difficultyLevelValueTextView;
	private TextView themeValueTextView;
	private TextView userNameTextView;
	
	
	public HighScoreRecordView(Context context, AttributeSet attrs,String position,String moves, String time, String difficultyLevel, String tilesTheme, String userName){
		
		super(context, attrs);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.high_score_record_view, this);
		
		positionTextView = (TextView) findViewById(R.id.positionTextView);
		movesValueTextView = (TextView) findViewById(R.id.movesValueTextView);
		timeValueTextView = (TextView) findViewById(R.id.timeValueTextView);
		difficultyLevelValueTextView = (TextView) findViewById(R.id.difficultyLevelValueTextView);
		themeValueTextView = (TextView) findViewById(R.id.themeValueTextView);
		userNameTextView = (TextView)  findViewById(R.id.playerNameTextView);
		
		positionTextView.setText(position);
		movesValueTextView.setText(moves);
		timeValueTextView.setText(time);
		difficultyLevelValueTextView.setText(difficultyLevel);
		themeValueTextView.setText(tilesTheme);
		userNameTextView.setText(userName);
	}

}
