#include<app_androbenchmark_GrayScaling.h>
#include <time.h>
#include <android/log.h>
#include <android/bitmap.h>

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

JNIEXPORT void JNICALL Java_app_androbenchmark_GrayScaling_pureJni(JNIEnv* env, jclass clazz, jobject bm){

	AndroidBitmapInfo  infocolor;
	void*              pixelscolor;
	int                ret;
	int             y;
	int             x;

	 if ((ret = AndroidBitmap_getInfo(env, bm, &infocolor)) < 0) {

	   return;
	 }

	 if ((ret = AndroidBitmap_lockPixels(env, bm, &pixelscolor)) < 0) {

	 }



	 for (y=0;y<infocolor.height;y++) {
		 uint32_t * line = (uint32_t *) pixelscolor;



	         for (x=0;x<infocolor.width;x++) {

	        	 int r = line[x] & 0xff;
	        	 int g = (line[x] >> 8) & 0xff;
	             int b = (line[x] >> 16) & 0xff;
	             int a = (line[x] >> 24) & 0xff;

	             line[x] = ((255-0.3 * r) + (255-0.59 * g) + (255-0.11 * b))/3;
	         }

	         pixelscolor = (char *)pixelscolor + infocolor.stride;

	  }


	 AndroidBitmap_unlockPixels(env, bm);
}


