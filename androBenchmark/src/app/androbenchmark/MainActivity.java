package app.androbenchmark;


import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

	//private static final String TAG= "MainActivity"; //tag for logcat 
	//Log.w("ANDROBENCHMARK", "id is" + selected);

	private final int num_of_test=3;
	private List gray_result_j;
	private List gray_result_jni;
	private List gray_result_rs;
	ArrayList names; // this contain the name of all the images

	private Long[] matrix_result;
	private Long[] brute_result;
	private XYPlot plot;

	
	private AlertDialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		gray_result_j = new ArrayList();
		gray_result_jni = new ArrayList();
		gray_result_rs = new ArrayList();

		names = new ArrayList();
		names.add("image1.bmp");
		names.add("image2.bmp");
		names.add("image3.bmp");
		names.add("image4.bmp");


		
		matrix_result = new Long[num_of_test];
		brute_result = new Long[num_of_test];
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
		 
		 if(selected == R.id.radio0)
		   {
			  for(int i=1;i<=names.size();i++)
		      {    
				Bitmap bm=null;
				try {
					bm = BitmapFactory.decodeStream(getAssets().open(""+names.get(i)));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
				
			    this.grayScale(view,bm);
			    this.grayScaleJni(view,bm);
			    this.render_filter(view,bm);
		     }
			  
			  
			 setContentView(R.layout.graph);
			 plot = (XYPlot) findViewById(R.id.xyPlot);
			 
			plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, names.size());     
			plot.setDomainValueFormat(new DecimalFormat("0"));
			plot.setDomainStepValue(1);

			plot.setDomainLeftMin(1);
			plot.setDomainRightMin(4);
		
			
			 plot.setRangeBoundaries(0,16000, BoundaryMode.FIXED);
			 plot.setRangeStepValue(15);
			 plot.setRangeValueFormat(new DecimalFormat("0"));
			 
			 XYSeries series1 = new SimpleXYSeries(gray_result_j,
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Pure Java");
			 
			 
			 XYSeries series2 = new SimpleXYSeries(gray_result_jni,
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "JNI");
			 
			 XYSeries series3 = new SimpleXYSeries(gray_result_rs,
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
			 
			 gray_result_j.clear();
			 gray_result_jni.clear();
			 gray_result_rs.clear();
			 
			 
		   }
		 else 
			 if(selected == R.id.radio1)
			   {
				 this.bruteforce(view);
				 this.bruteforceJni(view);
				 this.rsbrute(view);
				 
			   }
			 else
				 if(selected == R.id.radio2)
				   {	 
					 this.matrixjama(view);
					 this.matrixJni(view);
					 this.rsmatrix(view);
				   }
		
	}
		 
	
	 public void grayScale(View view,Bitmap bm){
	    	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------
	     		     		     	     	    	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJava", bm2); 	
	    	
	    	try {
				gray_result_j.add(task.get()); // retrieve the value from the async task
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		    
		 }
	   
	 
	 public void grayScaleJni(View view,Bitmap bm){
	    		     	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------	     	
	     	
	     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	task.execute(new GrayScaling(), "callPureJni", bm2); 	
	    	
	    	try {
	    		gray_result_jni.add(task.get()); // retrieve the value from the async task
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
	    	
	    	
	     }
	 
	 
	 
	 public void render_filter(View view,Bitmap bm)
	 {	  	
		  	 	
     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones     	
     	
     	//-----CORE OF THE BENCHMARK----------------------------	     	
     	/*
     	ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
     	
     	task.execute(new GrayScaling(), "callPureRenderScript", bm2, this);
     	*/
     	
     	showLoading();
     	
     	Long t = GrayScaling.callPureRenderScript(bm2, this);
     
     	gray_result_rs.add(t);
			 	
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
	 
	 public void rsparall_matrix(View view){
				 
		 
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
