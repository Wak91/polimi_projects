package hadoop.waxy.video.count;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

/*
 * The reducers will receive:
 * 
 * <(1 May 2003), (1,1,1,1,1,1,[...])>
 * <(2 May 2003), (1,1,1,1,1,1,[...])>
 * [ ... ]
 * 
 * It basically aggregate the list and write on hdfs:
 * 
 * 1 May 2003 3456
 * 2 May 2003 2341
 * [ ... ]
 * 
 * */
public class VideoCountReduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable> { 
	
	@Override
	public void reduce(Text key, Iterator<IntWritable> values, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		int sum = 0;
		while (values.hasNext()) {
			sum += values.next().get();
		}
		output.collect(key, new IntWritable(sum));
	}
	
	
	

}
