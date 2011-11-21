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
 * Defines loading type list view with the functionality of search filters.
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
        final String objectName = extras.getString(Constants.OBJECT);
        
        if(selectedDate != null){   
        	//TODO: change following logic for tasks.
        	actionBar.setIcon(R.drawable.blank); 
        	actionBar.setDisplayUseLogoEnabled(false);
        	actionBar.setTitle(selectedDate);
        	customAdapter = new CustomAdapter(this, R.layout.list_item, objectName);
        	recordListView.setAdapter(customAdapter);       					
	        recordListView.setTextFilterEnabled(true);	        
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
        	
        } else if (relatedObject != null) {			
			Log.d("ListRecords", "Object name is: " + objectName);
			String xid = extras.getString(Constants.XID);
			customAdapter = new CustomAdapter(this, R.layout.list_item, relatedObject, objectName, xid);
        	recordListView.setAdapter(customAdapter);
	        recordListView.setTextFilterEnabled(true);
		} else if (objectName != null) {			
			Log.d("ListRecords", "Object name is: " + objectName);
			if(!objectName.equals(Constants.OBJECT_TASKS)){
				actionBar.setIcon(R.drawable.blank); 
		    	actionBar.setDisplayUseLogoEnabled(false);
				actionBar.setTitle(objectName);
			}	
			customAdapter = new CustomAdapter(this, R.layout.list_item, objectName);
        	recordListView.setAdapter(customAdapter);
	        recordListView.setTextFilterEnabled(true);
	        
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
        
        customAdapter.notifyMayHaveMorePages();
		recordListView.setOnKeyListener(this);	
		/*recordSearchView.setOnCloseListener(new OnCloseListener() {
		      @Override
		      public boolean onClose() {
		    	recordSearchView.setVisibility(View.GONE);
		        return false;
		      }
		    });*/
    }
    
    private void setupSearchView() {
    	recordSearchView.setIconifiedByDefault(false);
        recordSearchView.setOnQueryTextListener(this);
        recordSearchView.setSubmitButtonEnabled(false);
    }

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