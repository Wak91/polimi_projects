#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)

#include "rs_matrix.rsh"
#include "rs_debug.rsh"


rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

rs_matrix4x4 matrix1;
rs_matrix4x4 matrix2;


void hello() {

   rsDebug("First", "c");
   
      rsDebug("Printing out matrix1", &matrix1);
      rsDebug("Printing out matrix1", &matrix2);
   
   rsMatrixMultiply(&matrix1,&matrix2); // place the result in matrix1
   rsDebug("Printing out matrix1", &matrix1);
   
}
