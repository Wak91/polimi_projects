package app.androbenchmark;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

public class MainActivity extends Activity {

	//private static final String TAG= "MainActivity"; //tag for logcat 
	
	private AlertDialog loadingDialog;
	
	
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
	
	public void start_benchmark(View view){
		
		 final CheckBox checkgray = (CheckBox) findViewById(R.id.checkBox1);
		 
		 final CheckBox checkmatrixjni = (CheckBox) findViewById(R.id.checkBox2);
		 final CheckBox checkmatrixjama = (CheckBox) findViewById(R.id.checkBox4);
		 final CheckBox checkmatrixjava = (CheckBox) findViewById(R.id.checkBox5);
		 final CheckBox checkmatrixrs = (CheckBox) findViewById(R.id.checkBox6);

		 
		 final CheckBox checkbrute = (CheckBox) findViewById(R.id.checkBox3);

		 if(checkgray.isChecked())
		   {
			 this.grayScale(view);
			 //Here store the result of bench
			 this.grayScaleJni(view);
			 this.render_filter(view);		 
		   }
		 
		 if(checkmatrixjni.isChecked())
		  {
			 this.matrixJni(view);	 		 
		  }
		 
		 if(checkmatrixjama.isChecked())
		  {
			 this.matrixjama(view);	 		 
		  }
		 
		 if(checkmatrixjava.isChecked())
		  {
			 this.matrix(view); 		 
		  }
		 
		 if(checkmatrixrs.isChecked())
		  {
			 this.rsmatrix(view);	 		 
		  }
		 
		 	 
		 if(checkbrute.isChecked())
		 {
			 this.bruteforce(view);
			 this.bruteforceJni(view);
			 this.rsbrute(view);	 
		 }
		 
		 
		
	}
	
	 public void grayScale(View view){
	    	
	     	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------
	     		     		     	     	    	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJava", bm2); 	
	    	
	    	//----------------------------------------------------------
	    	
	    	//ImageView iv = (ImageView) findViewById(R.id.image);
	    	//iv.setImageBitmap(bm2);
	    	
	    	
	    	
	     }
	 
	 public void grayScaleJni(View view){
	    	
	     	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------	     	
	     	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJni", bm2); 	
	    	
	    	//----------------------------------------------------------
	    	
	    	//ImageView iv = (ImageView) findViewById(R.id.image);
	    	//iv.setImageBitmap(bm2);
	    		    	
	    	
	     }
	 
	 
	 
	 public void render_filter(View view)
	 {	  	
		  
	 	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
	 	
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	
     	
     	//-----CORE OF THE BENCHMARK----------------------------	     	
     	/*
     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
     	
     	task.execute(new GrayScaling(), "callPureRenderScript", bm2, this);
     	*/
     	
     	showLoading();
     	
     	Long t = GrayScaling.callPureRenderScript(bm2, this);
     	
     	showResult(t);
     	
     	//----------------------------------------------------------
		
		//ImageView iv = (ImageView) findViewById(R.id.image);
    	//iv.setImageBitmap(bm2);
		
		 	
	 }
	 
	 public void matrix(View view){
	    	
		//-----CORE OF THE BENCHMARK----------------------------
		 	
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		task.execute(new Matrix(), "callPureJava"); 
	   	    	   	   
	   	//----------------------------------------------------------
	   	      	 	    	
	 }
	 
	 
	 public void matrixjama(View view){
	    	
		//-----CORE OF THE BENCHMARK----------------------------
		 	
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		task.execute(new Matrix(), "calljavaJAMA"); 
	   	    	   	   
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
		
		/* 
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    task.execute(new Matrix(), "callPureRenderScript", this);
	    */
		 
		 showLoading();
		 
		 Long t = Matrix.callPureRenderScript(this); 
		 
		 showResult(t);
	     
	    //----------------------------------------------------------

		 
	 }
	 
	 public void bruteforce(View view){
	    	
		 //-----CORE OF THE BENCHMARK----------------------------
		 
		 ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		 task.execute(new Bruteforce(), "callPureJava");
		 
	   	 //----------------------------------------------------------
	   	      	 	
	    	
	 }

	 public void bruteforceJni(View view){
	    	
	    //-----CORE OF THE BENCHMARK----------------------------
		 
	    ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		task.execute(new Bruteforce(), "callPureJni"); 
		
	   	//----------------------------------------------------------
	   	      	 	
	    	
	 }
	 

	 public void rsbrute(View view){
		 		 
		//-----CORE OF THE BENCHMARK----------------------------
		/*
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
		     	
		task.execute(new Bruteforce(), "callPureRenderScript", this);
		*/
		 
		showLoading();
		 
		Long t = Bruteforce.callPureRenderScript(this);
		
		showResult(t);
		     
	    //----------------------------------------------------------
		 
		 
	 }
	 
	 private void showLoading(){
		 
		 loadingDialog = new AlertDialog.Builder(this).setTitle("Executing").setMessage("Wait please...").setIcon(android.R.drawable.ic_dialog_alert).show();

	 }
	 
	 private void showResult(Long t){
		 
		 loadingDialog.dismiss();
		 
		 new AlertDialog.Builder(this)
	        .setTitle("Benchmark ended").setMessage("Benchmark finished \n\nTime:" + t + "ms").setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) { 
	                // continue with delete
	            }
	         })
	        .setIcon(android.R.drawable.ic_dialog_alert).show();
	 }
	 
	 

	

}
