package app.androbenchmark;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.viewpagerindicator.UnderlinePageIndicator;

import app.androbenchmark.util.SystemUiHider;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;


public class GraphActivity extends Activity {
	
	private XYPlot plot;
	private AlertDialog.Builder choiceDialog;
	
	private ViewPager viewPager;
    private PagerAdapter adapter;
    
    UnderlinePageIndicator mIndicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.graph_layout);
		
		Intent intent = getIntent();
		HashMap<String, List<Integer>> result = (HashMap<String, List<Integer> >)intent.getSerializableExtra(MainActivity.RESULTS);
		
		// Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) findViewById(R.id.pager);
        // Pass results to ViewPagerAdapter Class
        adapter = new ViewPagerAdapter(GraphActivity.this, result);
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);
 
        // ViewPager Indicator
        mIndicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
        mIndicator.setFades(false);
        mIndicator.setViewPager(viewPager);
        
		/*
		//scaliamo il grafico in modo appropriato
		Integer max = this.find_max(result.get("java"));
		
		//disegniamo il grafico
		this.drawPlot(max.intValue(), result);
		
		this.showChoiceDialog();
		*/
		

	}

	
	 /**
	  * funzione che disegna il grafico dei tempi di esecuzione 
	  * @param max
	  * @param result
	  */
	 public void drawPlot(int max, HashMap<String, List<Integer>> result, View view)
	 {
		 
		// setContentView(view);
		 this.plot = (XYPlot) view.findViewById(R.id.xyPlot);
		 
	     plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 3);     
	     plot.setDomainValueFormat(new DecimalFormat("0"));
		 plot.setDomainStepValue(1);

		 plot.setDomainLeftMin(0);
		 plot.setDomainRightMin(3);
		
		 max=max+300;
			
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
	 
		/**
		 * funzione utile per avere ua scala sulle y appropriata
		 * @param result_j
		 * @return
		 */
	 public Integer find_max(List<Integer> result_j) {
				
				int max= -1;
				int r ;
				 for(int i=0;i<result_j.size();i++)
				    {		 
					 r= result_j.get(i);
					 if(r>max)
						 max= result_j.get(i);	 
				    }
				 if(max==-1)
				   {
					 max= 1500;
				   }
				 return max;
				 
		}
	 
	 
	 
	 public void showChoiceDialog(){

		this.choiceDialog = new AlertDialog.Builder(this);
		this.choiceDialog.setMessage("Do you want to send data to the server?");
		//settiamo gli event listener appropriati (fare post o no al server)
		this.choiceDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		       
		    	Log.w("ANDROBENCHMARK", "sending.....");
		    	
		    	CheckConnectionTask task = new CheckConnectionTask(GraphActivity.this);
		 	    task.execute();
		    	dialog.dismiss();
		    	
		     }
		});
		
		this.choiceDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int id) {
		       
		    	Log.w("ANDROBENCHMARK", "Not accepted");
		    	dialog.dismiss();
		    	
		     }
		});
		
		this.choiceDialog.show();
		

	 }
	
}
