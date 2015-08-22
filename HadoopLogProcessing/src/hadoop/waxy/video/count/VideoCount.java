package hadoop.waxy.video.count;

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
 * This job will count the Video downloads (wmv files) per day in the entire time range. (Aggregating the two video versions - normal and remixed) 
 * It will write on the hdfs:
 * 
 * 1 May 2003 6363 ( where 6363 is the total number of video's download in that day ) 
 * 2 May 2003 2311
 * [ ... ] 
 * 
 * */
public class VideoCount  extends Configured implements Tool  {

	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();
		
		//Creation of a new Job for the HDFS
        JobConf job = new JobConf(conf,VideoCount.class);
           
        //Name of the job
        job.setJobName("videocount");
		
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
		
        job.setMapperClass(VideoCountMap.class);
        job.setCombinerClass(VideoCountReduce.class);        
        job.setReducerClass(VideoCountReduce.class);
		

        job.setInputFormat(TextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
		
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		
		JobClient.runJob(job);
		
		return 0;
	}
	
public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new VideoCount(), args);
        System.exit(res);		
	}
}
