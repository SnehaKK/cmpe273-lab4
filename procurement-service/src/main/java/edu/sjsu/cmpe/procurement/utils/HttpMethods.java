package edu.sjsu.cmpe.procurement.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;



/**
 * This class is used for sending and receiving
 * HTTP posts and gets. I am not using 
 * jersey client. Rather I am using the Default 
 * Java methods.
 * @author snehakulkarni
 *
 */




public class HttpMethods {
	
	public static String sendGet() {
		StringBuffer response = new StringBuffer();
		
		try {
		String url = "http://54.215.210.214:9000/orders/11536";
		URL obj = new URL(url);
		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "SnehaServiceClient");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("GET Method ------- Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return response.toString();
		
	}
	
	

	public static String  sentPost(ArrayList<String> books)  {
	try {
		String url = "http://54.215.210.214:9000/orders";
		URL obj = new URL(url);
		java.net.HttpURLConnection con = (java.net.HttpURLConnection) obj.openConnection();
       
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "SnehaServiceClient");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty("Content-Type", "application/json");
 
		JSONObject jObj = new JSONObject();
		JSONArray  jArr = new JSONArray();
		jArr.addAll(books);
		jObj.put("id", "11536");
		jObj.put("order_book_isbns", jArr);
       
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(jObj.toJSONString());
		wr.flush();
		wr.close();
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + jObj);
		System.out.println("Response Code : " + responseCode);
		
		StringBuffer response = new StringBuffer();
		
        if(responseCode == 200 ) {
        	BufferedReader in = new BufferedReader(
    		        new InputStreamReader(con.getInputStream()));
    		String inputLine;
    		
     
    		while ((inputLine = in.readLine()) != null) {
    			response.append(inputLine);
    		}
    		in.close();
        }
		
		
		//print result
		System.out.println("------response from publisher-------" +response.toString());
		
		return response.toString();
		
        }catch(Exception e) {
        	e.printStackTrace();
        }
		return null;
	}
	
	
	
}
