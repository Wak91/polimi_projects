package app.androbenchmark;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import app.androbenchmark.util.HTTPUtil;

public class SendResultTask extends AsyncTask <Void, Void, Boolean>  {
	

	private GraphActivity activity;
	private Context context;
	private AlertDialog sendingDialog;
	private String manuf;
	private String model;
	private int tent;
	
	public SendResultTask(GraphActivity context){
		this.context = context;
		this.activity = context;
		this.manuf = Build.MANUFACTURER; //Retreive the model and manuf. of device 
		this.model = Build.MODEL;
		this.tent=0;
	}
	
	@Override
	protected void onPreExecute(){
		//mostriamo il messaggio di testing
		this.sendingDialog = new AlertDialog.Builder(context).setTitle("Sending").setMessage("Wait please...sending result to server [tent " + tent + " ]").setIcon(android.R.drawable.ic_dialog_alert).show();
				
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		
		 List<NameValuePair> params = new ArrayList<NameValuePair>();
			
		 for(Entry<String, List<Integer>> e : this.activity.getResult().entrySet()) {
			        String key = e.getKey();
			        List<Integer> value = e.getValue();
			        if(key.equals("type")){
			        	switch (value.get(0)) {
						case 0:
							params.add(new BasicNameValuePair(key, "gray"));
							break;
							
						case 1:
							params.add(new BasicNameValuePair(key, "brute"));
							break;
							
						case 2:
							params.add(new BasicNameValuePair(key, "matrix"));
							break;
						default:
							break;
						}
			        }
			        else{
			        	 params.add(new BasicNameValuePair(key, value.toString()));
			        }
			        
	     }
		 
		 params.add(new BasicNameValuePair("vendor", this.manuf));
		 params.add(new BasicNameValuePair("model", this.model));
		
		try {
			HTTPUtil.sendRequestOverHTTP("http://37.187.225.187:3000/insert", params, HTTPUtil.RequestMethod.POST );
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
		
		return true;

	}
	
	@Override
	protected void onPostExecute(Boolean result) {	
		
		if (result) {
			this.sendingDialog.dismiss();
		} else {
			this.sendingDialog.dismiss();
			this.sendingDialog = new AlertDialog.Builder(context).setTitle("Error").setMessage("There was an error during sending, we make another try!").setIcon(android.R.drawable.ic_dialog_alert).show();
		    this.tent++;
		    if(tent<3)
		      this.doInBackground();
		    else
				this.sendingDialog = new AlertDialog.Builder(context).setTitle("Error").setMessage("Something went wrong with connection...aborting").setIcon(android.R.drawable.ic_dialog_alert).show();

		}
		
					
    }
	
	
}
