package app.androbenchmark;



import java.text.DecimalFormat;
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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

	//private static final String TAG= "MainActivity"; //tag for logcat 
	//Log.w("ANDROBENCHMARK", "id is" + selected);

	 private XYPlot plot;
	 private AlertDialog loadingDialog;
    
	
	
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
	
	/**
	 * chiama la suite di benchmark scelta
	 * @param view
	 */
	public void start_benchmark(View view){
		
	   //this.loadingDialog = new AlertDialog.Builder(this).setTitle("Executing").setMessage("Wait please...").setIcon(android.R.drawable.ic_dialog_alert).show();
	   
	   //ricaviamo la scelta dell utente
	   RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
	   int selected = radioGroup1.getCheckedRadioButtonId();		
	   
	   //eseguiamo il benchamrk in un asynctask
	   ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this, selected);
	   task.execute();
	   
	   //creiamo il grafico
	   setContentView(R.layout.graph);
	   this.plot = (XYPlot) findViewById(R.id.xyPlot);
	   
	   try {
		//risultato passato dal task   
		HashMap<String, List> result = task.get();
		//scaliamo il grafico in modo appropriato
		Long max = this.find_max(result.get("java"));
		//disebnamo il grafico
		this.drawPlot(max.intValue(), result);
		
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 
				   	
	}
		 
	


	/**
	 * funzione utile per avere ua scala sulle y appropriata
	 * @param result_j
	 * @return
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
	 
	 /**
	  * funzione che disegna il grafico
	  * @param max
	  * @param result
	  */
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
