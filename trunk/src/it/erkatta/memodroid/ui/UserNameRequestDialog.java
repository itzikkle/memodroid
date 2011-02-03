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
import it.erkatta.memodroid.ui.customviews.CharPickerView;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

/**
 * Is a widget used to retrieve the 3 letter user name used in the High Score screen
 * 
 * @author Marco Cattarin
 *
 */
public class UserNameRequestDialog extends Dialog {

/* COSTANTS */
    /* PREFERENCES */
    public static final String PREFERENCE_NAME = "MEMODROID_PREF";
    public static final String PREFERENCE_LAST_USERNAME = "last_username";
    /* WIDGET */
    public static final String DEFAULT_USERNAME = "AAA";
    private static final String NOT_INITIALIZED_USERNAME = "---";
    
    /* WIDGET VARIABLES */
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;

    /* LAYOUT VARIABLES */
    private Button confirmButton;
    private CharPickerView firstChar;
    private CharPickerView secondChar;
    private CharPickerView thirdChar;

    public UserNameRequestDialog(Context context, View.OnClickListener confirmButtonListener) {
        super(context);
        setCancelable(false);
        setTitle(R.string.username_dialog_title);
        setContentView(R.layout.user_name_dialog);

        prefs = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();

        confirmButton = (Button) findViewById(R.id.buttonConfirmUsername);
        if (confirmButton != null) {
            confirmButton.setOnClickListener(confirmButtonListener);
        }

        firstChar = (CharPickerView) findViewById(R.id.username_first_char);
        secondChar = (CharPickerView) findViewById(R.id.username_second_char);
        thirdChar = (CharPickerView) findViewById(R.id.username_third_char);

        String savedUsername = prefs.getString(PREFERENCE_LAST_USERNAME, NOT_INITIALIZED_USERNAME);
        if (savedUsername.equals(NOT_INITIALIZED_USERNAME)) {
            setUserName(DEFAULT_USERNAME);
        } else {
            setUserName(savedUsername);
        }
    }
 
    private void setUserName(String userName) {
        char[] userNameArray = userName.toCharArray();
        firstChar.setSelectedChar(userNameArray[0]);
        secondChar.setSelectedChar(userNameArray[1]);
        thirdChar.setSelectedChar(userNameArray[2]);
    }    
    
    /**
     * Returns the selected three letters user name
     * 
     * @return the selected user name
     */
    public String getUserName() {
        String userName = "" + firstChar.getSelectedChar() + secondChar.getSelectedChar()
                + thirdChar.getSelectedChar();
        prefsEditor.putString(PREFERENCE_LAST_USERNAME, userName);
        prefsEditor.commit();
        return userName;
    }

}
