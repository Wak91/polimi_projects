package app.androbenchmark;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

	private final int num_of_test=3;
	private List result_j;
	private List result_jni;
	private List result_rs;
	private ArrayList names; // this contain the name of all the images
    private int[] matrix_dimension;
    private ArrayList words;
    
	private XYPlot plot;

	//DATA FOR PERSISTENT MEMORY
	//--------------------------
	private String mgs1="GSJ"; //Gray scale java
	private String mgs2="GSJN"; //Gray scale JNI
	private String mgs3="GSR";  //Gray scale renderscript 
	
	private String mmm1="MMJ"; //Matrix multiplication java
	private String mmm2="MMJN";
	private String mmm3="MMR";
	
	private String mb1="BJ"; //Bruteforce java
	private String mb2="BJN";
	private String mb3="BR";
	//--------------------------

	
	private AlertDialog loadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
				
		result_j = new ArrayList();
		result_jni = new ArrayList();
		result_rs = new ArrayList();

		//Initialization of image's name 
		names = new ArrayList();
		names.add("image0.bmp");
		names.add("image1.bmp");
		names.add("image2.bmp");
		
		//Inizialization of matrix dimension
		matrix_dimension = new int[3];	
		matrix_dimension[0]=300;
		matrix_dimension[1]=400;
		matrix_dimension[2]=500;
		
		//Inizialization of words to crack 
		words = new ArrayList();
		words.add("ciao");
		words.add("ciaoo");
		words.add("ciaooo");
		
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
		 Long max;
		 
		 if(selected == R.id.radio0)
		   {
			  
		      for(int i=0;i<names.size();i++)
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
			  		    		    
		    //Let's write the last result on the persistent memory for further visualization 
		    
		    this.store(result_j, mgs1);
		    this.store(result_jni, mgs2);
		    this.store(result_rs, mgs3);

		    //Find the max from java result ( what a news?! ) 
		    max = this.find_max(result_j);	

		    //and plot the results
		    this.plot(max.intValue());
		    
		    //Clear the temporary store of the results for further tests 
		    result_j.clear();
		    result_jni.clear();
		    result_rs.clear(); 	 
		    
		    
		   }
		 else 
			 if(selected == R.id.radio1)
			   {
				 for(int j=0; j<words.size();j++)
				 {
				   String word;
				   word = (String) words.get(j);
				   				 
				   this.bruteforce(view,word);
				   this.bruteforceJni(view,word);
				   this.rsbrute(view,word);
				
				 }
				 
				 this.store(result_j, mb1);
				 this.store(result_jni, mb2);
				 this.store(result_rs, mb3);

				    //Find the max from java result ( what a news?! ) 
				 max = this.find_max(result_j);	

				    //and plot the results
				 this.plot(max.intValue());
				    
				    //Clear the temporary store of the results for further tests 
				 result_j.clear();
				 result_jni.clear();
				 result_rs.clear(); 	 
			   }
		 
			 else
				 if(selected == R.id.radio2)
				   {	
					 for(int j=0;j<matrix_dimension.length;j++)
					    {
						 
						 int dim;
						 dim = matrix_dimension[j];						 
						 
						 this.matrixjama(view,dim);	 
						 this.matrixJni(view,dim);	  
						 this.rsmatrix(view,dim);			
					    }
					 
					 this.store(result_j, mmm1);
					 this.store(result_jni, mmm2);
					 this.store(result_rs, mmm3);

					 //Find the max from java result ( what a news?! ) 
					 max = this.find_max(result_j);	

					 //and plot the results
					 this.plot(max.intValue());
					    
					 //Clear the temporary store of the results for further tests 
					 result_j.clear();
					 result_jni.clear();
					 result_rs.clear(); 	 			 
				   }		
	}
		 
	
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

	public void grayScale(View view,Bitmap bm){
	    	
	     	Bitmap bm2 = bm.copy(bm.getConfig(), true); //bm is immutable, I need to convert it in a mutable ones 
	     	
	     	//-----CORE OF THE BENCHMARK----------------------------
	     		 	     	
	     	//ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this);
	     	
	    	//task.execute(new GrayScaling(), "callPureJava", bm2); 	
	    	
	     	Long t = GrayScaling.callPureJava(bm2);
	     	
		    result_j.add(t); // retrieve the value from the async task
			
		    
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
     	*/
     	     	
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
	    */
		 
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
		*/
		 
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
	 
	 private void plot(int max)
	 {
		 
		 setContentView(R.layout.graph);
	     plot = (XYPlot) findViewById(R.id.xyPlot);
			 
	     plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, names.size());     
	     plot.setDomainValueFormat(new DecimalFormat("0"));
		 plot.setDomainStepValue(1);

		 plot.setDomainLeftMin(0);
		 plot.setDomainRightMin(3);
		
		 max=max+500;
			
		 plot.setRangeBoundaries(0,max, BoundaryMode.FIXED);
		 plot.setRangeStepValue(5);
		 plot.setRangeValueFormat(new DecimalFormat("0"));
			 
		 XYSeries series1 = new SimpleXYSeries(result_j,
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Pure Java");
			 
			 
		 XYSeries series2 = new SimpleXYSeries(result_jni,
				        SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "JNI");
			 
		 XYSeries series3 = new SimpleXYSeries(result_rs,
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
	 
	 
	 /*
	  * result is the list of value that you want to store and the id 
	  * is the marker of the persistent data on the memory delcared at the start of the class 
	  * */
	 
	 private void store(List result , String id)
	 {
		 String n;
		    
		    for(int i=0;i<result.size();i++)
		       {
		    	n=result.toString();	    	
		    	try 
		    	  {
		    		  
				   FileOutputStream fos = openFileOutput(id, Context.MODE_PRIVATE);
				   fos.write(n.getBytes());
				   fos.close();
				  
				    } 
		    	catch (FileNotFoundException e) {
					 	e.printStackTrace();
				    } 
		    	catch (IOException e) {
						e.printStackTrace();
				    } 	
		        }		 
	 }
	 
	 

	

}
