package app.androbenchmark;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class GrayScaling {	
	
	/**
	 * This is a gray scaling of a bitmap in pure java!
	 * @param bm2
	 * @return
	 */
	public static Bitmap pureJava(Bitmap bm2){

    	int r,g,b,a , pixel;
    	for(int x=0; x<bm2.getWidth(); x++){
    	   for(int y=0; y<bm2.getHeight(); y++)
    	      {
    		    pixel = bm2.getPixel(x, y);
    		    r = (int) (Color.red(pixel) * 0.299f);
    		    g = (int) (Color.green(pixel) * 0.587f);
    		    b = (int) (Color.blue(pixel) * 0.114f);
    		    
    		    int gray = Color.rgb(r, g, b);
    		    bm2.setPixel(x, y, gray);

    	      }
    	}
    	
    	return bm2;
    	
	}

}
