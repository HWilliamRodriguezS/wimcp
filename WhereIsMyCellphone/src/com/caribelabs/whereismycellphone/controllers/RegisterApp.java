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
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.caribelabs.whereismycellphone.entities.Device;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class RegisterApp extends AsyncTask<Void, Void, String> {

	private static final String TAG = "GCM Registration";
	Context ctx;
	GoogleCloudMessaging gcm;
	String SENDER_ID = "83246781410";
	String regid = null;
	private int appVersion;
	private Device device;
	private boolean registration;

	public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion) {
		this.ctx = ctx;
		this.gcm = gcm;
		this.appVersion = appVersion;
		this.registration = false;
		this.device = new Device();
		
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	

	@Override
	protected String doInBackground(Void... arg0) {
		String msg = "";
		try {
			if (gcm == null) {
				gcm = GoogleCloudMessaging.getInstance(ctx);
			}
			regid = gcm.register(SENDER_ID);
			msg = "Device registered, registration ID=" + regid;
			device.setRegistration_id(regid);
			// You should send the registration ID to your server over HTTP,
			// so it can use GCM/HTTP or CCS to send messages to your app.
			// The request to your server should be authenticated if your app
			// is using accounts.
			sendRegistrationIdToBackend();

			// For this demo: we don't need to send it because the device
			// will send upstream messages to a server that echo back the
			// message using the 'from' address in the message.

			// Persist the regID - no need to register again.
			storeRegistrationId(ctx, regid);
		} catch (IOException ex) {
			msg = "Error :" + ex.getMessage();
			// If there is an error, don't just keep trying to register.
			// Require the user to click a button again, or perform
			// exponential back-off.
		}
		return msg;
	}

	private void storeRegistrationId(Context ctx, String regid) {
		final SharedPreferences prefs = ctx.getSharedPreferences(
				MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
		Log.i(TAG, "Saving regId on app version " + appVersion);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("registration_id", regid);
		editor.putInt("appVersion", appVersion);
		editor.commit();
		
		this.device.setRegistration_id(regid);

	}

	private void sendRegistrationIdToBackend() {
		
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost("http://www.nmtecnologies.com/wimcp/devices/registrer");
	    
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        InputSource is;

	    try {
	        // Add your data
	    	
	    	//log.d("",);
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
	        nameValuePairs.add(new BasicNameValuePair("registration_id", "" + regid));
	        nameValuePairs.add(new BasicNameValuePair("android_id", "" +regid));
	        nameValuePairs.add(new BasicNameValuePair("user_id", "1"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	        
	        String result = null;
			InputStream inputStream = null;
	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        inputStream = response.getEntity().getContent();
	       
	        
	        //reading the xml
            builder = factory.newDocumentBuilder();
            is = new InputSource(new StringReader(convertInputStreamToString(inputStream)));
            Document doc = builder.parse(is);
            NodeList list = doc.getElementsByTagName("device_id");
            
            if(list.getLength() > 0){
            	 result = list.item(0).getTextContent();
            	 registration= true;
            }else{
            	list = doc.getElementsByTagName("error");
            	if(list.getLength() > 0){
            		registration = false;
            		result = "Did not work!";
            	}
            }
            /*
			// convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
			*/
            Log.i(TAG,result);
	        
	    } catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    } catch (ParserConfigurationException e) {
	    	
	    } catch (SAXException e) {
	    	
	    } 
				
		/* GET REQUEST
		 URI url = null;
		try {
			url = new URI(
					"http://www.nmtecnologies.com/wimcp/device/registerDevice?regId="
							+ regid);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet request = new HttpGet();
		request.setURI(url);
		
		HttpPost httpPost = new HttpPost();
 
		 
		try {
			String result = null;
			InputStream inputStream = null;
			HttpResponse httpResponse = httpclient.execute(request);
			inputStream = httpResponse.getEntity().getContent();
			
			// convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
			
            Log.i(TAG,result);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	 // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
 
        inputStream.close();
        return result;
 
    }
    
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if(registration){
			Toast.makeText(ctx,
					"Registration Completed. Now you can see the notifications",
					Toast.LENGTH_SHORT).show();
			Log.v(TAG, result);
		}else{
			Toast.makeText(ctx,
					"There was a problem trying to register your device.",
					Toast.LENGTH_SHORT).show();
			
		}
		Log.v(TAG, result);
	}
	
	
	
}