package app.androbenchmark;

import android.content.Context;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class Bruteforce {
	
// ----------------------------- BENCHMARK CORE ----------------------------- //
	
	private static final String SECRET= "ciaoo";
	private static int length;
	
	private static final String range= "abcdefghijklmnopqrstuvwxyz0123456789";
	
	private static int attemp[];
	private static int iword[];

	private static String word;

	
	
	private static void pureJava(){

		
		
		int r=0,i,k,s,l,index;
		
		length = SECRET.length();
		word = new String();
		
		word = SECRET;

		int range_size = range.length();
		attemp = new int[length];
		iword = new int [length];
		
	    int find;

		for(i=0;i<length;i++) // let's copy the integer representation of the word
		   {
			   iword[i] = (int)(word.charAt(i));
		   }

		//let's initialize the attemp with all 'a'

		for(i=0;i<length;i++)
		   {
			   attemp[i] = (int)range.charAt(i);
		   }


		r = test();
		
		index=length-1;

		while(r==0)
		     {
				 find=0;
				 for(k=0;k<range_size;k++)
				    {
						attemp[index] = (int)range.charAt(k);
						r = test();

					}
				 
				 if(r==0){ // if I don't find the word!
	 
				 for(l=index-1;l>=0;l--)
				    {
						if(attemp[l] != ((int)range.charAt(35)) )
						  {
							  if(attemp[l]==122)
							     attemp[l]=48;   // this is for the hole between chars and numbers
							  else
							     attemp[l]++;
							  for(s=l+1;s<length;s++)
							      {
									  attemp[s] = (int)range.charAt(0);
								  }
							  find = 1;
							  break; // stop to search behind for a char to scroll
						  }
					}
				 

					if(find!=1) // If the word is finish ( this can't happen )
					  	break;
				 }
		     } //closed while
		
		 
	}
	
	static int test()
	{
	    int i;
		for(i=0;i<length;i++)
		   {
			   if(attemp[i] != iword[i])
			      return 0;
			}
	    
        return 1;
		}
	
	private native static void pureJni();
	
	private static void pureRenderScript(RenderScript rs, ScriptC_brute script){
		
		script.invoke_brute();
		rs.finish();
								
	}
	
// ----------------------------- END BENCHMARK CORE ----------------------------- //
	
// ----------------------------- SETUP BENCHMARK ----------------------------- //
	
	public static Long callPureJava(){
		
		Long t = System.currentTimeMillis();
		
		pureJava();
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}


	public static Long callPureJni(){
		
		Long t = System.currentTimeMillis();
		
		pureJni();
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	public static Long callPureRenderScript(MainActivity activity){
		
		Context context = activity.getBaseContext();
		
		RenderScript rs = RenderScript.create(context);
	    ScriptC_brute script = new ScriptC_brute(rs,context.getResources(),R.raw.brute);
		 
	    String s1 = new String("ciaoo");
		 
	    int dim = s1.length();
		 
	    script.set_dim(dim);
		 
	    Allocation word = Allocation.createFromString(rs, s1,Allocation.USAGE_SCRIPT );
	    script.bind_word(word);
		
		Long t = System.currentTimeMillis();
		
		pureRenderScript(rs, script);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	
	
	
	

}
