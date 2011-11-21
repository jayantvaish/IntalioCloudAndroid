package com.intalio.cloud.android.view;

import java.io.IOException;
import java.util.Iterator;

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
 *Defines activity for viewing general record details.
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
	
					TableRow tr = new TableRow(this);
					tr.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT));
	
					TextView tv = new TextView(this);
					tv.setText(key + "  ");
					tv.setTextColor(Color.BLACK);
					tv.setBackgroundColor(Color.parseColor(Constants.BACKGROUND_COLOR));
					tv.setGravity(Gravity.RIGHT);
					tv.setTextSize(16);
					tv.setTypeface(Typeface.DEFAULT_BOLD);
	
					TextView tv1 = new TextView(this);
					tv1.setSingleLine(false);
					tv1.setText("  " + value);
					tv1.setTextColor(Color.BLACK);
					tv1.setTextSize(16);
					tv1.setBackgroundColor(Color.parseColor(Constants.BACKGROUND_COLOR));
					tv1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.FILL_PARENT, 1f));
	
					tr.addView(tv);
					tr.addView(tv1);
	
					tr.setPadding(2, 2, 2, 2);
	
					/* Add row to TableLayout. */
					TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
							LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
					tl.addView(tr, layoutParams);
				}
			}
		}
	}
}