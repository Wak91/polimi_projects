//Da rifare con script group 

#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)

#include "rs_debug.rsh"
#include "rs_math.rsh"

rs_script gScript;

int dim;



void calc() {

int d = dim;  
int matrix [d][d];
int matrix2[d][d];
int matrix3[d][d];
  
  int i,j,k,h,s,acc=0;
  for(i=0;i<d;i++)
     for(j=0;j<d;j++)
     {
      matrix[i][j] =  rsRand(20);
      matrix2[i][j] =  rsRand(20);
     }
  
  
  for(k=0;k<d;k++) 
    {  
     for(j=0;j<d;j++)
        { 
         for(i=0;i<d;i++)
            {
              acc+=matrix[k][i] * matrix2[i][j]; 
            }
         matrix3[k][j] = acc;
        }
     }
     

}
