package hadoop.waxy.referring.day.count;
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

public class ReferringCount extends Configured implements Tool  {

	@Override
	public int run(String[] args) throws Exception {
		
		Configuration conf = getConf();

		//Creation of a new Job for the HDFS
        JobConf job = new JobConf(conf,ReferringCount.class);
       
        //----CONFIGURATION OF THE NEW JOB
        
        //Name of the job
        job.setJobName("referringdaycount");
        job.setInputFormat(TextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        
        
        job.setMapperClass( ReferringCountMap.class);  
        //job.setCombinerClass(ReferringCountReduce.class); //Stupid combiner see http://stackoverflow.com/questions/11021478
        job.setReducerClass( ReferringCountReduce.class);
		
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
    

        //where the input will be
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		
		//where you want the output
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
        //----CONFIGURATION OF THE NEW JOB

		
		
		JobClient.runJob(job);
		
		
		
		return 0;
	}
	
public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new ReferringCount(), args);
        System.exit(res);
		
		
		

	}


}
