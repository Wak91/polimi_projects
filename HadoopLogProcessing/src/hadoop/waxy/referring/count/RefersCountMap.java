package hadoop.waxy.referring.count;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * The mappers filter the lines in order to understand if it is 
 * in the dates range prefixed and if it is a valid referring domains.
 * If it is, it send to the reducers the pairs:
 * 
 * ( www.google.it 1 )
 * ( www.yahoo.it 1  )
 * [ ... ]
 * */
public class RefersCountMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable> {

	private Text word2 = new Text(); //text in order to store the referring domain 
	private final static IntWritable one = new IntWritable(1);

	
	
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, IntWritable> output, Reporter reporter) throws IOException {
		
		String line = value.toString();
		DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH);
		int error=0;
		
		Date begin=null;
		Date end=null;
		
		//Let's create the begin and the end date in 
		//order to filter the data in this range
		
		try {
			begin = formatter.parse("22/Apr/2003");
			end = formatter.parse("30/May/2003");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StringTokenizer tokenizer = new StringTokenizer(line);
		while (tokenizer.hasMoreTokens()) {
			
			String text="";
			text = tokenizer.nextToken();
			
			//if the actual token match a date
			if(text.matches("\\[\\d{2}/\\w{3}/\\d{4}.*")){
						
				try {
					Date data = formatter.parse(text.substring(1,12));
					
					if(data.before(begin) || data.after(end)){
						error=1; //if the date isn't in the prefixed range signal an error to prevent this log to be sent to the reducers
								 //otherwise error will be 0 and we will send it to them
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
			
			
			//if the actual token contain a referring different from waxy, in the dates range prefixed 
			if( (text.contains("http://") || text.contains("https://")) && (!text.contains("waxy.org")) && (error==0) ){
												
				Pattern pat = Pattern.compile("\\b" + "(http://|https://)(\\w|\\.)+" + "\\b"); //remove some junk and extract the domanin in a cleaner format
				
				Matcher mat = pat.matcher(text);
				
				while(mat.find()){
					
					String s = mat.group();
					String [] a  = s.split("//"); //removing the 'http://'
					word2.set(a[a.length-1]);
				}
								
				output.collect(word2, one);	//send a '1' for this domain to the reducers 
			}			
		}
			
	}

}
