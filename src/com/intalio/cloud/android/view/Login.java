package com.intalio.cloud.android.view;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * 
 *This activity will perform login.
 */
public class Login  extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  //ActionBar actionBar = getActionBar();
	  //actionBar.setIcon(R.drawable.intalio);
	  setContentView(R.layout.main);	  
	  final Button button = (Button) findViewById(R.id.loginButton);
	  final Intent intent = new Intent(this, ListCRMObjects.class);
	  button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
        	  startActivity(intent);
          }
      });
	  
	}
	
	
}