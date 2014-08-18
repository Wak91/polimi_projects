package app.androbenchmark;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
	

}
