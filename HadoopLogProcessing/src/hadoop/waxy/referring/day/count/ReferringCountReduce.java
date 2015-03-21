package hadoop.waxy.referring.day.count;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class ReferringCountReduce extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> { 
	
	
	@Override
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		ArrayList <String> domains = new ArrayList <String>();
		
		while (values.hasNext()) {
		
			String temp = values.next().toString();
			
			if(!domains.contains(temp)){
				domains.add(temp);
			}

		}
		output.collect(key, new IntWritable(domains.size())); //that is right with an IntWritable 
	}
}
