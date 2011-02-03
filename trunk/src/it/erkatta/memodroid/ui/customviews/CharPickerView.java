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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Represents an input widget for one character selection
 * 
 * @author Marco Cattarin
 *
 */
public class CharPickerView extends LinearLayout {

	private char[] charArray = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
	private int charArraySize;
	private int currentIndex;
	
	private ImageView upButton;
	private ImageView downButton;
	private TextView charDisplay;
	
    /* ANONYMOUS LISTENERS */
    
    /* ONCLICK LISTENERS */
    private View.OnClickListener sUpButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
        	cursorUp();
        	charDisplay.setText(""+charArray[currentIndex]);
        }
    };
	
    private View.OnClickListener sDownButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
        	cursorDown();
        	charDisplay.setText(""+charArray[currentIndex]);
        }
    };	
	
	public CharPickerView(Context context, AttributeSet attrs) {
		super(context,attrs);
		
		charArraySize = charArray.length;
		currentIndex = 0;
		
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(R.layout.charpicker_view, this);
		
		charDisplay = (TextView) findViewById(R.id.char_picker_char);
		
		upButton = (ImageView) findViewById(R.id.char_picker_upButton);
		downButton = (ImageView) findViewById(R.id.char_picker_downButton);

		if (upButton != null) {
			upButton.setOnClickListener(sUpButtonListener);
        }
        
        if (downButton != null) {
        	downButton.setOnClickListener(sDownButtonListener);
        }
		
	}
	
	private int cursorUp(){
		currentIndex++;
		if(currentIndex >= charArraySize){
			currentIndex=0;
		}
		return currentIndex;
	}
	
	private int cursorDown(){
		currentIndex--;
		if(currentIndex < 0){
			currentIndex=charArraySize-1;
		}
		return currentIndex;
	}

	private int findCharPositionInCharArray(char selectedChar){
		for(int i=0;i<charArray.length;i++){
			if(charArray[i]==selectedChar){
				return i;
			}
		}
		
		return -1; //Char not found
	}	

	/**
	 * Returns the current selected character
	 * 
	 * @return the selected character
	 */
	public char getSelectedChar(){
		return charArray[currentIndex];
	}
	
	/**
	 * Permits to set which character has to be displayed in the widget
	 * 
	 * @param selectedChar the char to display on the widget
	 */
	public void setSelectedChar(char selectedChar){
		int charPosition = findCharPositionInCharArray(selectedChar);
		if(charPosition!=-1){
			currentIndex = charPosition;
		}
		else{
			Log.e("Memodroid", "CharPickerView | SELECTED CHAR NOT AVAILABLE!");
			//TODO: Use a customized exception here;
			return;
		}
		
		charDisplay.setText(""+charArray[currentIndex]);
	}

}
