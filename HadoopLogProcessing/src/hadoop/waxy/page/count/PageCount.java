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

public class PageCount  extends Configured implements Tool  {

	@Override
	public int run(String[] args) throws Exception {
		
Configuration conf = getConf();
		
		//Creation of a new Job for the HDFS
        JobConf job = new JobConf(conf, PageCount.class);
       
        //----CONFIGURATION OF THE NEW JOB
        
        //Name of the job
        job.setJobName("viewcount");
		
        //Define the class of the key of the reduced tuples
        job.setOutputKeyClass(Text.class);
        
        //Define the class of the value of the reduced tuples 
        job.setOutputValueClass(IntWritable.class);
		
        
        
        //Who is the mapper of this job?
        job.setMapperClass(PageCountMap.class);
        
        //Who is the combiner of this job?
        job.setCombinerClass(PageCountReduce.class);
        
        //Who is the reducer of this job?
        job.setReducerClass(PageCountReduce.class);
		
        /*
         * The input format define how the input file is splitted and readed by Hadoop,
         * in this case the TextInputFormat indicates that each line in the file is a record, and
         the key is the byte offset of the line, and value is the content of the line */
        job.setInputFormat(TextInputFormat.class);
        
        /*This define how you are going to write on the HDFS, in this case 
         * the tuple <V3,K3> is converted toString and separated by a \t 
         * */
        job.setOutputFormat(TextOutputFormat.class);
		
        //where the input will be
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		
		//where you want the output
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
        //----CONFIGURATION OF THE NEW JOB

		
		
		JobClient.runJob(job);
		
		
		
		return 0;
	}
	
public static void main(String[] args) throws Exception {
		
		int res = ToolRunner.run(new Configuration(), new PageCount(), args);
        System.exit(res);
		
		
		

	}


}
