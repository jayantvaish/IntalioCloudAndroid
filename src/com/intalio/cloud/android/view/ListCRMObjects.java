package com.intalio.cloud.android.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;

import com.intalio.cloud.android.adapter.CustomExpandableListAdapter;
import com.intalio.cloud.android.common.Constants;
import com.intalio.cloud.android.model.CloudObjects;

/**
 * It lists all the CRM cloud objects in groupwise.
 *
 */
public class ListCRMObjects extends ExpandableListActivity {
	
    private static final String LOG_TAG = ListCRMObjects.class.getSimpleName();
    
    private List<ArrayList<HashMap<String, String>>> childList;
   
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_list);
		SimpleExpandableListAdapter expListAdapter =
			new CustomExpandableListAdapter(
				this,
				createGroupList(),	
				R.layout.group_row,	
				new String[] { Constants.OBJECT_GROUP },	
				new int[] { R.id.groupname },		
				createChildList(),	
				R.layout.child_row,	
				new String[] { Constants.OBJECT },	
				new int[] { R.id.childname }	
			);
		setListAdapter( expListAdapter );
		ExpandableListView expList = getExpandableListView();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		expList.setIndicatorBounds(width - 40, width-10);
    }

    public void  onContentChanged  () {
        super.onContentChanged();
    }

    public boolean onChildClick(
            ExpandableListView parent, 
            View v, 
            int groupPosition,
            int childPosition,
            long id) {            
        
    	String objectName = childList.get(groupPosition).get(childPosition).get(Constants.OBJECT);
    	Log.d( LOG_TAG, "onChildClick:: childPosition: " + childPosition + 
        		" groupPosition: " + groupPosition + " objectName: " + objectName);    
        if(objectName.equals(Constants.OBJECT_TASKS)){
        	Intent recordIntent = new Intent(parent.getContext(),	TaskView.class);
    		recordIntent.putExtra(Constants.OBJECT, objectName);
    		startActivityForResult(recordIntent, 0);
        } else {
        	Intent recordIntent = new Intent(parent.getContext(),	ListRecords.class);
    		recordIntent.putExtra(Constants.OBJECT, objectName);
    		startActivityForResult(recordIntent, 0);
        }		
        return true;
    }

    public void  onGroupExpand  (int groupPosition) {
        Log.d( LOG_TAG,"onGroupExpand: "+groupPosition );
    }


	private List createGroupList() {
	  String[] cloudObjectGroups = new CloudObjects().getCloudObjectGroups();
	  ArrayList result = new ArrayList();
	  for( int i = 0 ; i < cloudObjectGroups.length ; ++i ) {
		HashMap m = new HashMap();
	    m.put( Constants.OBJECT_GROUP,cloudObjectGroups[i] );
		result.add( m );
	  }
	  return result;
    }


  private List createChildList() {
	String[][] cloudObjects = new CloudObjects().getCloudObjects();
	childList = new ArrayList<ArrayList<HashMap<String, String>>>();
	for( int i = 0 ; i < cloudObjects.length ; ++i ) {
		// Second-level lists
	  ArrayList<HashMap<String, String>> secList = new ArrayList<HashMap<String, String>>();
	  for( int n = 0 ; n < cloudObjects[i].length ; n++ ) {
	    HashMap<String, String> child = new HashMap<String, String>();
		child.put( Constants.OBJECT, cloudObjects[i][n] );
		secList.add( child );
	  }
	  childList.add( secList );
	}
	return childList;
  }
  
  

}

