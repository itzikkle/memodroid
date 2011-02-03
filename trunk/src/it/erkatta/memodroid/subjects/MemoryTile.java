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

import it.erkatta.memodroid.R;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MemoryTile extends ImageView {
    
	/* LOGIC VARIABLES */
	public Integer position; // Ordinal position of the tile
	public boolean isBackVisible; // Identify if the tile show its back or its face
	public boolean isGone;

	/* LAYOUT VARIABLES */
    public Integer tileBack; // Reference to drawable
    public Integer tileFace; // Reference to drawable	
	
    /* ANIMATIONS */	
    private int filpOverRequests = 0;
    private Animation tileFlipOverAnimation;
    
	/**
	 * Creates the tile
	 * 
	 * @param context to initialize the View
	 * @param tileBack is the reference to the drawable used for the back side of the tile
	 * @param tileFace is the reference to the drawable used for the face side of the tile
	 * @param position is the ordinal position of the tile in the board
	 * @param width
	 * @param height
	 */
	public MemoryTile(Context context, Integer tileBack, Integer tileFace, Integer position, int width, int height) {
		super(context);
		this.tileBack = tileBack;
		this.tileFace = tileFace;
		this.position = position;
		setLayoutParams(new LayoutParams(width,height));
		setPadding(2, 2, 2, 2);
		setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		setImageResource(tileBack);
		isBackVisible = true;
		isGone = false;
		tileFlipOverAnimation =  AnimationUtils.loadAnimation(getContext(), R.anim.tile_flipover_fade_out); //TODO: fetch animation from preference and create animations ad-hoc
		tileFlipOverAnimation.setAnimationListener(new FlipOverAfterAnimation());

	}

	/**
	 * Shows the tile
	 */
	public void show() {
		setVisibility(VISIBLE);
	}

	/**
     * Hides the tile
     */
	public void hide() {
		setVisibility(INVISIBLE);
	}
	
	/**
	 * Hides the tile and set the visibility in order to not allow it to take any space for layout purposes.
	 */
	public void gone(){
		setVisibility(GONE);
		isGone = true;
	}

	/**
	 * Flips over the tile 
	 */
	public void flipOver() {
		if(filpOverRequests>2){
			return; //Do no allow more than 2 queued flip over requests
		}
		filpOverRequests++;
        if(filpOverRequests==1){
        	this.startAnimation(tileFlipOverAnimation);
        }
	}
	
	private void swap(){
		if(isGone){
			Log.e("Memodroid", "TRYING TO FLIP OVER A GONE TILE");
			return;
		}
		Log.d("MemoryTile", "SWAP TILE N. "+position);
		if (isBackVisible) {
			Log.d("MemoryTile", " BACK IS VISIBLE. TURN TO FACE");
			setImageResource(tileFace);
			isBackVisible = false;
			//postInvalidate();
			invalidate();
		} else {
			Log.d("MemoryTile", " FACE IS VISIBLE. TURN TO BACK");
			setImageResource(tileBack);
			isBackVisible = true;
			//postInvalidate();
			invalidate();
		}
	}
	
    /* INNER CLASSES */
    
    /**
     * Manages flip over requests
     * 
     * @author Marco Cattarin
     */	
	protected class FlipOverAfterAnimation implements Animation.AnimationListener {
        
        public void onAnimationEnd(Animation animation) {
        	MemoryTile.this.swap();
        	MemoryTile.this.clearAnimation();
        	filpOverRequests--;
        	if(filpOverRequests>0){
        		MemoryTile.this.startAnimation(tileFlipOverAnimation);
        	}
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
            
        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
            
        }
        
    }
}
