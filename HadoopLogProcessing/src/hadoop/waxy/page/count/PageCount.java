package hadoop.waxy.page.count;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/*
 * This mapreduce job will count the Waxy.org total pageviews per day in the entire time range.
 * The final result on hdfs will be 
 * 
 * 1 May 2003 123762
 * 2 May 2003 1239218
 * [ ... ]
 * 
 * */
public class PageCount  extends Configured implements Tool  {

	@Override
	public int run(String[] args) throws Exception {
		
Configuration conf = getConf();
		
		//Creation of a new Job for the HDFS
        JobConf job = new JobConf(conf, PageCount.class);
       
        //----CONFIGURATION OF THE NEW JOB
        
        //Name of the job
        job.setJobName("viewcount");
		
        job.setOutputKeyClass(Text.class);
        
        job.setOutputValueClass(IntWritable.class);
		
        job.setMapperClass(PageCountMap.class);
        
        job.setCombinerClass(PageCountReduce.class);
        
        job.setReducerClass(PageCountReduce.class);
		
        job.setInputFormat(TextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
		
        //where the input will be
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		
		//where you want the output
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		JobClient.runJob(job);
	
		return 0;
	}
	
public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new PageCount(), args);
        System.exit(res);
		
		
		

	}


}
