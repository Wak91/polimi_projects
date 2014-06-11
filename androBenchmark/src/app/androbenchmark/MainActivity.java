package app.androbenchmark;

import java.util.Random;
import java.util.concurrent.Callable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Element.DataType;
import android.renderscript.RenderScript;
import android.renderscript.Matrix4f;
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
	     	long t = System.currentTimeMillis();
	     		     
	     	
	    	//new ExecuteBenchmarkTask().execute(new GrayScaling(), "pureJava", bm2);
	    	new ExecuteBenchmarkTask().execute(new GrayScaling(), "pureJava", bm2);
	    	
	    	t = System.currentTimeMillis() - t;
	    	//----------------------------------------------------------
	    	
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
	 
	 
	 
	 public void render_filter(View view)
	 {	  	
		  
	 	Bitmap bm = BitmapFactory.decodeResource(getResources(),  R.drawable.image); 
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	

     	//creo un istanza renderscript associandolo a questo contesto
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
		rs.finish(); // let's wait for the script in this context to finish 
		
		t = System.currentTimeMillis() - t;
		//----------------------------------------------------------
		
		//copio da memoria virtuale di rs all'immagine reale
		mOutAllocation.copyTo(bm2);
		
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
	 
	 
	 /*
	  *  Great doc.
	  *  http://stackoverflow.com/questions/21734275/calculate-the-sum-of-values-in-an-array-using-renderscript?lq=1
	  *  http://stackoverflow.com/questions/10576583/passing-array-to-rsforeach-in-renderscript-compute [ TO READ ]
	  */
	 public void simple_math(View view){
	     
     Random rg = new Random();

     int[] test  = new int[64];
     int[] test2  = new int[64];
          
     for(int i=0; i<63;i++)
    	 	{
    	 	 test[i]  = rg.nextInt(10);
    	 	}
        
	 RenderScript rs = RenderScript.create(this);
	 ScriptC_simple_math script = new ScriptC_simple_math(rs,getResources(),R.raw.simple_math);
	 script.set_gScript(script);

	 //Creo l'allocazione per l'array test 
	 Allocation array = Allocation.createSized(rs, Element.I32(rs), test.length, Allocation.USAGE_SCRIPT);
	 Allocation r = Allocation.createSized(rs, Element.I32(rs), test.length, Allocation.USAGE_SCRIPT);
     array.copyFrom(test);
     
   
	 script.set_gIn(array);
	 script.set_gOut(r);	 
	 
     script.invoke_calc();
     
     r.copyTo(test2);
     
     Log.w("ANDROBENCHMARK" , "test was:\n");
     for(int j=0; j<test.length;j++)
         Log.w("ANDROBENCHMARK",j+")"+test[j]);
     
     
     Log.w("ANDROBENCHMARK" , "test2 is:\n");

     for(int j=0; j<test2.length;j++)
        Log.w("ANDROBENCHMARK",j+")"+ test2[j]);
     
	 }

	 
	 public void simple_matrix(View view)
	 {
		 
		 Random rg = new Random();		
 	 	
	     Matrix4f test = new Matrix4f();
	     Matrix4f test2 = new Matrix4f();

	     for(int i=0; i<4;i++)
 	 	  for(int j=0;j<4;j++)
 	 		  {
 	 		  test.set(i, j,rg.nextInt(100));
 	 		  test2.set(i, j,rg.nextInt(100));
 	 		  }
 	 	 	 
		 RenderScript rs = RenderScript.create(this);
		 
		 ScriptC_simple_matrix script = new ScriptC_simple_matrix(rs,getResources(),R.raw.simple_matrix);
		 script.set_gScript(script);
		 		 
		 script.set_matrix1(test);
		 script.set_matrix2(test2);
		
		 script.invoke_hello(); 		
		
	 }
	 
	 public void rsmatrix(View view)
	 {
		
		 RenderScript rs = RenderScript.create(this);
		 ScriptC_rsmatrix script = new ScriptC_rsmatrix(rs,getResources(),R.raw.rsmatrix);
		 script.set_gScript(script);
		 
		 Element.Builder matrix = new Element.Builder(rs);
		 matrix.add(Element.U32(rs),"r0", 5);
		 matrix.add(Element.U32(rs),"r1", 5);
		 matrix.add(Element.U32(rs),"r2", 5);
		 matrix.add(Element.U32(rs),"r3", 5);
		 matrix.add(Element.U32(rs),"r4", 5);
		 
		 Element my_element = matrix.create();
		 
		 
		 Allocation wow = Allocation.createSized(rs, my_element, 5);
		 script.set_gIn(wow);
		 
		 
		 Allocation wow2 = Allocation.createSized(rs, my_element, 5);
		 script.set_gOut(wow2);
		 
		 
		 script.invoke_calc();
		 
		 
	 }
	 
	 public void bruteforce(View view){
	    	
		 	//-----CORE OF THE BENCHMARK----------------------------
		 	long t = System.currentTimeMillis();
		 	
	   	    Bruteforce.pureJava();
	   	    
	   	    t = System.currentTimeMillis() - t;
	   	    //----------------------------------------------------------
	   	      	 	
	   	    showResult(t);
	    	
	 }

	 public void bruteforceJni(View view){
	    	
		 	//-----CORE OF THE BENCHMARK----------------------------
		 	long t = System.currentTimeMillis();
		 	
	   	    Bruteforce.pureJni();
	   	    
	   	    t = System.currentTimeMillis() - t;
	   	    //----------------------------------------------------------
	   	      	 	
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
