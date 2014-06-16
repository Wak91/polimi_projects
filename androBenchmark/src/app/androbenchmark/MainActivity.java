package app.androbenchmark;


import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {

	//private static final String TAG= "MainActivity"; //tag for logcat 
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	static 
	{
		System.loadLibrary("app_androbenchmark_GrayScaling");
		System.loadLibrary("app_androbenchmark_Matrix");
		System.loadLibrary("app_androbenchmark_Bruteforce");
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
	     		     		     	     	    	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJava", bm2); 	
	    	
	    	//----------------------------------------------------------
	    	
	    	ImageView iv = (ImageView) findViewById(R.id.image);
	    	iv.setImageBitmap(bm2);
	    	
	    	
	    	
	     }
	 
	 public void grayScaleJni(View view){
	    	
	     	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------	     	
	     	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJni", bm2); 	
	    	
	    	//----------------------------------------------------------
	    	
	    	ImageView iv = (ImageView) findViewById(R.id.image);
	    	iv.setImageBitmap(bm2);
	    		    	
	    	
	     }
	 
	 
	 
	 public void render_filter(View view)
	 {	  	
		  
	 	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	 	
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	
     	
     	//-----CORE OF THE BENCHMARK----------------------------	     	
     	
     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
     	
     	task.execute(new GrayScaling(), "callPureRenderScript", bm2, this); 
     	
     	//----------------------------------------------------------
		
		ImageView iv = (ImageView) findViewById(R.id.image);
    	iv.setImageBitmap(bm2);
		
		 	
	 }
	 
	 public void matrix(View view){
	    	
		//-----CORE OF THE BENCHMARK----------------------------
		 	
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		task.execute(new Matrix(), "callPureJava"); 
	   	    	   	   
	   	//----------------------------------------------------------
	   	      	 	    	
	 }
	 
	 public void matrixJni(View view){
	     	
		//-----CORE OF THE BENCHMARK----------------------------
		
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		task.execute(new Matrix(), "callPureJni"); 
	    
		//----------------------------------------------------------
	    	
	    
	    	
	 }
	 
	 	 
	 public void rsmatrix(View view){
		 
		//-----CORE OF THE BENCHMARK----------------------------
		 
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    task.execute(new Matrix(), "callPureRenderScript", this);
	     
	    //----------------------------------------------------------

		 
	 }
	 
	 public void bruteforce(View view){
	    	
		 //-----CORE OF THE BENCHMARK----------------------------
		 ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		 task.execute(new Bruteforce(), "pureJava");
		 
	   	 //----------------------------------------------------------
	   	      	 	
	    	
	 }

	 public void bruteforceJni(View view){
	    	
	    //-----CORE OF THE BENCHMARK----------------------------
	    ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		task.execute(new Bruteforce(), "callPureJni"); 
	   	//----------------------------------------------------------
	   	      	 	
	    	
	 }
	 

	 public void rsbrute(View view)
	 {
		 RenderScript rs = RenderScript.create(this);
		 ScriptC_brute script = new ScriptC_brute(rs,getResources(),R.raw.brute);
		 
		 String s1 = new String("ciaoo");
		 
		 int dim = s1.length();
		 
		 script.set_dim(dim);
		 
		 Allocation word = Allocation.createFromString(rs, s1,Allocation.USAGE_SCRIPT );
		 script.bind_word(word);
		 
		 long t = System.currentTimeMillis();
		 
		 script.invoke_brute();
		 rs.finish();
	   	 
		 t = System.currentTimeMillis() - t;

	   	 showResult(t);

		 
		 
	 }
	 
	 
	 
	 public void showResult(long t){
		 
		 new AlertDialog.Builder(this)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished \n\nTime:" + t + "ms").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // continue with delete
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert).show();
	 }
	 
	

}
