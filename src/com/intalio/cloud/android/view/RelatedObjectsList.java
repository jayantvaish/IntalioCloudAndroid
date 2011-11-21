package com.intalio.cloud.android.view;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.model.Records;

public class RelatedObjectsList extends Activity implements SearchView.OnQueryTextListener, OnKeyListener   {
	
	private SearchView recordSearchView;
	private ListView recordListView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.searchview_filter);        
        recordSearchView = (SearchView) findViewById(R.id.search_view);        
        recordSearchView.setVisibility(View.GONE);
        
        ActionBar actionBar = this.getActionBar();
        recordListView = (ListView) findViewById(R.id.list_view);
		//actionBar.setIcon(R.drawable.blank);
        Bundle extras = getIntent().getExtras();
        String relatedObject = extras.getString(Constants.RELATED_OBJECT);
        String objectName = extras.getString(Constants.OBJECT);
		String xid = extras.getString(Constants.XID);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item,
				R.id.itemName);
		String[] relatedObjectEntries = getRecords(relatedObject, objectName, xid, 5);
		for(int index = 0; index < relatedObjectEntries.length; index++){
			adapter.add(relatedObjectEntries[index]);
		}
		
        if (extras.getString("objectName") != null) {
			recordListView.setAdapter(adapter);
	        recordListView.setTextFilterEnabled(true);
		}  
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
    
    
    private String[] getRecords(String objectName, String primaryObjectName,
			String xid, int page) {
		List<String> relatedObjectList = null;
		try {
			relatedObjectList = new Records().getRecordNames(objectName, primaryObjectName, xid, page);
		} catch (JSONException e) {			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(relatedObjectList != null){
			String[] relatedObjects = new String[relatedObjectList.size()];
			for(int index = 0; index < relatedObjectList.size(); index++){
				relatedObjects[index] = relatedObjectList.get(index);
				Log.d("RelatedObjectsList at index " + index + " is: ", relatedObjects[index]);
			}		
			return relatedObjects;
		}
		return new String[]{};
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
