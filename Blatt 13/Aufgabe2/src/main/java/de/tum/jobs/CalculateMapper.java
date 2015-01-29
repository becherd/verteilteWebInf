package de.tum.jobs;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class CalculateMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text>{

    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        int pageTabIndex = value.find("\t");
        int authTabIndex = value.find("\t", pageTabIndex+1);
        int hubTabIndex  = value.find("\t", authTabIndex+1);

        String page = Text.decode(value.getBytes(), 0, pageTabIndex);
        String auth = Text.decode(value.getBytes(), pageTabIndex+1, authTabIndex-(pageTabIndex+1));
        String hub  = Text.decode(value.getBytes(), authTabIndex+1,  hubTabIndex-(authTabIndex+1));
        
        String[] linksInOut = Text.decode(value.getBytes(), hubTabIndex+1, value.getLength()-(hubTabIndex+1)).split("\t");
        if (linksInOut.length < 2)
        	return;
        
        // for each page in out-list
        String[] outgoingLinks = linksInOut[0].split(",");
        for (String outLink : outgoingLinks) {
        	// Hub starts with "<H>"
        	output.collect(new Text(outLink), new Text("<H>"+hub));
        }
        output.collect(new Text(page), new Text(linksInOut[0]));
        
        // for each page in in-list
        String[] incomingLinks = linksInOut[1].split(",");
        for (String inLink : incomingLinks){
        	// Authority starts with "<A>"
        	output.collect(new Text(inLink), new Text("<A>"+auth));
        }
        // in-list starts with "|"
    	output.collect(new Text(page), new Text("|"+linksInOut[1]));
        
    }
}
