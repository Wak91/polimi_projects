/*
 * app_androbenchmark_Matrix.c
 *
 *  Created on: 22/mag/2014
 *      Author: phate
 */

#include <app_androbenchmark_Matrix.h>
#include <stdlib.h>


JNIEXPORT void JNICALL Java_com_hpps_androbenchmark_Matrix_pureJni(JNIEnv* env, jclass clazz){

	jint m1[1000][1000];
	jint m2[1000][1000];
	jint result[1000][1000];


	jint i = 0;
	jint j = 0;
	jint j2 = 0;

	for (i = 0; i < 1000; i++) {
		for (j = 0; j < 1000; j++) {

			srand(time(NULL));
			jint r = rand() % 1000;
			jint s = rand() % 1000;

			m1[i][j] = r; //inizializzo a caso la matrice
			m2[i][j] = s; //inizializzo a caso la matrice
			result[i][j] = 0;  //inizializzo a 0 la matrice
		}
	}

	//moltiplico le 2 matrici
	for (i = 0; i < 1000; i++) { //scorro le righe di m1
		for (j = 0; j < 1000; j++) { //scorro le colonne di m2
			for (j2 = 0; j2 < 1000; j2++) { //centro la colonna e la riga giusta corrispondente
				result[i][j] = result[i][j] + (m1[i][j2] * m2[j2][j]);
			}

		}

	}


}


