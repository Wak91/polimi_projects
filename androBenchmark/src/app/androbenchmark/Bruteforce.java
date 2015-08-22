package app.androbenchmark;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;

public class Bruteforce {
	
// ----------------------------- BENCHMARK CORE ----------------------------- //
	
	private static int length;
	
	private static final String range= "abcdefghijklmnopqrstuvwxyz0123456789";
	
	private static int attemp[];
	private static int iword[];

	private static String word;

	
	private static void pureJava(String sword){

		int r=0,i,k,s,l,index;
		
		length = sword.length();
		word = new String();
		
		word = sword;

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
	
	private native static void pureJni(String sword);
	
	private static void pureRenderScript(RenderScript rs, ScriptC_brute script, String sword){
		
		
		script.invoke_brute();
		rs.finish();
								
	}
	
// ----------------------------- END BENCHMARK CORE ----------------------------- //
	
// ----------------------------- SETUP BENCHMARK ----------------------------- //
	
	public static Long callPureJava(String sword){
		
		Long t = System.currentTimeMillis();
		
		pureJava(sword);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}


	public static Long callPureJni(String sword){
		
		Long t = System.currentTimeMillis();
		
		pureJni(sword);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	public static Long callPureRenderScript(String sword, Context context){
		
		
		RenderScript rs = RenderScript.create(context);
	    ScriptC_brute script = new ScriptC_brute(rs,context.getResources(),R.raw.brute);
		 
		 
	    int dim = sword.length();
		 
	    script.set_dim(dim);
		 
	    Allocation word = Allocation.createFromString(rs, sword,Allocation.USAGE_SCRIPT );
	    script.bind_word(word);
		
		Long t = System.currentTimeMillis();
		
		pureRenderScript(rs, script,sword);
		
		t = System.currentTimeMillis() - t;
    	
    	return t;
					
	}
	
	// ----------------------------- BATTERY STRESS  ----------------------------- //
	
		public static Integer stressJavaBattery(String sword , Context c) // need the context to register the receiver 
		{
			int l_diff=0;
		    int l_before=0;
		    int cont=0;
			 //Stress battery with Java 
		     l_before = getLevel(c);
			 
		     do
			    {
		    	 cont++;
		    	 pureJava(sword); 
		    	 l_diff = l_before - getLevel(c);		     
			    } while (l_diff<1); // quando la batteria si è scaricata di un livello
		     
		     return cont;
			     
		}
		
		public static Integer stressJNIBattery(String sword , Context c) // need the context to register the receiver 
		{ 
			 int l_diff=0;
			 int l_before=0;
			 int cont=0;
			 
			 l_before = getLevel(c);
				 
			     do
				    {
			    	 cont++;
			    	 pureJni(sword); 
			    	 l_diff = l_before - getLevel(c);		     
				    } while (l_diff<1); // quando la batteria si è scaricata di un livello
			     
			     return cont; 
		}	 
		
		public static Integer stressRSBattery(String sword , Context c) // need the context to register the receiver 
		{
			 //Stress battery with RS
			 
			 //Prepare renderscript here, in order to mantain the context during the stress 
			int l_before=0;
			int l_diff=0;
			int cont=0;
			
			 RenderScript rs = RenderScript.create(c);
			 ScriptC_brute script = new ScriptC_brute(rs,c.getResources(),R.raw.brute);
				 
				 
			 int dim = sword.length();
				 
			 script.set_dim(dim);
				 
			 Allocation word = Allocation.createFromString(rs, sword,Allocation.USAGE_SCRIPT );
			 script.bind_word(word);
			
			 l_before = getLevel(c);
			
			 do
			    {
				    cont++;
					script.invoke_brute();
					rs.finish();	
					l_diff = l_before - getLevel(c);
		        }while(l_diff<1);
			 
			 
		     rs.destroy();
			 
		     return cont;
		}
		

		/**
		 * Funzione che ritorna il valore in mvolt della batteria 
		 * @return
		 */
		private static int getLevel(Context context)
		{
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		    Intent b = context.registerReceiver(null, ifilter);
		    int lev = b.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);    
		    return lev;	
		}
	
	
	
	

}
