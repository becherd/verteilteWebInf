package de.tum.jobs;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class InitAuthHubReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
    
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        String linksInOut = "";

        boolean hasIncomingLinks = false;
        while(values.hasNext()){
            String links = values.next().toString();
            
            if (links.startsWith("|")) {	// get incoming links
            	hasIncomingLinks = true;
            	linksInOut += "\t" + links.substring(1);
            	continue;
            }
            else {							// get outgoing links
            	linksInOut = links + linksInOut;
            }
        }
        
        if (!hasIncomingLinks)
        	return;
        
        // output: <Page\tAuth\tHub>, <Out-List\tIn-List>
        output.collect(key, new Text(linksInOut));
    }
}