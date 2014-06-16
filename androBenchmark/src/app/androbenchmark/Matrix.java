package app.androbenchmark;

import java.util.Random;

import android.graphics.Bitmap;


public class Matrix {


	/**
	 * 
	 * moltiplicazione tra 2 matrici in puro java
	 * @return
	 */
	public static Long pureJava(){	
		
		Long t = System.currentTimeMillis();
		
		int[][] m1 = new int[300][300];  
		int[][] m2 = new int[300][300]; 
		int[][] result = new int[500][500];

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
		
		t = System.currentTimeMillis() - t;

		return t;


	}
	
	
	public static Long callPureJni(){
		
		Long t = System.currentTimeMillis();
		
		pureJni();
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}

	private native static void pureJni();

}
