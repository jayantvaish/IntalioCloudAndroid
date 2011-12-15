package com.intalio.cloud.android.view;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.model.Records;

/**
 *
 *Defines activity for viewing general record details,
 *which are hold into table layout in the forms of rows.
 *
 *It gets the data from the json objects and the iterated
 *with keys which are shown in rows with their values. 
 */
public class Info extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.info);

		TableLayout tl = (TableLayout) findViewById(R.id.tableLayoutForInfo);
		Records records = new Records();

		JSONObject recordsInJson = null;
		Bundle extras = getIntent().getExtras();
		String objectName = extras.getString(Constants.OBJECT);
		String recordName = extras.getString(Constants.RECORD);
		String xid = extras.getString(Constants.XID);

		try {
			recordsInJson = records.getRecordInJson(objectName, xid);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		if (recordsInJson != null) {

			Iterator<String> iterator = recordsInJson.keys();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				if(!key.equals("XID")){  //TODO: Remove if condition and hard-coding.
					String value = null;
					try {
						value = (String) recordsInJson.get(key);
						Log.d("Info", "Value is: " + value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
	
					TableRow row = new TableRow(this);
					row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT));
	
					TextView textViewForKey = new TextView(this);
					textViewForKey.setText(key + "  ");
					textViewForKey.setTextColor(Color.BLACK);
					textViewForKey.setTextSize(16);
					textViewForKey.setBackgroundColor(Color.parseColor(Constants.BACKGROUND_COLOR));
					textViewForKey.setGravity(Gravity.RIGHT);
					textViewForKey.setTypeface(Typeface.DEFAULT_BOLD);
					
	
					TextView textViewForValue = new TextView(this);
					textViewForValue.setText("  " + value);
					textViewForValue.setTextColor(Color.BLACK);
					textViewForValue.setTextSize(16);
					textViewForValue.setBackgroundColor(Color.parseColor(Constants.BACKGROUND_COLOR));
					textViewForValue.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT, 1f));
	
					row.addView(textViewForKey);
					row.addView(textViewForValue);
	
					row.setPadding(2, 2, 2, 2);
	
					/* Add row to TableLayout. */
					TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					tl.addView(row, layoutParams);
				}
			}
		}
	}
}