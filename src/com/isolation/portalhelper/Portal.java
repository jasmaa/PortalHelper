package com.isolation.portalhelper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

/**
 * Connects to MCPS Portal
 */
public class Portal {
	
	static String mcps = "https://portal.mcpsmd.org/";
	Map<String, String> allCookies = new HashMap<String, String>();
	
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
	public void login() throws IOException{
	
		Connection.Response loginForm = Jsoup.connect(mcps)
	            .method(Connection.Method.GET)
	            .execute();
		
		// Parameters
		Map<String, String> keys = getKeys(loginForm.body());
		
		String user = Credentials.user;
		String pass = Credentials.pass;
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
		System.out.println(allCookies);
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
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		Portal p = new Portal();
		p.login();
		p.nav("guardian/home.html#termGrades");
		// Powerschool why is it so hard to get grades??? :((((
		// https://portal.mcpsmd.org/guardian/prefs/termsData.json?schoolid=757
	}
}
