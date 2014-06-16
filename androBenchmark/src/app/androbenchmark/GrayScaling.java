package app.androbenchmark;


import android.graphics.Bitmap;
import android.graphics.Color;


public class GrayScaling {	
	
	private static final String TAG = "GrayScale"; //tag for logcat 
	
	/**
	 * This is a gray scaling of a bitmap in pure java!
	 * @param bm2
	 * @return
	 */
	public static Long pureJava(Bitmap bm2){

		Long t = System.currentTimeMillis();
		
    	int r,g,b , pixel;
    	for(int x=0; x<bm2.getWidth(); x++){
    	   for(int y=0; y<bm2.getHeight(); y++)
    	      {
    		    pixel = bm2.getPixel(x, y);
    		    r = (int) (Color.red(pixel) * 0.299);
    		    g = (int) (Color.green(pixel) * 0.587);
    		    b = (int) (Color.blue(pixel) * 0.114);
    		    //fix immagine verde (i valori r,g,b vanno sommati per ogni componente)
    		    int gray = Color.rgb(r+g+b,r+g+b,r+g+b);
    		    bm2.setPixel(x, y, gray);

    	      }
    	}
    	
    	t = System.currentTimeMillis() - t;
    	
    	return t;
    	
	}
	
	public static Long callPureJni(Bitmap bm){
		
		Long t = System.currentTimeMillis();
		
		pureJni(bm);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	
	
	private native static void pureJni(Bitmap bm);

	

}
