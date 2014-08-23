package app.androbenchmark;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import app.androbenchmark.util.TitlePageIndicator;


public class GraphActivity extends Activity {
	
	private XYPlot plot;
	private XYPlot battery_plot;
	private AlertDialog.Builder choiceDialog;
	
	private ViewPager viewPager;
    private PagerAdapter adapter;
    
    private HashMap<String, List<Integer>> result;
    
    TitlePageIndicator mIndicator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		//Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    
		setContentView(R.layout.graph_layout);
		
		Intent intent = getIntent();
		//prendiamo il risultato passato dall asynctask
		HashMap<String, List<Integer>> result = (HashMap<String, List<Integer> >)intent.getSerializableExtra(MainActivity.RESULTS);
		int stress = intent.getIntExtra(MainActivity.STRESS, 0);
		
		// ricaviamo il contenitore ViewPager	
        viewPager = (ViewPager) findViewById(R.id.pager);
        // laciamo il compito di costruire le varie view all adapter creato appositamente
        adapter = new ViewPagerAdapter(GraphActivity.this, result, stress);
        // associamo l adapter al view pager di prima
        viewPager.setAdapter(adapter);
 
        //mettiamo i titoli alle varie view
        mIndicator = (TitlePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(viewPager);
        
		
		this.showChoiceDialog();
		
		

	}

	
	 /**
	  * funzione che disegna il grafico dei tempi di esecuzione 
	  * @param max
	  * @param result
	  */
	 public void drawPlot(int max, HashMap<String, List<Integer>> result, View view)
	 {
		this.setResult(result); 
		// setContentView(view);
		 //la view giusta viene passata dall adapter
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
	 
	 public void drawBatteryPlot(int max, HashMap<String, List<Integer>> result ,  View view)
	 {
		 //Log.w("GRAPH", "entrato");
		 
		 //Log.w("GRAPH", "result e " + result.toString());
		 
		 //TESTING 
		 
		 
		 HashMap<String, List<Integer>> result1 = new HashMap<String, List<Integer>>();
		 
		 ArrayList <Integer> ar = new ArrayList <Integer>();
		 ar.add(0); // fake value to plot correctly the graph 
		 ar.add(result.get("battery").get(0));
		 ar.add(result.get("battery").get(1));
		 ar.add(result.get("battery").get(2));
		 
		 result1.put("battery", ar);
		 
		 //TESTING 
		 
		 battery_plot = (XYPlot) view.findViewById(R.id.xyPlot);
		 BarFormatter format1 = new BarFormatter(Color.RED,Color.RED);
		 format1.setPointLabelFormatter(new PointLabelFormatter(Color.WHITE));
		 
		 battery_plot.setTicksPerRangeLabel(1);
		 battery_plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
		 battery_plot.setRangeValueFormat(new DecimalFormat("0"));
		 battery_plot.setTicksPerDomainLabel(1);
		 
		 XYSeries series1 = new SimpleXYSeries(result1.get("battery"),
				 SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "");
				 battery_plot.addSeries(series1, format1);
		
		 battery_plot.setRangeBoundaries(0,max+1000, BoundaryMode.FIXED);
		
		 battery_plot.setDomainLeftMin(-1);
		 battery_plot.setDomainRightMin(4);
		
		 battery_plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 1); //Increment the domain by 1
	     battery_plot.setDomainValueFormat(new DecimalFormat("0"));

		 
		 battery_plot.getBackgroundPaint().setAlpha(0);
		 battery_plot.getGraphWidget().getBackgroundPaint().setAlpha(0);
		 battery_plot.getGraphWidget().getGridBackgroundPaint().setAlpha(0);
		 
		 //battery_plot.getLayoutManager().remove(battery_plot.getLegendWidget()); // remove legend 
		 
		  battery_plot.getGraphWidget().setDomainValueFormat(new GraphXLabelFormat());
		 


		 		 
	 }
	 
	 
		/**
		 * funzione utile per avere una scala sulle y appropriata
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
	 
	 public void postResult() throws IOException{
		 
		SendResultTask task = new SendResultTask(this);
		task.execute();
	 }


	public HashMap<String, List<Integer>> getResult() {
		return result;
	}


	public void setResult(HashMap<String, List<Integer>> result) {
		this.result = result;
	}
	
	
	
	
}
