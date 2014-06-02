#pragma version(1)
#pragma rs java_package_name(app.androbenchmark)

rs_allocation gIn;
rs_allocation gOut;
rs_script gScript;

void root(const uchar4 *v_in, uchar4 *v_out, const void *usrData, uint32_t x, uint32_t y) {
   v_out[x] = v_in[x];
}


void calc() 
{
rsForEach(gScript, gIn, gOut, 0); // first: script; second: input allocation; third:output allocation
}
