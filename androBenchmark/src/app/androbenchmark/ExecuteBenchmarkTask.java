package app.androbenchmark;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

public class ExecuteBenchmarkTask extends AsyncTask <Void, Void, HashMap<String, List> > {
	
	
	private AlertDialog loadingDialog;
	private Context context;
	private int selected;

	
	private final int num_of_test=3;
	private List result_j;
	private List result_jni;
	private List result_rs;
	private ArrayList names; // this contain the name of all the images
    private int[] matrix_dimension;
    private ArrayList words;
	


	//wrapper generale per gli asynctask cosi da non dover definire piu funzioni(una per benchmark)
	/*
	 * 
	 * SPIEGAZIONE
	 * 
	 * 1 - passo un istanza della classe di cui voglio chiamare il metodo, il nome del metodo da chiamare 
	 *     sottoforma di stringa, e gli eventuali parametri del metodo
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
	
	
	public ExecuteBenchmarkTask(Context context, int selected){
		//setto il contesto giusto(passatto dalla UI)
		this.context = context;
		this.selected = selected;
		
	}
	
	
	/**
	 * settiamo tutti i componenti utili alla suite di benchmark
	 */
	@Override
	protected void onPreExecute(){
		//mostro il loading dialog nel contesto giusto
		Log.w("ANDROBENCHMARK", "mostro");
		this.loadingDialog = new AlertDialog.Builder(context).setTitle("Executing").setMessage("Wait please...").setIcon(android.R.drawable.ic_dialog_alert).show();
		
		result_j = new ArrayList();
		result_jni = new ArrayList();
		result_rs = new ArrayList();

		//Initialization of image's name 
		names = new ArrayList();
		names.add("image0.bmp");
		names.add("image1.bmp");
		names.add("image2.bmp");
		
		//Inizialization of matrix dimension
		matrix_dimension = new int[3];	
		matrix_dimension[0]=300;
		matrix_dimension[1]=400;
		matrix_dimension[2]=500;
		
		//Inizialization of words to crack 
		words = new ArrayList();
		words.add("ciao");
		words.add("ciaoo");
		words.add("ciaooo");
	}
	
	
	
	/**
	 * a seconda della scelta eseguiamo il benchmark giusto all'interno di un asynctask
	 */
	@Override
	protected HashMap doInBackground(Void... params) {
		
		 
		 if(selected == R.id.radio0)
		   {
			  
		      for(int i=0;i<names.size();i++)
		      {    
				Bitmap bm = null;
				Bitmap bm2 = null;
				try {
					bm = BitmapFactory.decodeStream(this.context.getAssets().open(""+names.get(i)));
					bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				
				Long t = GrayScaling.callPureJava(bm2);
				result_j.add(t); 
				
				t = GrayScaling.callPureJni(bm); 
				result_jni.add(t); 
				
			    t = GrayScaling.callPureRenderScript(bm, this.context);			     
		     	result_rs.add(t);
		     	
		     	
		     }
			  		    		     	    
		    
		   }
		 /*
		 else 
			 if(selected == R.id.radio1)
			   {
				 for(int j=0; j<words.size();j++)
				 {
				   String word;
				   word = (String) words.get(j);
				   				 
				   this.bruteforce(view,word);
				   this.bruteforceJni(view,word);
				   this.rsbrute(view,word);
				
				 }

				    //Find the max from java result ( what a news?! ) 
				 max = this.find_max(result_j);	

				    //and plot the results
				 this.plot(max.intValue());
				    
				    //Clear the temporary store of the results for further tests 
				 result_j.clear();
				 result_jni.clear();
				 result_rs.clear(); 	 
			   }
		 
			 else
				 if(selected == R.id.radio2)
				   {	
					 for(int j=0;j<matrix_dimension.length;j++)
					    {
						 
						 int dim;
						 dim = matrix_dimension[j];						 
						 
						 this.matrixjama(view,dim);	 
						 this.matrixJni(view,dim);	  
						 this.rsmatrix(view,dim);			
					    }
					 
					 //Find the max from java result ( what a news?! ) 
					 max = this.find_max(result_j);	

					 //and plot the results
					 this.plot(max.intValue());
					    
					 //Clear the temporary store of the results for further tests 
					 result_j.clear();
					 result_jni.clear();
					 result_rs.clear(); 	 			 
				   }
				   */
		HashMap<String, List> result = new HashMap();
		
		result.put("java", result_j);
		result.put("jni", result_jni);
		result.put("rs", result_rs);
		
		return result;
	}
	
	/**
	 * togliamo il dialog di attesa(perche non va)
	 */
	@Override
	protected void onPostExecute(HashMap result) {	
		Log.w("ANDROBENCHMARK", "tolgo");
		this.loadingDialog.dismiss();
					
    }


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}
	
	
	
	
	

}
