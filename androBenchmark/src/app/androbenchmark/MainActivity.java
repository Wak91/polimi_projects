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
	     	
	     	//-----CORE OF THE GS ALGORITHM----------------------------
	    	GrayScaling.pureJava(bm2);
	    	//----------------------------------------------------------
	    	
	    	ImageView iv = (ImageView) findViewById(R.id.image);
	    	iv.setImageBitmap(bm2);
	    	
	    	new AlertDialog.Builder(this)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished, press OK").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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
	 
	 public void matrix(View view){
	    	
	     	
	   	    Matrix.pureJava();
	    	 	
	    	new AlertDialog.Builder(this)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished, press OK").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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
	 
	 public void matrixJni(View view){
	    	
	    	Matrix.pureJni();
	    	
	    	new AlertDialog.Builder(this)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished, press OK").setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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
	 
	 
	 public void render_hello(View view)
	 {
		  RenderScript rs = RenderScript.create(this); //Initialize a rs context
		  ScriptC_hello hs = new ScriptC_hello(rs,getResources(),R.raw.hello); // initializate the script 
		  hs.invoke_hello_world(); //invoke the script 
	 }
	 
	 
	 
	 public void render_filter(View view)
	 {	  	
		  
	 	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	

		RenderScript rs = RenderScript.create(this);

		Allocation mInAllocation = Allocation.createFromBitmap(rs, bm2,Allocation.MipmapControl.MIPMAP_NONE,Allocation.USAGE_SCRIPT);

		Allocation mOutAllocation = Allocation.createTyped(rs, mInAllocation.getType());
		
		Log.w("myApp", "all right here");

		ScriptC_filter  mScript = new ScriptC_filter(rs,getResources(),R.raw.filter);

		Log.w("myApp", "passed");

		mScript.set_gIn(mInAllocation);

		mScript.set_gOut(mOutAllocation);

		mScript.set_gScript(mScript);

		mScript.invoke_filter();
		
		mOutAllocation.copyTo(bm2);
		
		 
    	
	 }
	    
	
	 

}
