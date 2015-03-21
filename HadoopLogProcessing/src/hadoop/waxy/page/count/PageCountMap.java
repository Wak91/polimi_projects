package hadoop.waxy.page.count;

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
 * The mappers filter the line in order to understand if it is 
 * a valid GET on a waxy.org page.
 * If yes we are going to put in K2 the date of the request and in v2 'one'.
 * So the reducer are going to receive the 'one' of a specific date and can aggregate them.
 * 
 * */
public class PageCountMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private final static IntWritable one = new IntWritable(1);
	private Text word = new Text();
	
	
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		String line = value.toString();
		String date="";
		
		//TODO here let's perform the filter
		
		StringTokenizer tokenizer = new StringTokenizer(line);
		while (tokenizer.hasMoreTokens()) {
			
			String text="";
			text = tokenizer.nextToken();
			
			if(text.matches("\\[\\d{2}/\\w{3}/\\d{4}.*")){
				
				date = text.substring(1,12);		
			}
			
			//it will be always a GET on the waxy.org page 
			if(text.contains("GET")){
				word.set(date);
				output.collect(word, one);				
			}			
		}
		
		
		
		
		
	}

}
