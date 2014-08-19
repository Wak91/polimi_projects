package app.androbenchmark;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.renderscript.RenderScript;
import android.renderscript.ScriptGroup;
import Jama.*; 
import android.renderscript.ScriptGroup.Builder;


public class Matrix {


// ----------------------------- BENCHMARK CORE ----------------------------- //
	
	private static void pureJava(int dim){
		
		double[][] m1 = new double[dim][dim];  
		double[][] m2 = new double[dim][dim]; 
		double[][] result = new double[dim][dim];

		Random randomGenerator = new Random();

		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {

				m1[i][j] = randomGenerator.nextInt(100); //inizializzo a caso la matrice
				m2[i][j] = randomGenerator.nextInt(100);  //inizializzo a caso la matrice
				result[i][j] = 0;  //inizializzo a 0 la matrice
			}
		}
		
		
		Jama.Matrix A = new Jama.Matrix(m1);
		Jama.Matrix B = new Jama.Matrix(m2);

		A.times(B);
		
	}
	
	private native static void pureJni(int dim);
	
	private static void pureRenderScript(RenderScript rs, ScriptC_rsmatrix script , int dim){
		
		 script.set_dim(dim);
		 script.invoke_calc();
		 rs.finish();
	}

// ----------------------------- END BENCHMARK CORE ----------------------------- //
	
// ----------------------------- SETUP BENCHMARK  ----------------------------- //
	
	public static Long callPureJni(int dim){
		
		Long t = System.currentTimeMillis();
				
		pureJni(dim);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
public static Long callPureJava(int dim){
		
		Long t = System.currentTimeMillis();
		
		pureJava(dim);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	public static Long callPureRenderScript(int dim, Context context){
		
		
		RenderScript rs = RenderScript.create(context);
		ScriptC_rsmatrix script = new ScriptC_rsmatrix(rs,context.getResources(),R.raw.rsmatrix);
		
		Long t = System.currentTimeMillis();
		
		pureRenderScript(rs,script,dim);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	/*
	public static Long callparallRenderScript(MainActivity activity){
		

		Context context = activity.getBaseContext();
		
		RenderScript rs = RenderScript.create(context);
		
		ScriptGroup.Builder builder = new ScriptGroup.Builder(rs);
		
		ScriptC_rspmatrix script1 = new ScriptC_rspmatrix(rs,context.getResources(),R.raw.rspmatrix);
		ScriptC_sum_elements script2 = new ScriptC_sum_elements(rs,context.getResources(),R.raw.sum_elements);
				

		
		return (long) 1;
		
	
	}
	*/

// ----------------------------- END SETUP BENCHMARK  ----------------------------- //
	
	// ----------------------------- BATTERY STRESS  ----------------------------- //
	
			public static ArrayList <Integer> stressBattery(int dim , Context c) // need the context to register the receiver 
			{
			 
			 ArrayList <Integer> battery_result = new ArrayList<Integer>();
		     int l_before = getVoltage(c);
		     int l_after=0;
		     
			 //Stress battery with Java 

			 Long t = System.currentTimeMillis();
			 
		     do
		     {
			  callPureJava(dim);  
			  l_after = getVoltage(c);
		     } while(l_before - l_after <5 ); // when the battery is decreased by 5 points	
		     
		     Long t2 = System.currentTimeMillis() - t;
		     
			 battery_result.add(t2.intValue());
			 
			 //Stress battery with JNI 
			 
		     t = System.currentTimeMillis();
			 
		     do
		     {
			  callPureJni(dim);  
			  l_after = getVoltage(c);
		     } while(l_before - l_after <5 ); // when the battery is decreased by 5 points	
		     
		     t2 = System.currentTimeMillis() - t;
		     
			 battery_result.add(t2.intValue());
			 
			 //Stress battery with RS
			 
		     t = System.currentTimeMillis();
			 
		     do
		     {
			  callPureRenderScript(dim,c);  
			  l_after = getVoltage(c);
		     } while(l_before - l_after <5 ); // when the battery is decreased by 5 points	
		     
		     t2 = System.currentTimeMillis() - t;
		     
			 battery_result.add(t2.intValue());
			 
			 return battery_result;
			}
			

			/**
			 * Funzione che ritorna il valore in mvolt della batteria 
			 * @return
			 */
			private static int getVoltage(Context context)
			{
				IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			    Intent b = context.registerReceiver(null, ifilter);
			    int lev = b.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);    
			    return lev;	
			}
		
		
	
	
}
