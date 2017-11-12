package com.isolation.portalhelper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Connects to MCPS Portal
 * Represents a student
 */
public class Portal {
	
	private static String mcps = "https://portal.mcpsmd.org/";
	private Map<String, String> allCookies = new HashMap<String, String>();
	
	private String schoolId;
	private List<SchoolClass> classes = new ArrayList<SchoolClass>();
	
	/**
	 * Gets pskey and pstoken from html
	 * 
	 * @param body
	 * @return
	 * @throws IOException
	 */
	public Map getKeys(String body) throws IOException{
		
		Map<String, String> keys = new HashMap<String, String>();
		
		// Keys
        if(body.contains("contextData")){
        	String strToFind = "id=\"contextData\" value=\"";
        	int index = body.indexOf(strToFind)+strToFind.length();
        	String key = body.substring(index, index+64);
        	keys.put("pskey", key);
        }
        if(body.contains("pstoken")){
        	String strToFind = "name=\"pstoken\" value=\"";
        	int index = body.indexOf(strToFind)+strToFind.length();
        	String key = body.substring(index, index+42);
        	keys.put("pstoken", key);
        }
        return keys;
	}
	
	/**
	 * Logs user in
	 * 
	 * @throws IOException
	 */
	public void login(String user, String pass) throws IOException{
	
		Connection.Response loginForm = Jsoup.connect(mcps)
	            .method(Connection.Method.GET)
	            .execute();
		
		// Parameters
		Map<String, String> keys = getKeys(loginForm.body());
		
		String pstoken = keys.get("pstoken");
		String contextData = keys.get("pskey");
		String dbpw = CryptoHelper.calcDBPW(contextData, pass);
		String pw = CryptoHelper.calcPW(contextData, pass);
		
		allCookies.putAll(loginForm.cookies());
		
		Connection.Response loginToHome = Jsoup.connect(mcps + "guardian/home.html")
				.data("cookieexists", "false")
				.data(	"pstoken", pstoken,
						"contextData", contextData,
						"dbpw", dbpw,
						"translator_username", "",
						"translator_password", "",
						"translator_ldappassword", "",
						"returnUrl", "",
						"serviceName", "PS Parent Portal",
						"serviceTicket", "",
						"pcasServerUrl", "/",
						"credentialType", "User Id and Password Credential",
						"ldappassword", pass,
						"account", user,
						"pw", pw,
						"translatorpw", ""
				)
				.cookies(allCookies)
				.method(Connection.Method.POST)
	            .execute();
		
		allCookies.putAll(loginToHome.cookies());
		allCookies.put("uiStateCont", "null");
		allCookies.put("uiStateNav", "null");
		
		// School id
		schoolId = allCookies.get("currentSchool");
	}
	
	/**
	 * Navigates to sub dir
	 * 
	 * @param url
	 * @throws IOException
	 */
	public void nav(String url) throws IOException{
		Response nav = Jsoup.connect(mcps + url)
				.data("cookieexists", "false")
				.cookies(allCookies)
				.method(Connection.Method.GET)
				.execute();
		
		allCookies.putAll(nav.cookies());
		
		System.out.println(nav.body());
	}
	
	/**
	 * Gets all grades
	 * 
	 * @throws IOException
	 */
	public void getGrades() throws IOException{
		Response nav = Jsoup.connect(mcps + "guardian/prefs/gradeByCourseSecondary.json?schoolid=" + schoolId)
				.data("cookieexists", "false")
				.cookies(allCookies)
				.method(Connection.Method.GET)
				.execute();
		
		// Run through classes
		Gson gson = new Gson();
		JsonArray json = gson.fromJson(nav.body(), JsonArray.class);
		
		for(JsonElement e : json){
			
			JsonObject schoolClass = e.getAsJsonObject();
			
			if(!"{}".equals(e.toString())){
				classes.add(new SchoolClass(
						schoolClass.get("courseName").getAsString(),
						schoolClass.get("teacher").getAsString(),
						schoolClass.get("sectionid").getAsString()
					));
			}
		}
		
		// Add assignments for each school class
		for(SchoolClass c : classes){
			addAssignments(c);
		}
	}
	
	/**
	 * Add assignments for a school class
	 * 
	 * @param c
	 * @throws IOException
	 */
	public void addAssignments(SchoolClass c) throws IOException{
		Response nav = Jsoup.connect(mcps + "guardian/prefs/assignmentGrade_AssignmentDetail.json?schoolid=" + schoolId + "&secid=" + c.getSecID())
				.data("cookieexists", "false")
				.cookies(allCookies)
				.method(Connection.Method.GET)
				.execute();
		
		// Run through assignments
		Gson gson = new Gson();
		JsonArray json = gson.fromJson(nav.body(), JsonArray.class);
				
		for(JsonElement e : json){
					
			JsonObject assign = e.getAsJsonObject();
					
			if(!"{}".equals(e.toString())){
				//Detect excused
				if("X".equals(assign.get("Points").getAsString())){
					continue;
				}
				
				c.assigns.add(new Assignment(
						assign.get("Description").toString(),
						assign.get("Points").getAsFloat(),
						assign.get("Possible").getAsFloat()
					));
			}
		}
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		Portal p = new Portal();
		p.login(Credentials.user, Credentials.pass);
		p.getGrades();
		
		for(SchoolClass c : p.classes){
			System.out.println(c);
		}
	}
}
