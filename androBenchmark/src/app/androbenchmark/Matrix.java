package app.androbenchmark;

import java.util.Random;


public class Matrix {


	/**
	 * 
	 * moltiplicazione tra 2 matrici in puro java
	 * @return
	 */
	public static int[][] pureJava(){	

		int[][] m1 = new int[1000][1000];  
		int[][] m2 = new int[1000][1000]; ;
		int[][] result = new int[1000][1000];

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

		return result;


		}

	public native static void pureJni();

}
