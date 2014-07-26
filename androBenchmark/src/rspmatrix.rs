//Da rifare con script group 

#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)

#include "rs_debug.rsh"
#include "rs_math.rsh"

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

void root(const uchar4 *v_in, uchar4 *v_out) {

*v_out = (*v_in) * (*v_out);

}


void calc() 
{
rsForEach(gScript, gIn, gOut, 0); // first: script; second: input allocation; third:output allocation
}