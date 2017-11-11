package com.isolation.portalhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

public class Main {
	
	public static String getPSKey(HttpURLConnection conn) throws IOException{
		BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response;
       
        while ((response=reader.readLine())!=null) {
        	if(response.contains("contextData")){
        		int index = response.indexOf("value=");
        		String key = response.substring(index+7, index+71);
        		
        		return key;
        	}
        }
        return null;
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		String mcps = "https://portal.mcpsmd.org/public/";
		String test = "https://postman-echo.com/post";
		URL u = new URL(mcps);
		HttpURLConnection conn = (HttpURLConnection) u.openConnection();
		
		//System.out.println(getPSKey(conn));
		
		String user = "";
		String pass = "";
		String pstoken = "";
		String contextData = "";
		String dbpw = "";
		String pw = "";
		
		String rawData = 	"pstoken="+pstoken+","+
							"contextData="+contextData+","+
							"dbpw="+dbpw+","+
							"translator_username="+","+
							"translator_password"+","+
							"translator_ldappassword"+","+
							"returnUrl="+","+
							"serviceName=PS Parent Portal"+","+
							"serviceTicket="+","+
							"pcasServerUrl=/"+","+
							"credentialType=User Id and Password Credential"+","+
							"ldappassword"+pass+","+
							"account="+user+","+
							"pw"+pw+","+
							"translatorpw";
		String type = "application/x-www-form-urlencoded";
		String encodedData = URLEncoder.encode( rawData, "UTF-8" );
		
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
		OutputStream os = conn.getOutputStream();
		os.write(encodedData.getBytes());
		
		 //read the request
        BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response;
       
        while ((response=reader.readLine())!=null) 
            System.out.println(response);
	}
}
