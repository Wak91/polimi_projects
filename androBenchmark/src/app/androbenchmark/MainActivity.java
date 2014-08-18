package app.androbenchmark;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

	//private static final String TAG= "MainActivity"; //tag for logcat 
	//Log.w("ANDROBENCHMARK", "id is" + selected);

	 private XYPlot plot;
    
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		
		TelephonyManager tManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		String uid = tManager.getDeviceId(); //retrieve uid of the phone for server analysis 

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
		
	   RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
	   int selected = radioGroup1.getCheckedRadioButtonId();		
	    
	   ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this, selected);
	   task.execute();
	   
	   
	   setContentView(R.layout.graph);
	   this.plot = (XYPlot) findViewById(R.id.xyPlot);
	   
	   try {
		   
		HashMap<String, List> result = task.get();
		Long max = this.find_max(result.get("java"));
		
		this.drawPlot(max.intValue(), result);
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
				   	
	}
		 
	

/*
	public void grayScale(View view,Bitmap bm){
	    	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------
	     		 	     	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJava", bm2); 	
	    	
	    	Long t;
			try {
				t = (Long)task.get();
				result_j.add(t); // retrieve the value from the async task
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
	     	//Long t = GrayScaling.callPureJava(bm2);
	     		
		    
		 }
	   
	 public void grayScaleJni(View view,Bitmap bm){
	    		     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------	     	
	     	
	     	//ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	//task.execute(new GrayScaling(), "callPureJni", bm2); 	
	    	
	     	Long t = GrayScaling.callPureJni(bm);
	        
	     	result_jni.add(t); 
	    
			
	    	
	    	
	     }
	 
	 public void render_filter(View view,Bitmap bm)
	 {	  	
		  	 	
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	
     	
     	//-----CORE OF THE BENCHMARK----------------------------	     	
     	/*
     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
     	
     	task.execute(new GrayScaling(), "callPureRenderScript", bm2, this);
     	
     	     	
     	Long t = GrayScaling.callPureRenderScript(bm2, this);
     
     	result_rs.add(t);
			 	
	 }
	 
 
	 public void matrixjama(View view, int dim){
	    	
		//-----CORE OF THE BENCHMARK----------------------------
		 	
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		//task.execute(new Matrix(), "calljavaJAMA" , dim); 
		Long t = Matrix.calljavaJAMA(dim); // Da sistemare, bisogna passare dall'async task ma logcat 
		//dice che calljavaJAMA non esiste! 
	
		result_j.add(t);
	
	   	    	   	   
	   	//----------------------------------------------------------
	   	      	 	    	
	 }
	 
	 public void matrixJni(View view, int dim){
	     	
		//-----CORE OF THE BENCHMARK----------------------------
		
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		//task.execute(new Matrix(), "callPureJni" , dim); 
	    
		Long t = Matrix.callPureJni(dim);
		
	
			result_jni.add(t);
		
		
		//----------------------------------------------------------    	
	 }
	 
	 	 
	 public void rsmatrix(View view, int dim){
		 
		//-----CORE OF THE BENCHMARK----------------------------
		
		/* 
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    task.execute(new Matrix(), "callPureRenderScript", this);
	    
		 
		 //showLoading();
		 
		 Long t = Matrix.callPureRenderScript(this,dim); 
		 
		 result_rs.add(t);
	     
	    //----------------------------------------------------------

	 }
	 
	 public void rsparall_matrix(View view){
				 //TODO
		 
	 }
	 
	 public void bruteforce(View view, String word){
	    	
		 //-----CORE OF THE BENCHMARK----------------------------
		 
		 ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		 //task.execute(new Bruteforce(), "callPureJava");
		 
		 Long t = Bruteforce.callPureJava(word);
		 
		 result_j.add(t);
		 
	   	 //----------------------------------------------------------
	   	      	 	
	    	
	 }

	 public void bruteforceJni(View view , String word){
	    	
	    //-----CORE OF THE BENCHMARK----------------------------
		 
	    ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
		//task.execute(new Bruteforce(), "callPureJni"); 
		
	    Long t = Bruteforce.callPureJni(word);
	    
	    result_jni.add(t);
	    
	   	//----------------------------------------------------------
	   	      	 	
	    	
	 }
	 

	 public void rsbrute(View view , String word){
		 		 
		//-----CORE OF THE BENCHMARK----------------------------
		/*
		ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
		     	
		task.execute(new Bruteforce(), "callPureRenderScript", this);
	
		 
		//showLoading();
		 
		Long t = Bruteforce.callPureRenderScript(this,word);
		
		//showResult(t);
		
		result_rs.add(t);
		
		
	    //----------------------------------------------------------
		 
		 
	 }
	 
	 private void showLoading(){
		 
		 
		 
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
	 
	 */
	
	 private Long find_max(List result_j) {
			
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
	 
	 private void drawPlot(int max, HashMap<String, List> result)
	 {
		 
		
		 
	     plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 3);     
	     plot.setDomainValueFormat(new DecimalFormat("0"));
		 plot.setDomainStepValue(1);

		 plot.setDomainLeftMin(0);
		 plot.setDomainRightMin(3);
		
		 max=max+500;
			
		 plot.setRangeBoundaries(0,max, BoundaryMode.FIXED);
		 plot.setRangeStepValue(5);
		 plot.setRangeValueFormat(new DecimalFormat("0"));
			 
		 XYSeries series1 = new SimpleXYSeries(result.get("java"),
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Pure Java");
			 
			 
		 XYSeries series2 = new SimpleXYSeries(result.get("jni"),
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "JNI");
			 
		 XYSeries series3 = new SimpleXYSeries(result.get("rs"),
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Renderscript");
			 
		 LineAndPointFormatter s1Format = new LineAndPointFormatter();
			    s1Format.setPointLabelFormatter(new PointLabelFormatter());
			    s1Format.configure(getApplicationContext(),
			        R.layout.lpf1);
			    
		 LineAndPointFormatter s2Format = new LineAndPointFormatter();
			    s2Format.setPointLabelFormatter(new PointLabelFormatter());
			    s2Format.configure(getApplicationContext(),
			        R.layout.lpf2);
			    
		 LineAndPointFormatter s3Format = new LineAndPointFormatter();
			    s3Format.setPointLabelFormatter(new PointLabelFormatter());
			    s3Format.configure(getApplicationContext(),
			        R.layout.lpf3);
			    
		plot.addSeries(series1, s1Format);
		plot.addSeries(series2, s2Format);
		plot.addSeries(series3, s3Format);
			 

		plot.setTicksPerDomainLabel(1);
		plot.setTicksPerRangeLabel(1);
		plot.getGraphWidget().setDomainLabelOrientation(-45);
			    
	    plot.getBackgroundPaint().setAlpha(0);
	    plot.getGraphWidget().getBackgroundPaint().setAlpha(0);
		plot.getGraphWidget().getGridBackgroundPaint().setAlpha(0);   
		
		
	 }
	 

	 
}
