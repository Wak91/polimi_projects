package app.androbenchmark;


import java.lang.reflect.InvocationTargetException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

public class ExecuteBenchmarkTask extends AsyncTask <Object, Void, Long> {
	
	
	private AlertDialog loadingDialog;
	private Context context;
	


	//wrapper generale per gli asynctask cosi da non dover definire piu funzioni(una per benchmark)
	/*
	 * 
	 * SPIEGAZIONE
	 * 
	 * 1 - passo un istanza della classe di cui voglio chiamare il metodo, il nome del metodo da chiamare 
	 *     sottoforma di stringa, e gli eventuali parametri del merodo
	 *   
	 * 2 - ricavo la classe dall istanza passata
	 * 
	 * 3 - ricavo il metodo dal nome passato e anche gli argomenti che riceve a seconda di quelli passati(il tipo dei parametri e ricavato in automatico)
	 * 
	 * 4 - eseguo il metodo scelto e ne ritorno il valore
	 * 
	 * 
	 * TODO:RENDERE IL WRAPPER FLESSIBILE DAL PUNTO DI VISTA DEL NUMERO DEI PARAETRI IMMESSI
	 * 
	 * 
	 */
	
	
	public ExecuteBenchmarkTask(Context context){
		//setto il contesto giusto(passatto dalla UI)
		this.context = context;
	}
	
	
	@Override
	protected void onPreExecute(){
		//mostro il loading dialog nel contesto giusto
		loadingDialog = new AlertDialog.Builder(context).setTitle("Executing").setMessage("Wait please...").setIcon(android.R.drawable.ic_dialog_alert).show();
	}
	
	
	
	
	@Override
	protected Long doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			
			//ottimizzare
			//soluzione provvisoria perche conosciamo il massimo numero di argomenti da passare
			if(params.length == 2){
				//faccio il cast a long perche il tempo mi viene passatodirettamente dal benchmark			
				return (Long)params[0].getClass().getMethod((String)params[1]).invoke(params[0]);
				
			}
			else{
				//faccio il cast a long perche il tempo mi viene passatodirettamente dal benchmark										
				return (Long)params[0].getClass().getMethod((String)params[1], params[2].getClass()).invoke(params[0], params[2]);

			}
			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Long result) {	
		
		//tolgo il loading dialog
		loadingDialog.dismiss();
		
		//mostro il dialog di fine nel contesto giusto
		new AlertDialog.Builder(context)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished \n\nTime:" + result + "ms").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // continue with delete
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert).show();
		
    }


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}
	
	

	
	
	

}
