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
import android.util.Log;

public class CheckConnectionTask extends AsyncTask <Void, Void, Boolean>  {
	

	private GraphActivity activity;
	private Context context;
	private AlertDialog sendingDialog;
	
	public CheckConnectionTask(GraphActivity context){
		this.context = context;
		this.activity = context;
		
	}
	
	@Override
	protected void onPreExecute(){
		//mostriamo il messaggio di testing
		this.sendingDialog = new AlertDialog.Builder(context).setTitle("Testing Connection").setMessage("Wait please...").setIcon(android.R.drawable.ic_dialog_alert).show();
				
	}

	@Override
	protected Boolean doInBackground(Void... arg0) {
		//testiamo la connessione
		return this.checkConnessioneDisponibile();

	}
	
	@Override
	protected void onPostExecute(Boolean result) {	
		
		this.sendingDialog.dismiss();
		//se non ho connessione mostro un dialog di errore
		if(!result){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(activity);

		    builder.setMessage("You have no internet connection");
		    builder.setPositiveButton("OK", null);
		    builder.show();
			//Log.w("CONNECTIONTASK", "Nessuna connessione");
		}
		//altrimenti faccio la post al server (da implementare ancora)
		else{
					
			Log.w("CONNECTIONTASK", "Connessione disponibile");
			
			try {
				this.activity.postResult();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
					
    }
	
	/**
	 * controlliamo che ci sia una connessione dati attiva(questo controlla solo se e attiva e non se e realmente funzionante)
	 * usiamo la funzione checkConnessioneDisponibile() per testare il funzionamento effettivo
	 * @return
	 */
	private boolean checkInterfacciaReteCollegata() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) this.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	/**
	 * trucco per testare connessione
	 * pinghiamo un sito sicuramente up (google.com), se ci risponde allora la connessione e funzionante
	 * @return
	 */
	private boolean checkConnessioneDisponibile() {
		//prima controllo se ho una connessione disonibile
	    if (checkInterfacciaReteCollegata()) {
	        try {
	        	//contattiamo google
	            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	            urlc.setRequestProperty("User-Agent", "AndroBenchmark");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            //controlliamo che la risposta sia positiva
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            Log.w("CONNECTIONTASK", "Error checking internet connection");
	        }
	    } else {
	        Log.w("CONNECTIONTASK", "No network available!");
	    }
	    return false;
	}

}
