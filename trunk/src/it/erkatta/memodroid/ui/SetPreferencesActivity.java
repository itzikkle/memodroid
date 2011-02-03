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

package it.erkatta.memodroid.ui;

import it.erkatta.memodroid.MemodroidCoreActivity;
import it.erkatta.memodroid.R;
import it.erkatta.memodroid.persistence.HighScoreManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

/**
 * Manages the options menu
 * 
 * @author Team Replica
 * @author Marco Cattarin
 *
 */
public class SetPreferencesActivity extends PreferenceActivity implements
        YesNoDialogPreference.YesNoDialogListener {

/* COSTANTS */    
    public static final String PREFERENCE_CLEAR_HIGH_SCORES = "clearHighScores";

    /* OVERRIDE METHODS (ACTIVITY) */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);
        getPreferenceManager().setSharedPreferencesName(MemodroidCoreActivity.PREFERENCE_NAME);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        Preference eraseGameButton = getPreferenceManager().findPreference(
                PREFERENCE_CLEAR_HIGH_SCORES);
        if (eraseGameButton != null) {
            YesNoDialogPreference yesNo = (YesNoDialogPreference) eraseGameButton;
            yesNo.setListener(this);
        }

    }

    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            HighScoreManager hsManager = new HighScoreManager(getApplicationContext());
            hsManager.resetHighScores();
            Toast.makeText(this, R.string.toast_highScoresReset, Toast.LENGTH_SHORT).show();
        }
    }
}
