/*
 * app_androbenchmark_Matrix.c
 *
 *  Created on: 22/mag/2014
 *      Author: phate
 */

#include <app_androbenchmark_Matrix.h>
#include <stdlib.h>
#include <android/log.h>


JNIEXPORT void JNICALL Java_app_androbenchmark_Matrix_pureJni(JNIEnv* env, jclass clazz, jint d){


	int dim = d;

	jint m1[dim][dim];
	jint m2[dim][dim];
	jint results[dim][dim];


	jint i = 0;
	jint j = 0;
	jint j2 = 0;



	for (i = 0; i < dim; i++) {
		for (j = 0; j < dim; j++) {

			srand(time(NULL));
			jint r = rand() % 1000;
			jint s = rand() % 1000;

			m1[i][j] = r; //inizializzo a caso la matrice
			m2[i][j] = s; //inizializzo a caso la matrice
			results[i][j] = 1;  //inizializzo a 0 la matrice
		}
	}

	__android_log_print(ANDROID_LOG_INFO, "ANDROBENCHMARK", "dim is %d ",d);

	//moltiplico le 2 matrici
	for (i = 0; i < dim; i++) { //scorro le righe di m1
		for (j = 0; j < dim; j++) { //scorro le colonne di m2
			for (j2 = 0; j2 < dim; j2++) { //centro la colonna e la riga giusta corrispondente
				results[i][j] = results[i][j] + (m1[i][j2] * m2[j2][j]);
			}

		}

	}


}


