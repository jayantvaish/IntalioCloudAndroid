package com.intalio.cloud.android.view;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView;

import com.intalio.cloud.android.adapter.CustomAdapter;
import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.widget.LoadingListView;

/** 
 * This class creates activity to show the list of cloud object records
 * These records are loaded in for of list, but it's limit is defined,
 * when the user will scroll down list will again start loading if it will
 * contains the data.
 * 
 * It also supports the search filter, where contents can be filter.
 *
 */
public class ListRecords extends Activity implements SearchView.OnQueryTextListener, OnKeyListener {
	
	private SearchView recordSearchView;
    private LoadingListView recordListView;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.searchview_filter);        
        recordSearchView = (SearchView) findViewById(R.id.search_view);        
        recordSearchView.setVisibility(View.GONE);
        
        ActionBar actionBar = this.getActionBar();        
    	
        recordListView = (LoadingListView) findViewById(R.id.list_view);
        recordListView.setLoadingView(getLayoutInflater().inflate(R.layout.loading_view, null));
		Bundle extras = getIntent().getExtras();
        String selectedDate = extras.getString(Constants.TASK_SELECTED_DATE);
        String relatedObject = extras.getString(Constants.RELATED_OBJECT);
        String objectName = extras.getString(Constants.OBJECT);
        
        recordListView.setTextFilterEnabled(true);
        
        if(selectedDate != null){   
        	actionBar.setIcon(R.drawable.blank); 
        	actionBar.setDisplayUseLogoEnabled(false);
        	actionBar.setTitle(selectedDate);
        	setUpAdapterAndActivity(objectName);        	
        } else if (relatedObject != null) {			
			Log.d("ListRecords", "Object name is: " + objectName);
			String xid = extras.getString(Constants.XID);
			customAdapter = new CustomAdapter(this, R.layout.list_item, relatedObject, objectName, xid);
        	recordListView.setAdapter(customAdapter);
		} else if (objectName != null) {			
			Log.d("ListRecords", "Object name is: " + objectName);
			if(!objectName.equals(Constants.OBJECT_TASKS)){
				actionBar.setIcon(R.drawable.blank); 
		    	actionBar.setDisplayUseLogoEnabled(false);
				actionBar.setTitle(objectName);
			}	
			setUpAdapterAndActivity(objectName);			
		}          
        customAdapter.notifyMayHaveMorePages();
		recordListView.setOnKeyListener(this);
    }
    
    private void setUpAdapterAndActivity(final String objectName) {
    	customAdapter = new CustomAdapter(this, R.layout.list_item, objectName);
    	recordListView.setAdapter(customAdapter);	        
        recordListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {				
				Intent recordDetailsIntent = new Intent(view.getContext(),	RecordDetails.class);
				String[] recordNameWithXid = customAdapter.getItem(position).split(Constants.DELIMITER);
				String xid = recordNameWithXid[0];
				String recordName = recordNameWithXid[1];
				recordDetailsIntent.putExtra(Constants.OBJECT, objectName);
				recordDetailsIntent.putExtra(Constants.RECORD, recordName);
				recordDetailsIntent.putExtra(Constants.XID, xid);
				startActivityForResult(recordDetailsIntent, 0);
			}
		});
		
	}

	/**
     * Defines the property of the search view dialog.
     */
    private void setupSearchView() {
    	recordSearchView.setIconifiedByDefault(false);
        recordSearchView.setOnQueryTextListener(this);
        recordSearchView.setSubmitButtonEnabled(false);
    }
    
    
    /**
     * Defines the list view when text changes on 
     * search view dialog.
     */
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            recordListView.clearTextFilter();
        } else {
            recordListView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }   
    

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if(KeyEvent.KEYCODE_SEARCH == keyCode){
			recordSearchView.setVisibility(View.VISIBLE);
			setupSearchView();
		}
		return false;
	}
	
	
}