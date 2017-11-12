package com.isolation.portalhelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Connects to MCPS Portal
 */
public class Portal {
	
	static String mcps = "https://portal.mcpsmd.org/";
	static String test = "https://postman-echo.com/post";
	
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
	
	public void login() throws IOException{
	
		Connection.Response loginForm = Jsoup.connect(mcps)
	            .method(Connection.Method.GET)
	            .execute();
		
		Response res = loginForm;
		
		// Parameters
		Map<String, String> keys = getKeys(res.body());
		
		String user = Credentials.user;
		String pass = Credentials.pass;
		String pstoken = keys.get("pstoken");
		String contextData = keys.get("pskey");
		String dbpw = CryptoHelper.calcDBPW(contextData, pass);
		String pw = CryptoHelper.calcPW(contextData, pass);
		
		Document document = Jsoup.connect(mcps + "guardian/home.html#/termGrades/assignment?schoolId=757&studentNumber=327059&termId=MP1&sectionId=29850001")
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
				.cookies(loginForm.cookies())
	            .post();
		
		System.out.println(document.body());
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		Portal p = new Portal();
		p.login();
	}
}
