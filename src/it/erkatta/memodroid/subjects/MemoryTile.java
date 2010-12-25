package it.erkatta.memodroid.subjects;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class MemoryTile extends ImageView {

	private Integer tileBack; // Reference to drawable
	public Integer tileFace; // Reference to drawable
	public Integer position;

	private boolean isBackVisible; // Identify if the tile show its back or its
									// face

	public MemoryTile(Context context, Integer tileBack, Integer tileFace, Integer position) {
		super(context);
		//Log.d("MemoryTile", "NEW MEMORY TILE IS GOING TO BE CREATED!");
		this.tileBack = tileBack;
		this.tileFace = tileFace;
		this.position = position;

		//this.setLayoutParams(new LayoutParams(100, 100));
		this.setScaleType(ImageView.ScaleType.CENTER);
		this.setPadding(2, 2, 2, 2);

		this.setImageResource(this.tileBack);
		this.isBackVisible = true;

	}

	public void showTile() {
		//Log.d("MemoryTile", "SHOW TILE!");
		this.setVisibility(VISIBLE);
	}

	public void hide() {
		//Log.d("MemoryTile", "HIDE TILE!");
		this.setVisibility(INVISIBLE);
	}

	public void swap() {
		//Log.d("MemoryTile", "SWAP TILE");
		if (this.isBackVisible) {
			//Log.d("MemoryTile", " BACK IS VISIBLE. TURN TO FACE");
			this.setImageResource(this.tileFace);
			this.isBackVisible = false;
			this.postInvalidate();
		} else {
			//Log.d("MemoryTile", " FACE IS VISIBLE. TURN TO BACK");
			this.setImageResource(this.tileBack);
			this.isBackVisible = true;
			this.postInvalidate();
		}
	}

	public boolean getIsBackVisible() {
		return this.isBackVisible;
	}
}
