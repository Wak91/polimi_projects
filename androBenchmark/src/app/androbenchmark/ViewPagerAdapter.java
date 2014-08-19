package app.androbenchmark;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
 
public class ViewPagerAdapter extends PagerAdapter {
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
    public int getCount() {
        return 2;
    }
 
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
 
        
 
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        View itemView = inflater.inflate(R.layout.graph, container,false);
 
        //scaliamo il grafico in modo appropriato
        Integer max = this.activity.find_max(result.get("java"));
      		
        //disegniamo il grafico
        this.activity.drawPlot(max.intValue(), result, itemView);
 
        // Add viewpager_item.xml to ViewPager
        ((ViewPager) container).addView(itemView);
 
        return itemView;
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // Remove viewpager_item.xml from ViewPager
        ((ViewPager) container).removeView((LinearLayout) object);
 
    }
    
    
}