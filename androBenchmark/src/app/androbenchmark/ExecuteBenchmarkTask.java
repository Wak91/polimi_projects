package app.androbenchmark;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.BatteryManager;

public class ExecuteBenchmarkTask extends AsyncTask <Void, Void, HashMap<String, List<Integer>> > {
	
	
	
	private AlertDialog loadingDialog;
	private Context context;
	private int selected;
	private int stressmode;

	
	private final int num_of_test=3;
	private List<Integer> result_j;
	private List<Integer> result_jni;
	private List<Integer> result_rs;
	private List<Integer> result_type;
	
	private ArrayList<Integer> battery_result;


	
	private ArrayList<String> names; // this contain the name of all the images
    private int[] matrix_dimension;
    private ArrayList<String> words;
	


	//wrapper generale per gli asynctask cosi da non dover definire piu funzioni(una per benchmark)
	
	
	
	public ExecuteBenchmarkTask(MainActivity context, int selected , int sm){
		//setto il contesto giusto(passatto dalla UI)
		this.context = context;
		this.selected = selected;
		this.stressmode = sm;
		
	}
	
	/**
	 * mostriamo solo l alert (onPreExecute e onPostExecute girano sullo UI thread quindi limitiamo al mnimo il carico di lavoro cosi da non avere rallentamenti gafici)
	 */
	@Override
	protected void onPreExecute(){
		//Log.w("PRE", "nome "+ Looper.getMainLooper().getThread().getName());
		//mostro il loading dialog nel contesto giusto
		this.loadingDialog = new AlertDialog.Builder(context).setTitle("Executing").setMessage("Wait please...it could take many minutes").setIcon(android.R.drawable.ic_dialog_alert).show();
		this.loadingDialog.setCancelable(false);

		
	}
	
	/**
	 * a seconda della scelta eseguiamo il benchmark giusto all'interno di un asynctask
	 */
	@Override
	protected HashMap<String, List<Integer> > doInBackground(Void... params) {
		
		
		
		HashMap<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		//configurazione iniziale
		result_j = new ArrayList<Integer>();
		result_jni = new ArrayList<Integer>();
		result_rs = new ArrayList<Integer>();
		result_type = new ArrayList<Integer>();
		battery_result = new ArrayList <Integer> ();

		//Initialize battery_result with fake value, if StressBattery Mode is on we will update this array later
		battery_result.add(0);
		battery_result.add(0);
		battery_result.add(0);
	
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
			Bitmap bm = null;
			Bitmap bm2 = null; 
		      for(int i=0;i<names.size();i++)
		      {    
				try {
					bm = BitmapFactory.decodeStream(this.context.getAssets().open(""+names.get(i)));
					bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				
								    
				Long t = GrayScaling.callPureJava(bm2);
				result_j.add(t.intValue()); 
				
				t = GrayScaling.callPureJni(bm2); 
			    result_jni.add(t.intValue()); 
				
			    t = GrayScaling.callPureRenderScript(bm2, this.context);
		     	result_rs.add(t.intValue());
		     		     	
		     }
		  
		      result_type.add(0);
		     //Now battery tests 	  
		       
		    //Take the smallest image 
		    try {
				bm = BitmapFactory.decodeStream(this.context.getAssets().open(""+names.get(0)));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
			if(stressmode == 1)
			{
			battery_result.clear();//Remove the fake values
			bm2 = bm.copy(bm.getConfig(), true);
			
			//PRIMA DI INIZIARE E' MEGLIO FAR SCALARE DI UNO IL VALORE DELLA BATTERIA PER EVITARE DI FARSARE IL RISULTATO
			
			this.doJunk();
			
            this.battery_result.add(GrayScaling.stressJavaBattery(bm2, context));	
            this.battery_result.add(GrayScaling.stressJNIBattery(bm2, context));		 	 
            this.battery_result.add(GrayScaling.stressRSBattery(bm2, context));		  
			}
            
	        //Log.w("DEBUG", "Values of battery result are" + battery_result.get(0)+" -- " +  battery_result.get(1) + " -- " +  battery_result.get(2));

		 }  
		  
		 //caso bruteforce
		 else if(selected == R.id.radio1) {
			   String word="";
				 for(int j=0; j<words.size();j++)
				 {
				   word = (String) words.get(j);
				   				 
				   Long t = Bruteforce.callPureJava(word);
				   result_j.add(t.intValue()); 
					
				   t = Bruteforce.callPureJni(word);
				   result_jni.add(t.intValue()); 
					
				   t = Bruteforce.callPureRenderScript(word, this.context);
			       result_rs.add(t.intValue());
				
				 }
				 
				 result_type.add(1);
				 //Now battery tests 
				 
				 
				 
					if(stressmode == 1)
					{
					battery_result.clear();//Remove the fake values
					word = (String) words.get(0); // smallest word 
					
					this.doJunk();
					
		            this.battery_result.add(Bruteforce.stressJavaBattery(word, context));	
		            this.battery_result.add(Bruteforce.stressJNIBattery(word, context));		 	 
		            this.battery_result.add(Bruteforce.stressRSBattery(word, context));		  
					}
			     
			   }
		
		  //caso moltiplicazione
		  else if(selected == R.id.radio2) {	
			  int dim=2;
			  
			  for(int j=0;j<matrix_dimension.length;j++){
						 
				dim = matrix_dimension[j];						 
						 
				Long t = Matrix.callPureJava(dim);
				result_j.add(t.intValue()); 
								
				t = Matrix.callPureJni(dim);
				result_jni.add(t.intValue()); 
							
				t = Matrix.callPureRenderScript(dim, this.context); 
			    result_rs.add(t.intValue());		
					     
			 }
				
			  result_type.add(2);
			//Now battery tests 
		
			if(stressmode == 1)
			{
				
			battery_result.clear();//Remove the fake values
			dim = matrix_dimension[0]; //the smallest matrix   
			
			this.doJunk();
			
            this.battery_result.add(Matrix.stressJavaBattery(dim, context));	
            this.battery_result.add(Matrix.stressJNIBattery(dim, context));		 	 
            this.battery_result.add(Matrix.stressRSBattery(dim, context));	
            	  
			}
			

	        
		 }
			 		
		result.put("java", result_j); //valori dei tempi di esecuzione
		result.put("jni", result_jni);
		result.put("rs", result_rs);
		result.put("type", result_type);
		
		result.put("battery", battery_result); // valori della scarica della batteria 
		//Log.w("ANDROBENCHMARK", "il risultato e " + result.get("battery"));
				
		return result;
	}
	
	/**
	 * togliamo il dialog di attesa e disegnamo il grafico
	 */
	@Override
	protected void onPostExecute(HashMap<String, List<Integer>> result) {	
		//Log.w("ANDROBENCHMARK", "tolgo");
		this.loadingDialog.dismiss();
		
		Intent intent = new Intent(this.context, GraphActivity.class);
		
		intent.putExtra(MainActivity.RESULTS, result);
		intent.putExtra(MainActivity.STRESS, this.stressmode);
		
		this.context.startActivity(intent);				
    }
	

	public Context getContext() {
		return context;
	}


	public void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * Function to lose 1% and start stressBattery with a clean value
	 */
	private void doJunk()
	{
	  int l_diff=0;
	  int l_before=0;
	  Random randomGenerator = new Random();
	  l_before = this.getLevel(this.context);  
	  
	  do{
		 randomGenerator.nextInt(); 
		 l_diff = l_before - this.getLevel(this.context);
	    }while(l_diff<1);
	}
	
	private int getLevel(Context context)
	{
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    Intent b = context.registerReceiver(null, ifilter);
	    int lev = b.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
	    	    
	    return lev;	
	}
	
	
	
	
	
	
	
	
	
	

}
