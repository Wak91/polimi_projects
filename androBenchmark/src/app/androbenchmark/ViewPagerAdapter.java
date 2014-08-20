package app.androbenchmark;

import java.util.HashMap;
import java.util.List;

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
    Context context;
    GraphActivity activity;
    LayoutInflater inflater;
    HashMap<String, List<Integer>> result;
 
    public ViewPagerAdapter(GraphActivity context, HashMap<String, List<Integer>> result) {
        this.context = context;
        this.activity = context;
        this.result = result;
       
    }
    
    @Override
    public CharSequence getPageTitle(int position) {
      return ViewPagerAdapter.TITLE[position % TITLE.length];
    }
 
    @Override
    public int getCount() {
    	//abbiamo dimensione fissa per adesso(da modifivare in futuro)
        return 2;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
 
       
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        //schiantiamo la slide dentro il graph layout
        View itemView = inflater.inflate(R.layout.graph, container,false);
 
        //scaliamo il grafico in modo appropriato
        Integer max = this.activity.find_max(result.get("java"));
        Integer maxc = this.activity.find_max(result.get("battery"));
        //disegniamo il grafico performance 
        //this.activity.drawPlot(max.intValue(), result, itemView);
 
        //disegniamo il grafico dei consumi
        this.activity.drawBatteryPlot(maxc.intValue(), result,itemView);

        
        // mettiamola all interno del ViewPager
        ((ViewPager) container).addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       
        ((ViewPager) container).removeView((LinearLayout) object);
 
    }
    
    
}