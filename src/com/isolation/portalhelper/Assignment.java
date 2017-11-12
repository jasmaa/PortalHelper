package com.isolation.portalhelper;

/**
 * School assignment
 *
 */

public class Assignment {
	private String title;
	private float points;
	private float possible;
	private float percent;
	
	public Assignment(String t, float frac, float tot){
		title = t;
		points = frac;
		possible = tot;
		percent = frac / tot;
	}

	public String toString(){
		return title + ":" + percent;
	}
	
	public String getTitle() {
		return title;
	}
	public float getPoints() {
		return points;
	}
	public float getPossible() {
		return possible;
	}
	public float getPercent() {
		return percent;
	}
}
