package app.androbenchmark;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.BatteryManager;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;


public class GrayScaling {	
	
	private static final String TAG = "GrayScale"; //tag for logcat 
	
// ----------------------------- BENCHMARK CORE ----------------------------- //
	
	
	private static void pureJava(Bitmap bm2){

		
    	int r,g,b , pixel;
    	for(int x=0; x<bm2.getWidth(); x++){
    	   for(int y=0; y<bm2.getHeight(); y++)
    	      {
    		    pixel = bm2.getPixel(x, y);
    		    r = (int) (Color.red(pixel) * 0.299);
    		    g = (int) (Color.green(pixel) * 0.587);
    		    b = (int) (Color.blue(pixel) * 0.114);
    		    //fix immagine verde (i valori r,g,b vanno sommati per ogni componente)
    		    int gray = Color.rgb(r+g+b,r+g+b,r+g+b);
    		    bm2.setPixel(x, y, gray);

    	      }
    	}
    	 	
    	
	}
	
	private native static void pureJni(Bitmap bm);
	
	private static void pureRenderScript(RenderScript rs, ScriptC_filter mScript, Allocation mOutAllocation, Bitmap bm2){
					
		//chiamo la funzione di rendercript (script_c)
		mScript.invoke_filter();
		rs.finish(); // let's wait for the script in this context to finish 
								
		
		mOutAllocation.copyTo(bm2);				
		
	}
	
// ----------------------------- END BENCHMARK CORE ----------------------------- //
	
	
	
// ----------------------------- SETUP BENCHMARK  ----------------------------- //	
	
	
	public static Long callPureJava(Bitmap bm){
		
		Long t = System.currentTimeMillis();
				
		pureJava(bm);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	public static Long callPureJni(Bitmap bm){
		
		Long t = System.currentTimeMillis();
		
		pureJni(bm);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	public static Long callPureRenderScript(Bitmap bm, Context context){
		
		
		//creo un istanza renderscript associandolo a questo contesto
	    RenderScript rs = RenderScript.create(context);

		//creo le 2 allocazioni di memoria con cui renderscript lavora
		Allocation mInAllocation = Allocation.createFromBitmap(rs, bm,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);

		Allocation mOutAllocation = Allocation.createTyped(rs, mInAllocation.getType());
				
		//associo la classe generata
		ScriptC_filter  mScript = new ScriptC_filter(rs,context.getResources(),R.raw.filter);

		//setto levariabili di renderscript
		mScript.set_gIn(mInAllocation);

		mScript.set_gOut(mOutAllocation);

		mScript.set_gScript(mScript);
					
		Long t = System.currentTimeMillis();
		//cuore del benchmark
		pureRenderScript(rs, mScript, mOutAllocation, bm);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
	}
	

// ----------------------------- SETUP BENCHMARK  ----------------------------- //
	

// ----------------------------- BATTERY STRESS  ----------------------------- //
	
	public static ArrayList <Integer> stressBattery(Bitmap bm , Context c) // need the context to register the receiver 
	{
	 
	 ArrayList <Integer> battery_result = new ArrayList<Integer>();
     int l_before = getVoltage(c);
     int l_after=0;
     
	 //Stress battery with Java 

	 Long t = System.currentTimeMillis();
	 
     do
     {
	  callPureJava(bm);  
	  l_after = getVoltage(c);
     } while(l_before - l_after <5 ); // when the battery is decreased by 5 points	
     
     Long t2 = System.currentTimeMillis() - t;
     
	 battery_result.add(t2.intValue());
	 
	 //Stress battery with JNI 
	 
     t = System.currentTimeMillis();
	 
     do
     {
	  callPureJni(bm);  
	  l_after = getVoltage(c);
     } while(l_before - l_after <5 ); // when the battery is decreased by 5 points	
     
     t2 = System.currentTimeMillis() - t;
     
	 battery_result.add(t2.intValue());
	 
	 //Stress battery with RS
	 
     t = System.currentTimeMillis();
	 
     do
     {
	  callPureRenderScript(bm,c);  
	  l_after = getVoltage(c);
     } while(l_before - l_after <5 ); // when the battery is decreased by 5 points	
     
     t2 = System.currentTimeMillis() - t;
     
	 battery_result.add(t2.intValue());
	 
	 return battery_result;
	}
	

	/**
	 * Funzione che ritorna il valore in mvolt della batteria 
	 * @return
	 */
	private static int getVoltage(Context context)
	{
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    Intent b = context.registerReceiver(null, ifilter);
	    int lev = b.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);    
	    return lev;	
	}
}
