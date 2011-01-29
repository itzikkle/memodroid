package it.erkatta.memodroid.helpers;

import it.erkatta.memodroid.R;

import java.util.ArrayList;

import android.content.res.Resources;
import android.widget.TextView;

public class ResourceHelper {

	private Resources resources;

	private ArrayList<Integer> availableImages;
	private ArrayList<Integer> tileFacesResourcesArray;
	private Integer totalTileCount;
	private TextView timeTextView;

	public ResourceHelper(Resources resources) {
		this.resources = resources;
	}

	public ArrayList<Integer> getAvailableImages() {
		if (this.availableImages == null) {
			int[] intArray = this.resources
					.getIntArray(R.array.availableImages);
			this.availableImages = new ArrayList<Integer>();
			for (int i : intArray) {
				availableImages.add(new Integer(i));
			}

			return this.availableImages;
		} else {
			return this.availableImages;
		}
	}

	public Integer getTotalTileCount() {
		if (this.totalTileCount == null) {
			this.totalTileCount = this.resources
					.getInteger(R.integer.tileNumber);
			return this.totalTileCount;
		} else {
			return this.totalTileCount;
		}
	}
	
}
