#include<app_androbenchmark_GrayScaling.h>

#include <android/bitmap.h>
#include <stdio.h>
#include <stdlib.h>
#include <android/log.h>
#include <time.h>


JNIEXPORT void JNICALL Java_app_androbenchmark_GrayScaling_pureJni(JNIEnv* env, jclass clazz, jobject bm) {

	AndroidBitmapInfo infocolor;
	void* pixelscolor;
	int ret;
	int y;
	int x;

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

			//trucco per prendere i pixel senza usare librerie(uso maschere per prendere le porzioni giuste)
			int r = (line[x] ) & 0x000000ff;
			int g = (line[x] ) & 0x0000ff00;
			int b = (line[x] ) & 0x00ff0000;
			int a = (line[x] ) & 0xff000000;
			//__android_log_print(ANDROID_LOG_DEBUG, "ANDROTAG", "r is %08x , g is %08x , b is %08x",r,g,b);

			//il filtro di grigio e dato dalla somma dei tre pixel e poi opportunamente shiftati nella loro posizione giusta
			int r_gray = r*0.299 + ( (int)((g >> 8) * 0.587 )) + ( (int)( (b >> 16) * 0.114 ));
			int g_gray = ((int)(r*0.299 + ( (int)((g >> 8) * 0.587 )) + ( (int)( (b >> 16) * 0.114 ))) << 8 );
			int b_gray = ((int)(r*0.299 + ( (int)((g >> 8) * 0.587 )) + ( (int)( (b >> 16) * 0.114 ))) << 16 );
			//lascio inalterata la luminosita
			int a_gray = a;

			//controlli dei bound
			if(r_gray <= 0x00000000) {
				r_gray = 0x00000000;
			}

			if(r_gray > 0x000000ff) {
				r_gray = 0x000000ff;
			}


			if(g_gray <= 0x000000ff) {
				g_gray = 0x00000000;
			}

			if(g_gray > 0x0000ff00) {
				g_gray = 0x0000ff00;
			}


			if(b_gray <= 0x0000ffff) {
				b_gray = 0x00000000;
			}

			if(b_gray > 0x00ff0000) {
				b_gray = 0x00ff0000;
			}

			//creo il pixel giusto
			int gray = r_gray + g_gray + b_gray;

			//__android_log_print(ANDROID_LOG_DEBUG, "ANDROTAG", "new_r is %08x, new_g is %08x, new_b is %08x ", r_gray,g_gray,b_gray);

			//or bit a bit per posizionare in modo giusto le informazioni del pixel
			line[x] = gray;
			line[x] = gray | line[x];
			line[x] = gray | line[x];
			line[x] = a_gray | line[x];

			//__android_log_print(ANDROID_LOG_DEBUG, "ANDROTAG", "line is %08x ",line[x]);

		}

		pixelscolor = (char *)pixelscolor + infocolor.stride;

	}

	//sblocco l immagine
	AndroidBitmap_unlockPixels(env, bm);


}


