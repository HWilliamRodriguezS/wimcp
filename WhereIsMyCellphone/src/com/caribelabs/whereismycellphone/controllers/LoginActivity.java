package com.caribelabs.whereismycellphone.controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.caribelabs.whereismycellphone.R;

public class LoginActivity extends Activity implements OnClickListener{
	
	private String TAG = "LoginActivity";
	
	private EditText etUsername , etPassword ;
	private Button	bLogin , bRegister;
	private TextView tvLoginError ;
	
	private SharedPreferences  mPrefs ;
	Intent mainActivity ;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		etUsername = (EditText)findViewById(R.id.loginEmail);
		etPassword = (EditText)findViewById(R.id.loginPassword);
		bLogin = (Button)findViewById(R.id.btnLogin);
		bRegister =(Button)findViewById(R.id.btnLinkToRegisterScreen);
		tvLoginError = (TextView)findViewById(R.id.loginError);
		mPrefs = getPreferences(MODE_PRIVATE);
		
		bLogin.setOnClickListener(this);
		bRegister.setOnClickListener(this);
		 
		String user_id = mPrefs.getString("user_id", "");
		String user_username = mPrefs.getString("user_username", "");
		String user_email = mPrefs.getString("user_email", "");
		String user_phonenumber = mPrefs.getString("user_phonenumber", "");
		String user_password= mPrefs.getString("user_password", "");
		
		mainActivity = new Intent(getApplicationContext(), MainActivity.class);
		
		if( !user_id.equals("") || user_id.length() > 0 ){
			Log.i(TAG, "User_id : " +  user_id +  "" + 
					   "User_username : " + user_username + "" +
					   "User_email : " + user_email + "" +
					   "User_phonenumber : " + user_phonenumber + "" +
					   "User_username : " + user_password + "" +
					   "");
			 mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
             startActivity(mainActivity);
		}
		
		 
		
	}
	
	
	private void validateForm(){
		Log.i(TAG,"I'm validating the form");
	}
	
	private void login(){
		
	}
	
	private void register(){
		Log.i(TAG,"I'm going to call the Register Activity... ");
	}


	@Override
	public void onClick(View v) {
		Log.i(TAG,"GOT CLICK");
		switch(v.getId()){
			case R.id.btnLogin:
				validateForm();
				new LoginOpertation().execute();
				break;
			
			case R.id.btnLinkToRegisterScreen:
				register();
				break;
				
		}
		
	}
	
	
	private class LoginOpertation extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://www.nmtecnologies.com/wimcp/users/login");
		    
		    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder;
	        InputSource is;
	        String result = null;

		    try {

		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("username", "" + etUsername.getText()));
		        nameValuePairs.add(new BasicNameValuePair("password", "" + etPassword.getText()));
		        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		        
		        
				InputStream inputStream = null;
		        // Execute HTTP Post Request
		        HttpResponse response = httpclient.execute(httppost);
		        inputStream = response.getEntity().getContent();
		        
		        //reading the xml
		        String responseString = convertInputStreamToString(inputStream);
	            builder = factory.newDocumentBuilder();
	            is = new InputSource(new StringReader(responseString));
	            Document doc = builder.parse(is);
	            NodeList list = doc.getElementsByTagName("item");

	            if(list.getLength() > 0){
	            	 result = list.item(0).getTextContent();
	            	 Log.e(TAG, "Got Logged : " + list.item(0).getChildNodes()) ;
	            	 //registration= true;
	            	 
	            	 Editor prefsEditor = mPrefs.edit();

	            	 Log.i(TAG,result);
	            	 NodeList nodeList = list.item(0).getChildNodes();
	            	 for(int i = 0; i < nodeList.getLength() ; i++ ){
	            		 Node node = nodeList.item(i);
	            		 Log.i(TAG, node.getNodeName() + " : " + node.getTextContent()); 
	            		 prefsEditor.putString("user_" +node.getNodeName()  , node.getTextContent());   
	            	 }
	            	 prefsEditor.commit();
	            	 result = "success";
	            	 
	            }else{
	            	Log.e(TAG, responseString);
	            	list = doc.getElementsByTagName("error");
	            	if(list.getLength() > 0){
	            		//registration = false;
	            		//tvLoginError.setText("Invalid User or Password");
	            		result = list.item(0).getTextContent();
	            		//list.item(0).getNodeValue();
	            	   
	            	}
	            	Log.i(TAG,result);
	            	result = "error";
	            }

	            
		        
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		    } catch (ParserConfigurationException e) {
		    	
		    } catch (SAXException e) {
		    	
		    } 
			
			return result;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if(result.equals("success")){
				 
                 // Close all views before launching Dashboard
            	 mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 startActivity(mainActivity);
			}else{
				tvLoginError.setText("Invalid User or Password");
			}
			
			
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}
		
	}
	
	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
	
}
