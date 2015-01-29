package de.tum.jobs;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation")
public class CalculateReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {

    public void reduce(Text page, Iterator<Text> values, OutputCollector<Text, Text> out, Reporter reporter) throws IOException {
    	double auth = 0.0F;
    	double hub  = 0.0F;
    	double normAuth = 0.0F;
    	double normHub  = 0.0F;
    	double tmpValue = 0.0F;
        String linksInOut = "";
        String valueString = null;

        while(values.hasNext()){
            valueString = values.next().toString();
            
            // value is hub
            if(valueString.startsWith("<H>")) {
            	tmpValue = Double.parseDouble(valueString.substring(3));
                auth += tmpValue;
                normAuth += tmpValue * tmpValue;
                continue;
            }
            
            // value is authority
            if(valueString.startsWith("<A>")){
            	tmpValue = Double.parseDouble(valueString.substring(3));
                hub += tmpValue;
                normHub += tmpValue * tmpValue;
                continue;
            }
            
            // value is in-list
            if(valueString.startsWith("|")){
                linksInOut += "\t" + valueString.substring(1);
                continue;
            }
            
            // else: value is out-list
            linksInOut = valueString + linksInOut;
        }
        
        if (linksInOut.split("\t").length < 2)
        	return;
        
        auth = auth / Math.sqrt(normAuth);
        hub  = hub  / Math.sqrt(normHub);
        if (Double.isNaN(auth))
        	auth = 0.0;
        if (Double.isNaN(hub))
        	hub = 0.0;

        out.collect(new Text(page.toString()+"\t"+auth+"\t"+hub), new Text(linksInOut));
    }
}
