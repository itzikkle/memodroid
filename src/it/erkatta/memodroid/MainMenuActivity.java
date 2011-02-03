/*
 * Copyright (C) 2010 The Android Open Source Project
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

import it.erkatta.memodroid.ui.HighScoreActivityTab;
import it.erkatta.memodroid.ui.SetPreferencesActivity;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Manages the game main menu
 * 
 * 
 * @author Team Replica
 * @author Marco Cattarin
 *
 */
public class MainMenuActivity extends Activity {
    
    /* ACTIVITY VARIABLES */
    
    private boolean isPaused;
    
    /* LAYOUT OBJECTS */
    private View startButton;
    private View optionButton;
    private View highScoreButton;
    private View background;
    
    /* ANIMATIONS */
    private Animation buttonFlickerAnimation;
    private Animation fadeOutAnimation1;
    private Animation fadeOutAnimation2;
    private Animation fadeOutAnimation3;
    private Animation fadeInAnimation;
    
    /* ANONYMOUS LISTENERS */
    
    /* ONCLICK LISTENERS */
    private View.OnClickListener startButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!isPaused) {
                Intent i = new Intent(getBaseContext(), MemodroidCoreActivity.class);
                v.startAnimation(buttonFlickerAnimation);
                fadeOutAnimation1.setAnimationListener(new StartActivityAfterAnimation(i));
                background.startAnimation(fadeOutAnimation1);
                optionButton.startAnimation(fadeOutAnimation2);
                highScoreButton.startAnimation(fadeOutAnimation3);
                isPaused = true;
            }
        }
    };
    
    private View.OnClickListener optionButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!isPaused) {
                Intent i = new Intent(getBaseContext(), SetPreferencesActivity.class);
                v.startAnimation(buttonFlickerAnimation);
                fadeOutAnimation1.setAnimationListener(new StartActivityAfterAnimation(i));
                background.startAnimation(fadeOutAnimation1);
                startButton.startAnimation(fadeOutAnimation2);
                highScoreButton.startAnimation(fadeOutAnimation3);
                isPaused = true;
            }
        }
    };
    
    private View.OnClickListener highScorButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!isPaused) {
                Intent i = new Intent(getBaseContext(), HighScoreActivityTab.class);
                v.startAnimation(buttonFlickerAnimation);
                fadeOutAnimation1.setAnimationListener(new StartActivityAfterAnimation(i));
                background.startAnimation(fadeOutAnimation1);
                startButton.startAnimation(fadeOutAnimation2);
                optionButton.startAnimation(fadeOutAnimation2);
                isPaused = true;
            }
        }
    };

    /* OVERRIDE METHODS (ACTIVITY) */
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.RGBA_8888);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);
        setContentView(R.layout.mainmenu);
        isPaused = true;
        startButton = findViewById(R.id.startButton);
        optionButton = findViewById(R.id.optionButton);
        highScoreButton = findViewById(R.id.highScoreButton);
        background = findViewById(R.id.mainMenuBackground);
        if (startButton != null) {
            startButton.setOnClickListener(startButtonListener);
        }
        if (optionButton != null) {
            optionButton.setOnClickListener(optionButtonListener);
        }
        if (highScoreButton != null) {
            highScoreButton.setOnClickListener(highScorButtonListener);
        }
        buttonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
        fadeOutAnimation1 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeOutAnimation2 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeOutAnimation3 = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        if (background != null) {
            background.clearAnimation();
            background.invalidate();
        }
        if (startButton != null) {
            startButton.setVisibility(View.VISIBLE);
            startButton.clearAnimation();
            startButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));
            ((ImageView) startButton).setImageDrawable(getResources().getDrawable(
                    R.drawable.ui_button_start));
        }
        if (optionButton != null) {
            optionButton.setVisibility(View.VISIBLE);
            optionButton.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
            anim.setStartOffset(200L);
            optionButton.startAnimation(anim);
        }
        if (highScoreButton != null) {
            highScoreButton.setVisibility(View.VISIBLE);
            highScoreButton.clearAnimation();
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.button_slide);
            anim.setStartOffset(200L);
            highScoreButton.startAnimation(anim);
        }
    }
    
    /* INNER CLASSES */
    
    /**
     * Starts activities after the animation
     * 
     * @author Team Replica
     * @author Marco Cattarin
     *
     */
    protected class StartActivityAfterAnimation implements Animation.AnimationListener {
        private Intent intent;

        StartActivityAfterAnimation(Intent intent) {
            this.intent = intent;
        }

        public void onAnimationEnd(Animation animation) {
            startButton.setVisibility(View.INVISIBLE);
            startButton.clearAnimation();
            optionButton.setVisibility(View.INVISIBLE);
            optionButton.clearAnimation();
            highScoreButton.setVisibility(View.INVISIBLE);
            highScoreButton.clearAnimation();
            startActivity(intent);
        }

        public void onAnimationRepeat(Animation animation) {
            // TODO Auto-generated method stub
        }

        public void onAnimationStart(Animation animation) {
            // TODO Auto-generated method stub
        }
    }
}