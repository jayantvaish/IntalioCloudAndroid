package com.intalio.cloud.android.view;


import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.model.Records;

/**
 * It defines all the details of any cloud's object record.
 *
 */
public class RecordDetails extends TabActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		    setContentView(R.layout.tab);

		    Resources res = getResources(); 
		    TabHost tabHost = getTabHost(); 
		    TabHost.TabSpec spec; 
		    Intent intent; 
		    
		    Bundle extras = getIntent().getExtras();
		    String objectName = extras.getString(Constants.OBJECT);
		    String recordName = extras.getString(Constants.RECORD);
		    String xid = extras.getString(Constants.XID);
		    
		    ActionBar actionBar = this.getActionBar();
		    actionBar.setIcon(R.drawable.blank); 
	    	actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setTitle(recordName);
		    String[] relatedObjectsToRecord = new Records().getRelatedObjects(objectName);
		    
		    // Create an Intent to launch an Activity for the tab (to be reused)
		    intent = new Intent();
		    intent.putExtra(Constants.OBJECT, objectName);
		    intent.putExtra(Constants.RECORD, recordName);
		    intent.putExtra(Constants.XID, xid);
		    intent.setClass(this, Info.class);
		    spec = tabHost.newTabSpec("info").setIndicator("",
                    res.getDrawable(getDrawableId("info"))).setContent(intent);
	    	tabHost.addTab(spec);
	    	
		    for(int index=0; index<relatedObjectsToRecord.length; index++){
		    	intent = new Intent();
			    intent.putExtra(Constants.OBJECT, objectName);
			    intent.putExtra(Constants.RECORD, recordName);
			    intent.putExtra(Constants.XID, xid);
			    intent.putExtra(Constants.RELATED_OBJECT, relatedObjectsToRecord[index]);
			    intent.setClass(this, ListRecords.class);
		    	
		    	spec = tabHost.newTabSpec(relatedObjectsToRecord[index]).setIndicator("",
	                      res.getDrawable(getDrawableId(relatedObjectsToRecord[index]))).setContent(intent);
		    	tabHost.addTab(spec);
		    }
		    //tabHost.setCurrentTab(0);

	}
	
	public int getDrawableId(String relatedObjectName){		
		if (relatedObjectName.equals("info")) {
			return R.drawable.info;
		} else if (relatedObjectName.equals(Constants.OBJECT_CONTACTS)) {
			return R.drawable.contacts;
		} else if (relatedObjectName.equals(Constants.OBJECT_CONTRACTS)) {
			return R.drawable.contracts;
		} else if (relatedObjectName.equals(Constants.OBJECT_INVOICES)) {
			return R.drawable.invoices;
		} else if (relatedObjectName.equals(Constants.OBJECT_OPPORTUNITIES)) {
			return R.drawable.opportunities;
		} else if (relatedObjectName.equals(Constants.OBJECT_QUOTES)){
			return R.drawable.quotes;
		}  else if (relatedObjectName.equals(Constants.OBJECT_PRICE_LISTS)){
			return R.drawable.price_lists;
		}
		return 0;
	}

}
