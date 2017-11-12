package com.isolation.portalhelper;

/**
 * School assignment
 *
 */

public class Assignment {
	private String title;
	private float myScore;
	private float totalScore;
	private float percent;
	
	public Assignment(String t, float frac, float tot){
		title = t;
		myScore = frac;
		totalScore = tot;
		percent = frac / tot;
	}

	public String getTitle() {
		return title;
	}
	public float getMyScore() {
		return myScore;
	}
	public float getTotalScore() {
		return totalScore;
	}
	public float getPercent() {
		return percent;
	}
}
