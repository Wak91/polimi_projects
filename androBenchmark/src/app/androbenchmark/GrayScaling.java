package app.androbenchmark;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.BatteryManager;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.util.Log;


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
	
	public static Integer stressJavaBattery(Bitmap bm , Context c) // need the context to register the receiver 
	{
	 
     int l_diff=0;
     
     int l_before = getVoltage(c);
	 
     for(int i=0;i<400;i++)
	    pureJava(bm);  
     
     l_diff = l_before - getVoltage(c);
     
     return l_diff;
	}
	
	public static Integer stressJNIBattery(Bitmap bm , Context c) // need the context to register the receiver 
	{  
		
	 int l_before = getVoltage(c);
	  
     for(int i=0;i<4000;i++)
	    pureJni(bm); 
     
     int l_diff = l_before - getVoltage(c);
     
     return l_diff;
     
	}
	 
	public static Integer stressRSBattery(Bitmap bm , Context c) // need the context to register the receiver 
	{
	 
	 RenderScript rs = RenderScript.create(c);

     Allocation mInAllocation = Allocation.createFromBitmap(rs, bm,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);

     Allocation mOutAllocation = Allocation.createTyped(rs, mInAllocation.getType());
				
	 ScriptC_filter  mScript = new ScriptC_filter(rs,c.getResources(),R.raw.filter);

	 mScript.set_gIn(mInAllocation);

	 mScript.set_gOut(mOutAllocation);

	 mScript.set_gScript(mScript);
						 
	 int l_before = getVoltage(c);
	
	 for(int i=0;i<4000;i++)
	    {
         mScript.invoke_filter();
 	     rs.finish();
        }
	 
	 int l_diff = l_before - getVoltage(c);
     rs.destroy();

	 return l_diff;
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
