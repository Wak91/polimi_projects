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

public class ExecuteBenchmarkTask extends AsyncTask <Void, Void, HashMap<String, List<Long>> > {
	
	
	private AlertDialog loadingDialog;
	private AlertDialog choiceDialog;
	private Context context;
	private int selected;
	private MainActivity activity;

	
	private final int num_of_test=3;
	private List<Long> result_j;
	private List<Long> result_jni;
	private List<Long> result_rs;
	private ArrayList<String> names; // this contain the name of all the images
    private int[] matrix_dimension;
    private ArrayList<String> words;
	


	//wrapper generale per gli asynctask cosi da non dover definire piu funzioni(una per benchmark)
	
	
	
	public ExecuteBenchmarkTask(MainActivity context, int selected){
		//setto il contesto giusto(passatto dalla UI)
		this.context = context;
		this.selected = selected;
		this.activity = context;
		
	}
	
	
	/**
	 * mostriamo solo l alert (onPreExecute e onPostExecute girano sullo UI thread quindi limitiamo al mnimo il carico di lavoro cosi da non avere rallentamenti gafici)
	 */
	@Override
	protected void onPreExecute(){
		//Log.w("PRE", "nome "+ Looper.getMainLooper().getThread().getName());
		//mostro il loading dialog nel contesto giusto
		//Log.w("ANDROBENCHMARK", "mostro");
		this.loadingDialog = new AlertDialog.Builder(context).setTitle("Executing").setMessage("Wait please...").setIcon(android.R.drawable.ic_dialog_alert).show();
		
		
	}
	
	
	
	/**
	 * a seconda della scelta eseguiamo il benchmark giusto all'interno di un asynctask
	 */
	@Override
	protected HashMap<String, List<Long> > doInBackground(Void... params) {
		
		//configurazione iniziale
		result_j = new ArrayList<Long>();
		result_jni = new ArrayList<Long>();
		result_rs = new ArrayList<Long>();

		//Initialization of image's name 
		names = new ArrayList<String>();
		names.add("image0.bmp");
		names.add("image1.bmp");
		names.add("image2.bmp");
		
		//Inizialization of matrix dimension
		matrix_dimension = new int[3];	
		matrix_dimension[0]=100;
		matrix_dimension[1]=150;
		matrix_dimension[2]=200;
		
		//Inizialization of words to crack 
		words = new ArrayList<String>();
		words.add("ciao");
		words.add("ciaoo");
		words.add("ciaooo");
		
		//Log.w("DOINBACK",  "nome "+ Looper.myLooper());
		 //caso grayscale
		 if(selected == R.id.radio0){
			  
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
		 
		 //caso bruteforce
		 else if(selected == R.id.radio1) {
				 for(int j=0; j<words.size();j++)
				 {
				   String word;
				   word = (String) words.get(j);
				   				 
				   Long t = Bruteforce.callPureJava(word);
				   result_j.add(t); 
					
				   t = Bruteforce.callPureJni(word);
				   result_jni.add(t); 
					
				   t = Bruteforce.callPureRenderScript(word, this.context);
			       result_rs.add(t);
				
				 }

				  	 
			   }
		
		  //caso moltiplicazione
		  else if(selected == R.id.radio2) {	
			
			  for(int j=0;j<matrix_dimension.length;j++){
						 
				int dim;
				dim = matrix_dimension[j];						 
						 
				Long t = Matrix.callPureJava(dim);
				result_j.add(t); 
				
				Log.w("ANDROBENCHMARK", "mostro" + dim); 
				
				t = Matrix.callPureJni(dim);
				result_jni.add(t); 
							
				t = Matrix.callPureRenderScript(dim, this.context); 
			    result_rs.add(t);		
					     
			 }
					  	 			 
		 }
			 
	
		HashMap<String, List<Long>> result = new HashMap<String, List<Long>>();
		
		result.put("java", result_j);
		result.put("jni", result_jni);
		result.put("rs", result_rs);
		
		return result;
	}
	
	/**
	 * togliamo il dialog di attesa e disegnamo il grafico
	 */
	@Override
	protected void onPostExecute(HashMap<String, List<Long>> result) {	
		//Log.w("ANDROBENCHMARK", "tolgo");
		this.loadingDialog.dismiss();
		
		//scaliamo il grafico in modo appropriato
		Long max = this.find_max(result.get("java"));
		//disebnamo il grafico
		this.activity.drawPlot(max.intValue(), result);
		
		this.activity.showChoiceDialog();
					
    }
	
	/**
	 * funzione utile per avere ua scala sulle y appropriata
	 * @param result_j
	 * @return
	 */
	 private Long find_max(List<Long> result_j) {
			
			Long max=(long) -1;
			Long r ;
			 for(int i=0;i<result_j.size();i++)
			    {		 
				 r= (Long) result_j.get(i);
				 if(r>max)
					 max=(Long) result_j.get(i);	 
			    }
			 if(max==-1)
			   {
				 max=(long) 1500;
			   }
			 return max;
			 
	}


	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}
	
	
	
	
	

}
