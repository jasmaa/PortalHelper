package com.isolation.portalhelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class MD5 {
	
	// MD5
	public static String b64pw(final String inputString) throws NoSuchAlgorithmException {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(inputString.getBytes());

	    byte[] digest = md.digest();

	    return Base64.getEncoder().encodeToString(digest);
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException{
		System.out.println(b64pw("pass"));
	}
}
