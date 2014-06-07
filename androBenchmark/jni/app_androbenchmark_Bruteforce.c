#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>


JNIEXPORT void JNICALL Java_app_androbenchmark_Bruteforce_pureJni(JNIEnv* env, jclass clazz){

	char* secret = "ciaoo";

	static const char *lettere[] = {"a","b","c","d","e","f","g","h","i","l","m","n","o","p","q","r","s","t","u","v","z","0","1","2","3","4","5","6","7","8","9"};

	int i = 0;
	int j = 0;
	int k = 0;
	int h = 0;
	int l = 0;

	int size = sizeof(lettere)/sizeof(lettere[0]);

	char create[10] = "";

	for (i = 0; i < size; i++) {
    		for (j = 0; j < size; j++) {
    			for (k = 0; k < size; k++) {
    				for (h = 0; h < size; h++) {
    					for (l = 0; l < size; l++) {

    						//costruisco la parola
    						strcpy(create,lettere[i]);
    						strcat(create,lettere[j]);
    						strcat(create,lettere[k]);
    						strcat(create,lettere[h]);
    						strcat(create,lettere[l]);

    						//__android_log_print(ANDROID_LOG_DEBUG, "ANDROTAG", "create : %s ", create);
    						//se e uguale esco
    						if(strcmp(create,secret) == 0){
    							return;
    						}


    					}
    				}
    			}
    		}
		}

}
