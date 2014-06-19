package app.androbenchmark;

import java.util.Random;

import android.content.Context;
import android.renderscript.RenderScript;
import Jama.*; 


public class Matrix {


// ----------------------------- BENCHMARK CORE ----------------------------- //
	
	private static void pureJava(){	
		
		
		
		int[][] m1 = new int[300][300];  
		int[][] m2 = new int[300][300]; 
		int[][] result = new int[300][300];

		Random randomGenerator = new Random();

		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m1[0].length; j++) {

				m1[i][j] = randomGenerator.nextInt(100); //inizializzo a caso la matrice
				m2[i][j] = randomGenerator.nextInt(100);  //inizializzo a caso la matrice
				result[i][j] = 0;  //inizializzo a 0 la matrice
			}
		}

		//moltiplico le 2 matrici
		for (int i = 0; i < m1.length; i++) { //scorro le righe di m1
			for (int j = 0; j < m2[0].length; j++) { //scorro le colonne di m2
				for (int j2 = 0; j2 < m2[0].length; j2++) { //centro la colonna e la riga giusta corrispondente
					result[i][j] = result[i][j] + ( m1[i][j2] * m2[j2][j] );
				}

			}

		}
		


	}
	
	
	private static void javaJAMA(){
		
		
		double[][] m1 = new double[300][300];  
		double[][] m2 = new double[300][300]; 
		double[][] result = new double[300][300];

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
	
	private native static void pureJni();
	
	private static void pureRenderScript(RenderScript rs, ScriptC_rsmatrix script){
		
		 script.invoke_calc();
		 rs.finish();
								

	}

// ----------------------------- END BENCHMARK CORE ----------------------------- //
	
// ----------------------------- SETUP BENCHMARK  ----------------------------- //
	
	
	public static Long callPureJava(){
		
		Long t = System.currentTimeMillis();
		
		pureJava();
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	
	public static Long callPureJni(){
		
		Long t = System.currentTimeMillis();
		
		pureJni();
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
public static Long calljavaJAMA(){
		
		Long t = System.currentTimeMillis();
		
		javaJAMA();
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	public static Long callPureRenderScript(MainActivity activity){
		
		Context context = activity.getBaseContext();
		
		RenderScript rs = RenderScript.create(context);
		ScriptC_rsmatrix script = new ScriptC_rsmatrix(rs,context.getResources(),R.raw.rsmatrix);
		
		Long t = System.currentTimeMillis();
		
		pureRenderScript(rs, script);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}

// ----------------------------- END SETUP BENCHMARK  ----------------------------- //

}
