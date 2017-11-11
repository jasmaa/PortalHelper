package com.isolation.portalhelper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

public class Bill {
	public static void login() throws IOException, NoSuchAlgorithmException{
		
		URL u = new URL("https://bill.mbhs.edu/friends");
		HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
		
		// Parameters
		String user = "user";
		String pass = "pass";
		
		String rawData = 	"username="+user+"&"+
							"password="+pass;
		String encodedData = URLEncoder.encode( rawData, "UTF-8" );
		
		conn.setRequestMethod("POST");
		conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded"); 
		conn.setRequestProperty("Accept-Language", "en-US,en;q=0.8");
		conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
		conn.setRequestProperty( "charset", "utf-8");
		
		// Post
		conn.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
		wr.writeBytes(rawData);
		wr.flush();
		wr.close();
		
		System.out.println(conn.getResponseCode());
		
		 //read the request
        BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String response;
       
        while ((response=reader.readLine())!=null) 
            System.out.println(response);
	}
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException{
		login();
	}
}
