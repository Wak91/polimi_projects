package app.androbenchmark;

import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


 
public class ViewPagerAdapter extends PagerAdapter {
	
	protected static final String[] TITLE = new String[] { "Performance", "Consumption"};
	
    // Declare Variables
	private Context context;
	private GraphActivity activity;
	private LayoutInflater inflater;
	private HashMap<String, List<Integer>> result;
    
    private int stressMode;
 
    public ViewPagerAdapter(GraphActivity context, HashMap<String, List<Integer>> result, int stress) {
        this.context = context;
        this.activity = context;
        this.result = result;
        this.stressMode = stress;
       
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
      return ViewPagerAdapter.TITLE[position % TITLE.length];
    }
 
    @Override
    public int getCount() {
    	if (this.stressMode == 0){
    		return 1;
    	}
    	else{
    		 return 2;
    	}
       
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
 
       
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        
 
        //scaliamo il grafico in modo appropriato
        if (position == 0){ //TODO ==0
        	
        	//schiantiamo la slide dentro il graph layout
            View itemView = inflater.inflate(R.layout.graph, container,false);
        	
        	 Integer max = this.activity.find_max(result.get("java"));
        	 
        	 this.activity.drawPlot(max.intValue(), result, itemView);
        	 
        	 // mettiamola all interno del ViewPager
             ((ViewPager) container).addView(itemView);
             
             return itemView;
        }
        else {
        	
        	//schiantiamo la slide dentro il graph layout
            View itemView = inflater.inflate(R.layout.bgraph, container,false);
            
        	Integer maxc = this.activity.find_max(result.get("battery"));
        	
        	this.activity.drawBatteryPlot(maxc.intValue(), result,itemView);
        	
        	 // mettiamola all interno del ViewPager
            ((ViewPager) container).addView(itemView);
            
            return itemView;
        	
		}
		
   
 
       
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       
        ((ViewPager) container).removeView((LinearLayout) object);
 
    }
    
    
}