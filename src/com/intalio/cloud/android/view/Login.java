package com.intalio.cloud.android.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity will perform login.
 */
public class Login extends Activity {
	
	private final static String TAG = Login.class.getSimpleName();

	private EditText etUsername;
	private EditText etPassword;
	private Button btnLogin;
	//private TextView lblResult;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		etUsername = (EditText) findViewById(R.id.username);
		etPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.loginButton);
		//lblResult = (TextView)findViewById(R.id.result);
		final Intent intent = new Intent(this, ListCRMObjects.class);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();

				if (username.equals("intalio") && password.equals("intalio")) {
					//lblResult.setText("");
					startActivity(intent);
				} else {
					//lblResult.setText("Login Failed.");
					Log.e(TAG, "Login failed");
				}				
			}
		});

	}

} 