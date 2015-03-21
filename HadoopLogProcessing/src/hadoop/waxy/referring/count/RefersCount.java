package hadoop.waxy.referring.count;
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
 * This job will count the number of referrals per domain (between the 22nd of April and the 30th of May) 
 * 
 * The output on the hdfs will be for example:
 * 
 * www.google.com 5666
 * www.yahoo.it 2344
 * [ ... ] 
 * 
 * */
public class RefersCount extends Configured implements Tool  {

	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();

		//Creation of a new Job for the HDFS
        JobConf job = new JobConf(conf,RefersCount.class);
       
        //----CONFIGURATION OF THE NEW JOB
        
        //Name of the job
        job.setJobName("referscount");
        job.setInputFormat(TextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        
        
        job.setMapperClass( RefersCountMap.class);  
        job.setReducerClass( RefersCountReduce.class);
		
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
    

        //where the input will be
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		
		//where you want the output
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
        //----CONFIGURATION OF THE NEW JOB
	
		JobClient.runJob(job);
			
		return 0;
	}
	
public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new RefersCount(), args);
        System.exit(res);
	

	}


}
