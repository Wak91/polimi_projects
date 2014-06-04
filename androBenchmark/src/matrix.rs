#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

int fix=1;

void root(const int32_t *v_in, int32_t *v_out) {
   
   *v_out = *v_in + fix;
}


void calc() 
{
rsForEach(gScript, gIn, gOut, 0); // first: script; second: input allocation; third:output allocation
}
