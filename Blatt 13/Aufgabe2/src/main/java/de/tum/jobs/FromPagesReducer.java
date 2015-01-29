package de.tum.jobs;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class FromPagesReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
    
    public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {
        String linksFromPages = "|";

        boolean isExistingPage = false;
        boolean hasIncomingLinks = false;
        boolean first = true;
        while(values.hasNext()) {
        	String page = values.next().toString();
        	
        	if (page.equals("!")) {
        		isExistingPage = true;
        		continue;
        	}
        	
        	if (!first)
            	linksFromPages += ",";
        	
            linksFromPages += page;
            
            hasIncomingLinks = true;
            first = false;
        }
        
        if (!isExistingPage)
        	return;
        
        if (!hasIncomingLinks)
        	return;
        
        // output: < page, links from other pages(incoming links) >
        output.collect(key, new Text(linksFromPages));
    }
}
