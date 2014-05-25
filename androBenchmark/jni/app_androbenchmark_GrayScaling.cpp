#include<app_androbenchmark_GrayScaling.h>

#include <android/bitmap.h>
#include <stdio.h>
#include <stdlib.h>


JNIEXPORT void JNICALL Java_app_androbenchmark_GrayScaling_pureJni(JNIEnv* env, jclass clazz, jobject bm){

	AndroidBitmapInfo  infocolor;
	void*              pixelscolor;
	int                ret;
	int             y;
	int             x;


	//prendo le info e faccio una lock sui i pixel
	 if ((ret = AndroidBitmap_getInfo(env, bm, &infocolor)) < 0) {

	   return;
	 }

	 if ((ret = AndroidBitmap_lockPixels(env, bm, &pixelscolor)) < 0) {

	 }



	 for (y=0;y<infocolor.height;y++) {
		 //i pixel sono visti come array tridimensionali
		 uint32_t * line = (uint32_t *) pixelscolor;

	         for (x=0;x<infocolor.width;x++) {

	        	 //trucco per prendere i pixel senza usare librerie(prendo gli ultimi 8 bit
	        	 //e poi prendo la parte che mi interessa shiftando a dovere)
	        	 int r = line[x] & 0xff;
	        	 int g = (line[x] >> 8) & 0xff;
	             int b = (line[x] >> 16) & 0xff;

	             //applico il filtro al pixel
	             line[x] = ((255-0.9 * r) + (255-0.59 * g) + (255-0.11 * b));
	         }


	         pixelscolor = (char *)pixelscolor + infocolor.stride;

	  }

	 //sblocco l immagine
	 AndroidBitmap_unlockPixels(env, bm);
}


