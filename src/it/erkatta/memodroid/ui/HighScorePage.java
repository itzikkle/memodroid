package it.erkatta.memodroid.ui;

import it.erkatta.memodroid.R;
import it.erkatta.memodroid.persistence.HighScoreManager;
import it.erkatta.memodroid.subjects.HighScoreRecord;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HighScorePage extends Activity {
	
	private ArrayList<HighScoreRecord> records;
	private HighScoreManager hsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.hsManager = new HighScoreManager(this);
		
		this.records = this.hsManager.getRecords();
		
		this.setUpViews();
	}

	/* PRIVATE LAYOUTS METHODS */
	
	private void setUpViews() {
		setContentView(R.layout.highscores);
		populateHighScoreTable();
	}
	
	private void populateHighScoreTable(){
		//Log.d("HighScorePage | populateHighScoreTable", "HIGH SCORE TABLE GENERATION STARTED");
		TableLayout hsTable = (TableLayout) findViewById(R.id.highScoresTable);
		
		for(HighScoreRecord record : this.records){
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new TableRow.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			TextView positionTextView = new TextView(this);
			positionTextView.setText("#"+record.position);
			positionTextView.setTextSize(30);
			positionTextView.setPadding(0, 0, 20, 0);
			positionTextView.setTextColor(Color.WHITE);
			positionTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			
			tr.addView(positionTextView);
			
			TextView movesTextView = new TextView(this);
			movesTextView.setText(""+record.moves);
			movesTextView.setTextSize(30);
			movesTextView.setPadding(0, 0, 20, 0);
			movesTextView.setTextColor(Color.WHITE);
			movesTextView.setGravity(Gravity.CENTER_HORIZONTAL);
			
			tr.addView(movesTextView);
			
			TextView elapsedGametimeTextView = new TextView(this);
			elapsedGametimeTextView.setText(record.getElapsedGameTime());
			elapsedGametimeTextView.setTextSize(30);
			elapsedGametimeTextView.setTextColor(Color.WHITE);
			elapsedGametimeTextView.setGravity(Gravity.RIGHT);
			
			tr.addView(elapsedGametimeTextView);
			
			hsTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}
	
	 private void appendRow(TableLayout table, int pos, String name,
	          String created, String location, String comments, int score, boolean selected) {
	    setProgressBarVisibility(false);
	    TableRow row = new TableRow(this);
	    if (selected) {
	      row.setBackgroundColor(Color.argb(0x99, 0xff, 0x00, 0x00));
	      row.setTag("TEST");
	    } else if (pos % 2 == 1) {
	      row.setBackgroundColor(Color.argb(0x33, 0x99, 0x99, 0x99));
	    }

	    // Number
	    TextView numberView = new TextView(this);
	    if (pos < 10) {
	      numberView.setText((pos < 9 ? " " : "") + (pos + 1) + ". ");
	    }
	    numberView.setTextSize((int) (1.2 * numberView.getTextSize()));
	    numberView.setPadding(5, 3, 0, 3);
	    row.addView(numberView, new TableRow.LayoutParams(
	            ViewGroup.LayoutParams.WRAP_CONTENT,
	            ViewGroup.LayoutParams.WRAP_CONTENT));

	    // Name, Date, Comments
	    TextView nameDateCommentsView = new TextView(this);
	    setNameDateLocationComments(nameDateCommentsView, name, created, location, comments);
	    row.addView(nameDateCommentsView, new TableRow.LayoutParams());

	    // Score
	    TextView scoreView = new TextView(this);
	    scoreView.setText(score > 0 ? "" + score : "");
	    scoreView.setTypeface(scoreView.getTypeface(), Typeface.BOLD);
	    scoreView.setPadding(0, 3, 15, 3);
	    scoreView.setGravity(Gravity.RIGHT | Gravity.TOP);
	    row.addView(scoreView, new TableRow.LayoutParams(
	            ViewGroup.LayoutParams.WRAP_CONTENT,
	            ViewGroup.LayoutParams.WRAP_CONTENT));

	    table.addView(row, new TableLayout.LayoutParams());
	    if (selected) {
	      // Scroll highlighted cell into view.
	      row.setFocusableInTouchMode(true);
	      row.requestFocus();
	    }
	  }
	  /**
	   * Formats the name, date, and comments for display in the table.
	   * 
	   * @param view
	   * @param name
	   * @param created
	   * @param comments
	   */
	  private void setNameDateLocationComments(TextView view, String name, String created, String location,
	          String comments) {
	    int nameLength = name.length() + 3;
	    int createdLocationLength = created.length();
	    StringBuffer nameDateComments = new StringBuffer(name);
	    nameDateComments.append("   ");
	    nameDateComments.append(created);
	    if (location != null && location.length() > 0) {
	      nameDateComments.append(" - ");
	      nameDateComments.append(location);
	      createdLocationLength += 3 + location.length();
	    }
	    if (comments.length() > 0) {
	      nameDateComments.append("\n");
	      nameDateComments.append(comments);
	    }

	    view.setText(nameDateComments.toString(), TextView.BufferType.SPANNABLE);
	    Spannable str = (Spannable) view.getText();
	    str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0,
	            name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

	    str.setSpan(new ForegroundColorSpan(android.graphics.Color.GRAY),
	                nameLength, nameLength + createdLocationLength,
	            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    view.setPadding(0, 3, 0, 3);
	    view.setGravity(Gravity.LEFT | Gravity.TOP);
	  }	
}
