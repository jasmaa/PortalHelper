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

/**
 * Connects to MCPS Portal
 */
public class Portal {
	
	static String mcps = "https://portal.mcpsmd.org/public/home.html";
	static String test = "https://postman-echo.com/post";
	
	public Map getKeys() throws IOException{
		
		Map<String, String> keys = new HashMap<String, String>();
		
		// Temp vals
		keys.put("pskey", "placeholder_pskey");
		keys.put("pstoken", "placeholder_pstoken");
		
		// Temp new conn
		URL u = new URL(mcps);
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		
		// Cookies
		String cookies = "";
		for(String c : conn.getHeaderFields().get("Set-Cookie")){
			cookies += c.split(";")[0] + ";";
		}
		
		keys.put("cookies", cookies.substring(0, cookies.length() - 1));
		
		// Keys
		BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response;
        while ((response=reader.readLine())!=null) {
        	if(response.contains("contextData")){
        		int index = response.indexOf("value=");
        		String key = response.substring(index+7, index+71);
        		keys.put("pskey", key);
        	}
        	else if(response.contains("pstoken")){
        		int index = response.indexOf("value=");
        		String key = response.substring(index+7, index+49);
        		keys.put("pstoken", key);
        	}
        }
        return keys;
	}
	
	public void login() throws IOException{
		URL u = new URL(mcps);
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		
		// Get keys
		Map<String, String> keys = getKeys();
		
		// Parameters
		String user = "user";
		String pass = "pass";
		String pstoken = keys.get("pstoken");
		String contextData = keys.get("pskey");
		String dbpw = CryptoHelper.calcDBPW(contextData, pass);
		String pw = CryptoHelper.calcPW(contextData, pass);
		
		String rawData = 	"pstoken="+pstoken+"&"+
							"contextData="+contextData+"&"+
							"dbpw="+dbpw+"&"+
							"translator_username="+"&"+
							"translator_password="+"&"+
							"translator_ldappassword="+"&"+
							"returnUrl="+"&"+
							"serviceName=PS Parent Portal"+"&"+
							"serviceTicket="+"&"+
							"pcasServerUrl=/"+"&"+
							"credentialType=User Id and Password Credential"+"&"+
							"ldappassword="+pass+"&"+
							"account="+user+"&"+
							"pw="+pw+"&"+
							"translatorpw=";
		String encodedData = URLEncoder.encode( rawData, "UTF-8" );
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
		conn.setRequestProperty( "charset", "utf-8");
		
		// Cookies
		conn.setRequestProperty("Cookie", keys.get("cookies"));
		
		System.out.println(conn.getRequestProperties());
		
		// Post
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(rawData);
		wr.flush();
		wr.close();
		
		// Debug
		System.out.println(conn.getResponseCode());
		System.out.println(rawData);
		System.out.println();
		
		 //read the request
        BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response;
       
        while ((response=reader.readLine())!=null) 
            System.out.println(response);
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		Portal p = new Portal();
		p.login();
	}
}
