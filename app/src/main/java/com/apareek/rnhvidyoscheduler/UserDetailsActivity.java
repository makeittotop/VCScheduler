package com.apareek.rnhvidyoscheduler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class UserDetailsActivity extends Activity implements OnClickListener{
	private EditText firstNameEditText;
	private EditText lastNameEditText;
	private EditText userIdEditText;
	private Spinner departmentSpinner;
	private Button skipButton;
	private Button userSubmitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.userdetails);
		
		firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
		lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
		userIdEditText = (EditText) findViewById(R.id.userIDEditText);
		
		departmentSpinner = (Spinner) findViewById(R.id.departmentSpinner);
		
		skipButton = (Button) findViewById(R.id.skipButton);
		userSubmitButton = (Button) findViewById(R.id.userSubmitButton);
		
		skipButton.setOnClickListener(this);
		userSubmitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.skipButton:
			setResult(RESULT_CANCELED);
			break;
		case R.id.userSubmitButton:
			String strFName = firstNameEditText.getText().toString();
			String strLName = lastNameEditText.getText().toString();
			int intUserId = Integer.parseInt(userIdEditText.getText().toString());
			String strDepartment = "Technology";
			
			Intent i = new Intent();
			i.putExtra("fname", strFName);
			i.putExtra("lname", strLName);
			i.putExtra("userid", intUserId);
			i.putExtra("department", strDepartment);
			
			setResult(RESULT_OK, i);
			break;
		}
		finish();
	}
}
