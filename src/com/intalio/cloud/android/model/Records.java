package com.intalio.cloud.android.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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

import android.os.AsyncTask;
import android.util.Log;

import com.intalio.cloud.android.common.Constants;

/**
 * This class read's records in json by http and return as JSONObject or
 * List. It also returns the related objects to any object.
 *
 */
public class Records {
	
	private AsyncTask<Object, Void, List<String>> backgroundTaskForListResult;
	private AsyncTask<Object, Void, JSONObject> backgroundTaskForJSONResult;
	
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
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public JSONObject getRecordInJson(String object, String xid)
			throws JSONException, IOException, InterruptedException, ExecutionException {
		if (backgroundTaskForJSONResult != null) {
			backgroundTaskForJSONResult.cancel(false);
		}		
		backgroundTaskForJSONResult = new AsyncTask<Object, Void, JSONObject>(){
			@Override
			protected JSONObject doInBackground(Object... params) {
				JSONObject recordJsonObject = null;
				String object = (String) params[0];
				String xid = (String) params[1];
				String objectName = getObjectName(object);
				Log.d("Records", "objectNameInLowerCase: " + objectName);
				String records = null;
				try {
					records = readRecords(Constants.URL + "?action=" + objectName + "&subaction=getinfo&id=" + xid);
				} catch (IOException e1) {
					e1.printStackTrace();
				}			
				try {
					JSONObject jsonRecords = new JSONObject(records);
					JSONArray jsonArray = (JSONArray) jsonRecords.opt(objectName);
					if(jsonArray != null){
						recordJsonObject = jsonArray.getJSONObject(0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return recordJsonObject;
			}
		}.execute(object, xid);
		return backgroundTaskForJSONResult.get();
	}
	
	/**
	 * Initialize list of records of any object with limit.
	 * @param object
	 * @param page
	 * @return
	 * @throws IOException
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public List<String> getRecordNames(String object, int page) throws IOException, InterruptedException, ExecutionException{
		Log.d("Records", "AsyncTask going to execute");
		if(page == 1){
			if (backgroundTaskForListResult != null) {
				backgroundTaskForListResult.cancel(false);
			}		
			backgroundTaskForListResult = new AsyncTask<Object, Void, List<String>>(){
				@Override
				protected List<String> doInBackground(Object... params) {
					String object = (String) params[0];
					int page = (Integer) params[1];		
					return getRecords(object, page);
				}			
			}.execute(object, page);
			return backgroundTaskForListResult.get();
		} else{
			return getRecords(object, page);
		}
	}
	
	
	public List<String> getRecords(String object, int page){
		String objectName = getObjectName(object);
		Log.d("Records", "objectNameInLowerCase: " + objectName);
		Log.d("Records", "page is:: " + page);
		String records = null;
		int limit = page*Constants.RECORD_LIMIT;
		int offset = 1;
		if(page != 1){
			offset = ((page-1) * Constants.RECORD_LIMIT) + 1;
		}
		String url = Constants.URL + "?action=" + objectName + "&subaction=getrows&startfrom=" + 
									offset + "&upto=" + limit;
		Log.d("Records", "Url is: " + url);
		try {
			records = readRecords(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
		List<String> recordNames = null;
		try {
			JSONObject jsonRecords = new JSONObject(records);
			JSONArray jsonArray = (JSONArray) jsonRecords.opt(objectName);
			recordNames = new ArrayList<String>();
			if(jsonArray != null){
				for(int index = 0; index < jsonArray.length(); index++){
					JSONObject jsonRecord = (JSONObject) jsonArray.opt(index);
					String xid = (String) jsonRecord.opt("xid");
					String recordName = (String) jsonRecord.opt("name");
					Log.d("Records", "recordName is: " + recordName);
					recordNames.add(xid + Constants.DELIMITER + recordName);
				}
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
				Log.d("Records", "Failed to download file");
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
	public List<String> getRecords(String object,
			String primaryObject, String xid, int page) throws JSONException, IOException {
		String objectName = getObjectName(object);
		String primaryObjectName = getObjectName(primaryObject);		
		int limit = page*Constants.RECORD_LIMIT;
		int offset = 1;
		if(page != 1){
			offset = ((page-1) * Constants.RECORD_LIMIT) + 1;
		}
		String url = Constants.URL + "?action=" + objectName + 
				"&subaction=getby" + primaryObjectName + "&id=" +
				xid + "&startfrom=" + offset + "&upto=" + limit;
		Log.d("Records", "Url is: " + url);		
		String records = readRecords(url);	
		List<String> recordNames = null;
		JSONObject jsonRecords = new JSONObject(records);
		JSONArray jsonArray = (JSONArray) jsonRecords.opt(objectName);		
		recordNames = new ArrayList<String>();
		if(jsonArray != null){
			if(object.equals(Constants.OBJECT_CONTACTS)){
				for(int index = 0; index < jsonArray.length(); index++){
					JSONObject jsonRecord = (JSONObject) jsonArray.get(index);				
					String contactName = (String) jsonRecord.opt("name");
					String mobile = (String) jsonRecord.opt("Mobile No");
					String email = (String) jsonRecord.opt("Email ID");
					recordNames.add(contactName + Constants.DELIMITER + mobile + 
							Constants.DELIMITER + email);
				}
			}else {			
				for(int index = 0; index < jsonArray.length(); index++){
					JSONObject jsonRecord = (JSONObject) jsonArray.get(index);				
					String xidOfRelatedObject = (String) jsonRecord.opt("xid");
					String recordName = (String) jsonRecord.opt("name");
					Log.d("Records", "recordName is: " + recordName);
					recordNames.add(xidOfRelatedObject + Constants.DELIMITER + recordName);
				}
			}	
		}
		return recordNames;
	}
	
	public List<String> getRecordNames(String object,
			String primaryObject, String xid, int page) throws InterruptedException, ExecutionException, JSONException, IOException{
		if(page == 1){
			if (backgroundTaskForListResult != null) {
				backgroundTaskForListResult.cancel(false);
			}		
			backgroundTaskForListResult = new AsyncTask<Object, Void, List<String>>(){
				@Override
				protected List<String> doInBackground(Object... params) {
					String object = (String) params[0];
					String primaryObject = (String) params[1];
					String xid = (String) params[2];
					int page = (Integer) params[3];	
					List<String> recordNames = null;
					try {
						recordNames = getRecords(object, primaryObject, xid, page);
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					return recordNames;
				}			
			}.execute(object, primaryObject, xid, page);
			return backgroundTaskForListResult.get();
		} else{
			return getRecords(object, primaryObject, xid, page);
		}
	}

}
