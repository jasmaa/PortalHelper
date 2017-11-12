package com.isolation.portalhelper;

import java.util.ArrayList;
import java.util.List;

/**
 * School class
 *
 */

public class SchoolClass {
	
	private String title;
	private String teacher;
	private String secid;
	
	public List<Assignment> assigns = new ArrayList<Assignment>();
	
	private float totalPoints;
	private float totalPossible;
	private float totalPercent;
	
	public SchoolClass(String t, String teach, String id){
		title = t;
		teacher = teach;
		secid = id;
	}

	public void calcTotalGrade(){
		totalPoints = 0;
		totalPossible = 0;
		totalPercent = 0;
		
		for(Assignment a : assigns){
			totalPoints += a.getPoints();
			totalPossible += a.getPossible();
		}
		totalPercent = totalPoints / totalPossible;
	}
	
	public String toString(){
		return title + ":" + teacher + ":" + getTotalPercent();
	}
	
	public String getTitle() {
		return title;
	}

	public String getTeacher() {
		return teacher;
	}

	public float getTotalPoints() {
		calcTotalGrade();
		return totalPoints;
	}
	public float getTotalPossible() {
		calcTotalGrade();
		return totalPossible;
	}
	public float getTotalPercent() {
		calcTotalGrade();
		return totalPercent;
	}
	public String getSecID(){
		return secid;
	}
	
	
	
}
