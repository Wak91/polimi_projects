package hadoop.waxy.referring.day.count;


import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class ReferringCountMap extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	private Text word = new Text(); //text in order to store the date 
	private Text word2 = new Text(); //text in order to store the referring domain 
	
	
	@Override
	public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
		
		String line = value.toString();
		String date="";
		DateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH);
		int error=0;
		
		Date begin=null;
		Date end=null;
		
		try {
			begin = formatter.parse("22/Apr/2003");
			end = formatter.parse("30/May/2003");
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		//TODO here let's perform the filter
		
		StringTokenizer tokenizer = new StringTokenizer(line);
		while (tokenizer.hasMoreTokens()) {
			
			String text="";
			text = tokenizer.nextToken();
			
			if(text.matches("\\[\\d{2}/\\w{3}/\\d{4}.*")){
				
				
				try {
					Date data = formatter.parse(text.substring(1,12));
					
					if(data.before(begin) || data.after(end)){
						error=1;
					}
					else{
						date = text.substring(1,12);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				date = text.substring(1,12);

				
				
			}
			
			
			
			//it will be always a GET on the waxy.org page 
			if( (text.contains("http://") || text.contains("https://")) && (!text.contains("waxy.org")) && (error==0) ){
								
				word.set(date);
				//word2.set(text);
				
				//UNTILL HERE ALL RIGHT

				
				Pattern pat = Pattern.compile("\\b" + "(http://|https://)(\\w|\\.)+" + "\\b");
				
				Matcher mat = pat.matcher(text);
				
				while(mat.find()){
					
					String s = mat.group();
					String [] a  = s.split("//");
					word2.set(a[a.length-1]);
				}
				
							
				output.collect(word, word2);				
			}			
		}
			
	}

}
