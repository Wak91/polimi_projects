package app.androbenchmark;


import java.lang.reflect.InvocationTargetException;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public  class ExecuteBenchmarkTask extends AsyncTask <Bitmap, Void, Bitmap> {

	@Override
	protected Bitmap doInBackground(Bitmap... params) {
		return GrayScaling.pureJava(params[0]);
	}

	/*
	@Override
	protected Object doInBackground(Object... params) {
		// TODO Auto-generated method stub
		try {
			return params[0].getClass().getMethod((String)params[1]).invoke(params[0]);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	*/
	
	

}
