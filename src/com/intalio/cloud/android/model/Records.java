package com.intalio.cloud.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.StrictMode;
import android.util.Log;

import com.intalio.cloud.android.common.Constants;

/**
 * This class read's records in json by http and return as JSONObject or
 * String[] of object name. It also returns the related objects to any object.
 *
 */
public class Records {
	
	//TODO: remove StrictMode.enableDefaults().
	public Records(){
		StrictMode.enableDefaults();  
	}
	
	/**
	 * Returns related objects to any object.
	 * @param objectName
	 * @return
	 */
	public String[] getRelatedObjects(String objectName) {
		if (objectName.equals(Constants.OBJECT_ACCOUNTS)) {
			return new String[] {
					Constants.OBJECT_CONTACTS,
					Constants.OBJECT_CONTRACTS,
					Constants.OBJECT_INVOICES,
					Constants.OBJECT_OPPORTUNITIES,
					Constants.OBJECT_QUOTES
			};
		} else if(objectName.equals(Constants.OBJECT_PRODUCTS)){
			return new String[] { 
					Constants.OBJECT_PRICE_LISTS
			};
		} else {
			return new String[]{};
		}
	}
	
	/**
	 * Get objectName in lower case without any spaces, 
	 * to be used in url for query.
	 * @param objectName
	 * @return
	 */
	public String getObjectName(String objectName){
		return objectName.toLowerCase().replaceAll("\\s+", "");
	}
	
	
	/**
	 * Get JSONObject from name and xid.
	 * @param object
	 * @param xid
	 * @return
	 * @throws JSONException
	 * @throws IOException 
	 */
	public JSONObject getRecordInJson(String object, String xid)
			throws JSONException, IOException {
		JSONObject recordJsonObject = null;
		String objectName = getObjectName(object);
		Log.d("Records", "objectNameInLowerCase: " + objectName);
		String records = readRecords(Constants.URL + "?action=" + objectName + "&subaction=getinfo&id=" + xid);			
		try {
			JSONObject jsonRecords = new JSONObject(records);
			JSONArray jsonArray = (JSONArray) jsonRecords.get(objectName);
			recordJsonObject = jsonArray.getJSONObject(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return recordJsonObject;
	}
	
	/**
	 * Get list of records of any object with limit.
	 * @param object
	 * @param page
	 * @return
	 * @throws IOException 
	 */
	public List<String> getRecordNames(String object, int page) throws IOException{
		String objectName = getObjectName(object);
		Log.d("Records", "objectNameInLowerCase: " + objectName);
		Log.d("Records", "page is:: " + page);
		String records = readRecords(Constants.URL + "?action=" + objectName + "&subaction=getrows&startfrom=1&upto=" + page*Constants.RECORD_LIMIT);	
		List<String> recordNames = null;
		try {
			JSONObject jsonRecords = new JSONObject(records);
			JSONArray jsonArray = (JSONArray) jsonRecords.get(objectName);
			recordNames = new ArrayList<String>();
			for(int index = 0; index < jsonArray.length(); index++){
				JSONObject jsonRecord = (JSONObject) jsonArray.get(index);
				String xid = (String) jsonRecord.get("xid");
				String recordName = (String) jsonRecord.get("name");
				Log.d("Records", "recordName is: " + recordName);
				recordNames.add(xid + Constants.DELIMITER + recordName);
			}			
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		return recordNames;
	}
	
	
	/**
	 * Reads json data.
	 * @param url
	 * @return
	 * @throws IOException 
	 */
	public String readRecords(String url) throws IOException {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		InputStream content = null;
		BufferedReader reader = null;
		try {
			Log.e("Records", "Getting response");
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				content = entity.getContent();
				reader = new BufferedReader(new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
				}
				reader.close();
				content.close();				
			} else {
				Log.e("Records", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(reader != null){
				reader.close();
			}
			if(content != null){
				content.close();
			}
		}
		return builder.toString();		
	}

	/**
	 * Get related object records.
	 * @param object
	 * @param primaryObject
	 * @param xid
	 * @param page
	 * @return
	 * @throws JSONException 
	 * @throws IOException 
	 */
	public List<String> getRecordNames(String object,
			String primaryObject, String xid, int page) throws JSONException, IOException {
		String objectName = getObjectName(object);
		String primaryObjectName = getObjectName(primaryObject);
		String records = readRecords(Constants.URL + "?action=" + objectName + 
				"&subaction=getby" + primaryObjectName + "&id=" + xid + "&startfrom=1&upto=" + page*Constants.RECORD_LIMIT);	
		List<String> recordNames = null;
		//try {
			JSONObject jsonRecords = new JSONObject(records);
			JSONArray jsonArray = (JSONArray) jsonRecords.get(objectName);
			recordNames = new ArrayList<String>();
			if(object.equals(Constants.OBJECT_CONTACTS)){
				for(int index = 0; index < jsonArray.length(); index++){
					JSONObject jsonRecord = (JSONObject) jsonArray.get(index);				
					String contactName = (String) jsonRecord.get("name");
					String mobile = (String) jsonRecord.get("Mobile No");
					String email = (String) jsonRecord.get("Email ID");
					recordNames.add(contactName + Constants.DELIMITER + mobile + 
							Constants.DELIMITER + email);
				}
			}else {			
				for(int index = 0; index < jsonArray.length(); index++){
					JSONObject jsonRecord = (JSONObject) jsonArray.get(index);				
					String xidOfRelatedObject = (String) jsonRecord.get("xid");
					String recordName = (String) jsonRecord.get("name");
					Log.d("Records", "recordName is: " + recordName);
					recordNames.add(xidOfRelatedObject + Constants.DELIMITER + recordName);
				}
			}			
		/*} catch (JSONException e) {
			e.printStackTrace();
		}*/		
		return recordNames;
	}

}
