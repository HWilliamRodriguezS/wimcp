package com.caribelabs.whereismycellphone.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
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
import android.os.AsyncTask;
import android.util.Log;

public class GenericServiceAsyncTask extends AsyncTask<ArrayList<BasicNameValuePair>, Void, Boolean>{
	
	String TAG = this.getClass().toString();
	
	int errorCode = 0;
	String errorMsg = "";
	Object response = null;
	String WSURL =  "http://www.nmtecnologies.com/wimcp/";
	
	String wsMethod = "";
	String httpMethod = "";
	WSResponseFormat wsrf = WSResponseFormat.JSON;
	Context context;
	RequestCallBack callBack = null;
	
	public GenericServiceAsyncTask(String serviceMethod ,String httpMethod , WSResponseFormat format , Context context , RequestCallBack callback ) {
		super();
		this.wsMethod = serviceMethod;
		this.httpMethod = httpMethod;
		this.wsrf = format;
		this.context = context;
		this.callBack = callback;

	}


	@Override
	protected Boolean doInBackground(ArrayList<BasicNameValuePair>... params) {
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(WSURL + wsMethod + ( WSResponseFormat.JSON.equals(wsrf) ? "format?json" : "" ));
	    boolean result = false;
	    
	    HttpResponse response = null;
	    InputStream inputStream = null;

	    try {
	    	
	    	switch(wsrf){
	 	    case JSON :
	 	    	
	 	    	
	 	    	break;
	 	    	
	 	    case XML :
	 	    default :
	 	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder builder;
		        InputSource is;
		        httppost.setEntity(new UrlEncodedFormEntity(params[0]));
				
		        // Execute HTTP Post Request
		        response = httpclient.execute(httppost);
		        inputStream = response.getEntity().getContent();
		        Log.i("URL",WSURL + wsMethod);
		        
		        //reading the xml
		        String responseString = convertInputStreamToString(inputStream);
	            builder = factory.newDocumentBuilder();
	            is = new InputSource(new StringReader(responseString));
	            Document xmldoc = builder.parse(is);
	            NodeList list = xmldoc.getElementsByTagName("error");
	            
	            if(list.getLength() > 0){
	            	Log.e(TAG, responseString);
	            	result = false;
	            	 
	            }else{
	            	Log.e(TAG, responseString);
	            	result = true;
	            }
		        this.response = xmldoc;
	 	    	break;
	 	    
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
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		this.callBack.onCompleted(result, response);
	}

	
	@Override
	protected void onProgressUpdate(Void... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
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
	
	
	public enum WSResponseFormat {
		
		XML , JSON;
		
	}
	
}
