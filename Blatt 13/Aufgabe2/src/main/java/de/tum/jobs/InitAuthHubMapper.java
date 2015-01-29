package de.tum.jobs;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation") public class InitAuthHubMapper extends MapReduceBase
    implements Mapper<LongWritable, Text, Text, Text> {

  public void map(LongWritable key, Text value, OutputCollector<Text, Text> output,
      Reporter reporter) throws IOException {

    int pageTabIndex = value.find("\t");
    String page = Text.decode(value.getBytes(), 0, pageTabIndex);
    String links =
        Text.decode(value.getBytes(), pageTabIndex + 1, value.getLength() - (pageTabIndex + 1));

    // Initialization: <page,auth=1.0,hub=1.0 incoming-links/outgoing-links>
    output.collect(new Text(page + "\t1.0\t1.0"), new Text(links));
  }
}
