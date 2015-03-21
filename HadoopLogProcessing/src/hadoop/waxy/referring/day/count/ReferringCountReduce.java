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

/*
 * The reducer will receive:
 * 
 * <(1 May 2003) , (www.google.it,www.yahoo.it,www.google.it,...)> 
 * [ ... ]
 * 
 * It will for every block call this function and scan the list filling the ArrayList.
 * If it is a domain that we have encointered yet, it won't add it again to the ArrayList otherwise
 * it will add the new domain to it.
 * 
 * The final result written on the hdfs will be:
 * 
 * 1 May 2003  3213 ( where 3213 it is basically the size of the ArrayList ) 
 * 2 May 2003  1234
 * [ ... ]
 * 
 * */
public class ReferringCountReduce extends MapReduceBase implements Reducer<Text, Text, Text, IntWritable> { 
	
	
	@Override
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		ArrayList <String> domains = new ArrayList <String>();
		
		while (values.hasNext()) {
		
			String temp = values.next().toString(); // conver to string the value
			
			if(!domains.contains(temp)){  
				domains.add(temp);
			}

		}
		output.collect(key, new IntWritable(domains.size())); 
	}
}
