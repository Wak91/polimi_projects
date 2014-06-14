//Da rifare con script group 

#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)

#include "rs_debug.rsh"
#include "rs_math.rsh"

rs_script gScript;

int static matrix [300][300];
int static matrix2[300][300];
int static matrix3[300][300];

void calc() {
  
  int i,j,k,h,s,acc=0;
  for(i=0;i<300;i++)
     for(j=0;j<300;j++)
     {
      matrix[i][j] =  rsRand(20);
      matrix2[i][j] =  rsRand(20);
     }
  
  
  for(k=0;k<300;k++) 
    {  
     for(j=0;j<300;j++)
        { 
         for(i=0;i<300;i++)
            {
              acc+=matrix[k][i] * matrix2[i][j]; 
            }
         matrix3[k][j] = acc;
        }
     }
     

}
