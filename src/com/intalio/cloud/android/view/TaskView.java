package com.intalio.cloud.android.view;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TabHost;

import com.intalio.cloud.android.common.Constants;

/**
 * This class creates the activity when clicked on task.
 * It defines the view which contains the horizontal scroll tab
 * with agenda type of view which will show all the tasks and second
 * tab will be calendar view which will show tasks in calendar view. 
 */
public class TaskView extends TabActivity {
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		 super.onCreate(savedInstanceState);
		    setContentView(R.layout.task_view);

		    Resources res = getResources(); 
		    TabHost tabHost = getTabHost();  
		    tabHost.setBackgroundColor(Color.BLACK);
		    
		    Bundle extras = getIntent().getExtras();
		    String objectName = extras.getString(Constants.OBJECT);
		    
		    ActionBar actionBar = this.getActionBar();
		    actionBar.setIcon(R.drawable.blank); 
	    	actionBar.setDisplayUseLogoEnabled(false);
			actionBar.setTitle(objectName);
		    
		    // Create an Intent to launch an Activity for the tab (to be reused)
			Intent intent1 = new Intent();
			intent1.putExtra(Constants.OBJECT, objectName);
			intent1.setClass(this, ListRecords.class);
			Intent intent2 = new Intent().setClass(this, Calendar.class);
		   
	    	/*Calendar cal = Calendar.getInstance();              
	    	intent = new Intent(Intent.ACTION_EDIT);
	    	intent.setType("vnd.android.cursor.item/event");
	    	intent.putExtra("beginTime", cal.getTimeInMillis());
	    	intent.putExtra("allDay", true);
	    	intent.putExtra("rrule", "FREQ=YEARLY");
	    	intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
	    	intent.putExtra("title", "A Test Event from android app");
	    	startActivity(intent);*/

	    	//intent = new Intent(Intent.ACTION_VIEW).setDataAndType(null, CalendarActivity.MIME_TYPE);
	    	//startActivity(intent);  	    
			
			TabHost.TabSpec spec1 = tabHost.newTabSpec("agenda").setIndicator("",
                    res.getDrawable(R.drawable.info)).setContent(intent1);
	    	tabHost.addTab(spec1);
	    	TabHost.TabSpec spec2 = tabHost.newTabSpec("calendar").setIndicator("",
                    res.getDrawable(R.drawable.calendar_perspective)).setContent(intent2);
	    	tabHost.addTab(spec2);
	    	
	    	
	}

}