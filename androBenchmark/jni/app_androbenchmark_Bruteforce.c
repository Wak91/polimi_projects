#include <jni.h>
#include <string.h>
#include <android/log.h>
#include <stdio.h>
#include <stdlib.h>


JNIEXPORT void JNICALL Java_app_androbenchmark_Bruteforce_pureJni(JNIEnv* env, jclass clazz){

	char range[] = "abcdefghijklmnopqrstuvwxyz0123456789";  //36 elements

	char fword[] = "ciaoo";

		__android_log_write(ANDROID_LOG_INFO, "ANDROBENCHMARK", "I'M HERE BABY");
		int r,i,k,s,l,index,checkpoint;

		int length = strlen(fword);
		char word[length];
		strcpy(word,fword);


		int range_size = strlen(range);
		int attemp[length];
		int iword[length];
	    int find;

		for(i=0;i<length;i++) // let's copy the integer representation of the word
		   {
			   iword[i] = (int)word[i];
		   }

		//let's initialize the attemp with all 'a'

		for(i=0;i<length;i++)
		   {
			   attemp[i] = (int)range[0];
		   }

		r  = test(iword,attemp,length);

		if(r == 1)
		  goto FINE;


		index=length-1;


		while(1)
		     {

				 find=0;
				 for(k=0;k<range_size;k++)
				    {
						attemp[index] = (int)range[k];
						r = test(attemp,iword,length);
						if(r == 1)
						  goto FINE;

					}

				 for(l=index-1;l>=0;l--)
				    {
						if(attemp[l] != ((int)range[35]) )
						  {
							  if(attemp[l]==122)
							     attemp[l]=48;   // this is for the hole between chars and numbers
							  else
							     attemp[l]++;
							  for(s=l+1;s<length;s++)
							      {
									  attemp[s] = (int)range[0];
								  }
							  find = 1;
							  break;
						  }
					}

					if(find!=1)
					  	break;

		     }

		FINE: printf("well,done");


	}

	int test(int *attemp, int* iword , int l)
		{
 			int i;
			for(i=0;i<l;i++)
			   {
				   if(attemp[i] != iword[i])
				      return 0;
				}
		    return 1;

			}

