package hadoop.waxy.video.count;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/*
 * Waxy.org total pageviews per day in the entire time range 
 * 
 * This mapper filter the lines where there is a download of the video and when it found
 * a valid line it will send to the reducer:
 * 
 * <(1 May 2003), 1>
 * <(2 May 2003), 1>
 * [ ... ]
 * 
 * */
public class VideoCountMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		String line = value.toString();
		String date="";
				
		StringTokenizer tokenizer = new StringTokenizer(line);
		while (tokenizer.hasMoreTokens()) {
			
			String text="";
			text = tokenizer.nextToken();
			
			if(text.matches("\\[\\d{2}/\\w{3}/\\d{4}.*")){
				
				date = text.substring(1,12);		
			}
			
			//check if the line contains a .wmv
			if(text.contains(".wmv")){
				word.set(date);
				output.collect(word, one);				
			}			
		}
	}
}
