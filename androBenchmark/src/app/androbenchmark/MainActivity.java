package app.androbenchmark;



import com.androidplot.xy.XYPlot;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

public class MainActivity extends Activity {

	//private static final String TAG= "MainActivity"; //tag for logcat 
	//Log.w("ANDROBENCHMARK", "id is" + selected);
	
	 public final static String RESULTS = "app.androbenchmark.RESULTS";
	 public final static String STRESS = "app.androbenchmark.STRESS";
	
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
	

	
	/**
	 * chiama la suite di benchmark scelta
	 * @param view
	 */
	public void start_benchmark(View view){
		
		
	   //ricaviamo la scelta dell utente
	   RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
	   int selected = radioGroup1.getCheckedRadioButtonId();		
	   
	   
	   CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox1);
	   int stressmode=0; 
	   
	   if(checkBox.isChecked()) 
		  stressmode=1;
	
	   //eseguiamo il benchamrk in un asynctask (lasciamo a onPostExecute il compito di aggiornare la UI cosi da non avere rallentamenti )
	   ExecuteBenchmarkTask task = new ExecuteBenchmarkTask(this, selected,stressmode);
	   task.execute();

				   	
	}	 
}
