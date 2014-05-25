package app.androbenchmark;

import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	private static final String TAG= "MainActivity"; //tag for logcat 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	static 
	{
		System.loadLibrary("app_androbenchmark_GrayScaling");
		System.loadLibrary("app_androbenchmark_Matrix");
	}
	  

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	 public void grayScale(View view){
	    	
	     	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------
	     	long t = System.currentTimeMillis();
	     	
	    	GrayScaling.pureJava(bm2);
	    	
	    	t = System.currentTimeMillis() - t;
	    	//----------------------------------------------------------
	    	
	    	ImageView iv = (ImageView) findViewById(R.id.image);
	    	iv.setImageBitmap(bm2);
	    	
	    	showResult(t);
	    	
	     }
	 
	 public void matrix(View view){
	    	
		 	//-----CORE OF THE BENCHMARK----------------------------
		 	long t = System.currentTimeMillis();
		 	
	   	    Matrix.pureJava();
	   	    
	   	    t = System.currentTimeMillis() - t;
	   	    //----------------------------------------------------------
	   	      	 	
	   	    showResult(t);
	    	
	     }
	 
	 public void matrixJni(View view){
	     	
		 	//-----CORE OF THE BENCHMARK----------------------------
		    long t = System.currentTimeMillis();
		 
	    	Matrix.pureJni();
	    	
	    	t = System.currentTimeMillis() - t;
	    	//----------------------------------------------------------
	    	
	    	showResult(t);
	    	
	     }
	 
	 
	 
	 public void render_filter(View view)
	 {	  	
		  
	 	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	

     	//creo un isrtanza renderscript associandolo a questo contesto
		RenderScript rs = RenderScript.create(this);

		//creo le 2 allocazioni di memoria con cui renderscript lavora
		Allocation mInAllocation = Allocation.createFromBitmap(rs, bm2,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);

		Allocation mOutAllocation = Allocation.createTyped(rs, mInAllocation.getType());
		
		//associo la classe generata
		ScriptC_filter  mScript = new ScriptC_filter(rs,getResources(),R.raw.filter);

		//setto levariabili di renderscript
		mScript.set_gIn(mInAllocation);

		mScript.set_gOut(mOutAllocation);

		mScript.set_gScript(mScript);
		
		//-----CORE OF THE BENCHMARK----------------------------
		long t = System.currentTimeMillis();
		//chiamo la funzione di rendercript (script_c)
		mScript.invoke_filter();
		t = System.currentTimeMillis() - t;
		//----------------------------------------------------------
		
		//copio da memoria virtuale di rs all'immagine reale
		mOutAllocation.copyTo(bm2);
		
		ImageView iv = (ImageView) findViewById(R.id.image);
    	iv.setImageBitmap(bm2);
		
    	showResult(t);
		 
    	
	 }
	 
	 
	 public void grayScaleJni(View view){
	    	
	     	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------
	     	long t = System.currentTimeMillis();
	     	
	    	GrayScaling.pureJni(bm2);
	    	
	    	t = System.currentTimeMillis() - t;
	    	//----------------------------------------------------------
	    	
	    	ImageView iv = (ImageView) findViewById(R.id.image);
	    	iv.setImageBitmap(bm2);
	    	
	    	showResult(t);
	    	
	     }
	 
	 private void showResult(long t){
		 
		 new AlertDialog.Builder(this)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished, press OK \n\nTime:" + t + "ms").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // continue with delete
	            }
	         })
	        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // do nothing
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert).show();
	 }
	    
	
	 

}
