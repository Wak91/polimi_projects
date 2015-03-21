package hadoop.waxy.referring.count;


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
 * (www.google.it , [1,1,1,1,1,1,1,...])
 * (www.yahoo.it , [1,1,1,1,1,...])
 * [ ... ]
 * 
 * it will sum up all the list of '1' and write on the hdfs the final statistic 
 * of how many times a domain act as a referrer in the period analyzed 
 * 
 * www.google.it 7777
 * www.yahooi.it 2324
 * [ ... ]
 * 
 * */
public class RefersCountReduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> { 
	
	
	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {

		int sum=0;
		
		while (values.hasNext()) {
			  values.next(); //to the next element
			  sum++;
		}
		output.collect(key, new IntWritable(sum)); //writing on hdfs 
	}
}
