package hadoop.waxy.page.count;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/*
 * Reducing the counter relative to a date
 * 
 * i.e.
 * 
 * <(30/12/2001),list(1,1,1,1,1,1,1,1,1,1,1,1,1>
 * 
 * we are going to write on HDFS 
 * 
 * 30/12/2001 13
 * [ ... ]
 * 
 * */
public class PageCountReduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> { 
	
	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		int sum = 0;
		while (values.hasNext()) {
			sum += values.next().get();
		}
		output.collect(key, new IntWritable(sum));
	}
	
	
	

}
