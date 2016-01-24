package com.apareek.rnhvidyoscheduler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends Activity implements OnClickListener {
	private EditText userNameEditText;
	private EditText passwordEditText;
	private Button signInButton;
	
	private Drawable userNameEditTextDrawable;
	private Drawable passwordEditTextDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.signin);
		
		userNameEditText = (EditText) findViewById(R.id.usernameEditText);
		passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);
		
		userNameEditTextDrawable = userNameEditText.getBackground();
		passwordEditTextDrawable = passwordEditText.getBackground();
		
		signInButton = (Button) this.findViewById(R.id.signInButton);
		signInButton.setOnClickListener(this);
/*		
		userNameEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				userNameEditText.setBackground(userNameEditTextDrawable);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		passwordEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setBackground(passwordEditTextDrawable);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
			case R.id.signInButton:
				String strUserName = userNameEditText.getText().toString();
				String strPassword = passwordEditText.getText().toString();
				
				boolean brk = false;
				if(strUserName.length() == 0) {
					Toast.makeText(getApplicationContext(),
						"Username can't be empty" , Toast.LENGTH_LONG)
						.show();
					brk = true;
				}
				
				if(strPassword.length() == 0) {
					Toast.makeText(getApplicationContext(),
						"Password can't be empty" , Toast.LENGTH_LONG)
						.show();
					brk = true;
				}				
				
				if (brk) {
					break;
				}
				
				Intent i = new Intent();
				i.putExtra("username", strUserName);
				i.putExtra("password", strPassword);
				setResult(RESULT_OK, i);
				finish();
		}
	}
}
