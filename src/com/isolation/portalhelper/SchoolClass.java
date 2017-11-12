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
	
	public List<Assignment> assigns = new ArrayList<Assignment>();
	
	private float totalMyPoints;
	private float totalPoints;
	private float totalPercent;
	
	public SchoolClass(String t, String teach){
		title = t;
		teacher = teach;
	}

	public void calcTotalGrade(){
		totalMyPoints = 0;
		totalPoints = 0;
		totalPercent = 0;
		
		for(Assignment a : assigns){
			totalMyPoints += a.getMyScore();
			totalPoints += a.getTotalScore();
		}
		totalPercent = totalMyPoints / totalPoints;
	}
	
	public String getTitle() {
		return title;
	}

	public String getTeacher() {
		return teacher;
	}

	public float getTotalMyPoints() {
		calcTotalGrade();
		return totalMyPoints;
	}
	public float getTotalPoints() {
		calcTotalGrade();
		return totalPoints;
	}
	public float getTotalPercent() {
		calcTotalGrade();
		return totalPercent;
	}
	
	
	
}
