#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)
#include "rs_debug.rsh"


rs_script gScript;
int dim;
char* word;

char static range[] = "abcdefghijklmnopqrstuvwxyz0123456789";  //36 elements 

int static test(int *attemp, int* iword , int l)
{
	int i;
	for(i=0;i<l;i++)
	   {
		   if(attemp[i] != iword[i])
		      return 0;
		}	
		return 1;
	}

void brute()
{	
	 int32_t i,k,s,l,index;
	
	 int32_t length = dim;
	
	 int32_t range_size = 36;
	 int32_t attemp[dim];
	 int32_t iword[dim];
     int32_t find;
	
	for(i=0;i<length;i++) // let's copy the integer representation of the word 
	   {
		   iword[i] = (int)word[i];
		
	   }
	   
	//let's initialize the attemp with all 'a'
	
	for(i=0;i<length;i++)
	   {
		   attemp[i] = (int)range[0];
	   }   
	
		int r = test(iword,attemp,length);
		
		if(r == 1)
		  goto FINE;
 
	
	index=length-1;

	
	while(1)
	     {
			 
			 find=0;
			 for(k=0;k<range_size;k++)
			    {
					attemp[index] = (int)range[k];
					for(i=0;i<l;i++)
	   	
	   r =	test(iword,attemp,length);
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
	     
	FINE:   
	rsDebug("Ciao", "c");    
}
