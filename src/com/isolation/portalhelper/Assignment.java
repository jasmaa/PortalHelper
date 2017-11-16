package com.isolation.portalhelper;

import java.io.Serializable;

/**
 * School assignment
 *
 */

public class Assignment implements Serializable{
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
	public void setPoints(float p){
		points = p;
		percent = points / possible;
	}
	public float getPossible() {
		return possible;
	}
	public void setPossible(float p){
		possible = p;
		percent = points / possible;
	}
	public float getPercent() {
		return percent;
	}
}
