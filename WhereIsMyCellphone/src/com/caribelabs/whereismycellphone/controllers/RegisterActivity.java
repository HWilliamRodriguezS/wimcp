package com.caribelabs.whereismycellphone.controllers;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.caribelabs.whereismycellphone.R;
import com.caribelabs.whereismycellphone.utils.GenericServiceAsyncTask;
import com.caribelabs.whereismycellphone.utils.RequestCallBack;
import com.caribelabs.whereismycellphone.utils.GenericServiceAsyncTask.WSResponseFormat;

public class RegisterActivity extends Activity implements OnClickListener{
	private String TAG = "LoginActivity";
	
	private Button btnRegister;
	private Button btnToLogin;
	
	
	private EditText etRegisterName;
	private EditText etRegisterEmail;
	private EditText etRegisterPassword;
	private EditText etRepeatRegisterPassword;
	private TextView tvRegisterError;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
		
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
		etRegisterName = (EditText) findViewById(R.id.registerName);
		etRegisterEmail = (EditText) findViewById(R.id.registerEmail);
		etRegisterPassword = (EditText) findViewById(R.id.registerPassword);
		etRepeatRegisterPassword = (EditText) findViewById(R.id.repeatRegisterPassword);
		tvRegisterError = (TextView) findViewById(R.id.registerError);

		btnRegister.setOnClickListener(this);
		btnToLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Log.i(TAG,"GOT CLICK");
		switch(v.getId()){
		case R.id.btnRegister :
			Log.i("RegisterActivity","Now I suppuse to register You :P");
			
			String fullName = etRegisterName.getText().toString();
			String email = etRegisterEmail.getText().toString();
		    String password = etRegisterPassword.getText().toString();
		    String rePassword = etRepeatRegisterPassword.getText().toString();
			
		    Log.i("Values : " , "Fullname : " + fullName + ", email : " + email + ", password : " + password + " rePassword : " + rePassword);
		    
		    if (fullName.matches("")) {
		        tvRegisterError.setText("Full Name is Required.");
		        return;
		    }
		    
		    if (email.matches("")) {
		        tvRegisterError.setText("Email is Required.");
		        return;
		    }
		    
		    
		    if(!isEmailValid(email)){
		    	tvRegisterError.setText("Email is invalid.");
		        return;
		    }
		    
		    
		    if (password.matches("")) {
		        tvRegisterError.setText("Password is Required.");
		        return;
		    }

		    if(!(password.equals(rePassword)) ){
		    	 tvRegisterError.setText("The Written passwords doesn't match.");
			     return;
		    }
		    
		    
		    tvRegisterError.setText("");
		    //Toast.makeText(getApplicationContext() , "The form is valid" ,Toast.LENGTH_LONG ).show();
			ArrayList<BasicNameValuePair> params = new  ArrayList<BasicNameValuePair>(3);
			params.add(new BasicNameValuePair("username", "" + fullName));
			params.add(new BasicNameValuePair("email", "" + email));
			params.add(new BasicNameValuePair("password", "" + password));
			//nameValuePairs.add(new BasicNameValuePair("username", "" + etUsername.getText()));
			new GenericServiceAsyncTask("users/sign_up", "POST",
					WSResponseFormat.XML, getApplicationContext(),
					new RequestCallBack() {

						@Override
						public void onCompleted(boolean result, Object response) {
							Document xmldoc = (Document)response;
							Log.i("RequestCallBack","Result : " + result + " WS RESPONSE : " + xmldoc);
							
							if(result){
								Toast.makeText(getApplicationContext(), "Registration Succesffully", Toast.LENGTH_SHORT).show();
								tvRegisterError.setText("");
								
								new Handler().postDelayed(new Runnable() {
						            @Override
						            public void run() {
						            	onBackPressed();
						            }
						        }, 5000);
								
							}else{
								
								NodeList list = ((Document) xmldoc).getElementsByTagName("error");
								 if(list.getLength() > 0){
									 
								 }
								tvRegisterError.setText("Unable to register");
							}
							
							
						}
						
					}).execute(params);
			break;
		
		case R.id.btnLinkToLoginScreen :
			Log.i("RegisterActivity","Now I'm getting back to the login activity");
			onBackPressed();
			break;
		}
		
		
		
		
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	
	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	
}
