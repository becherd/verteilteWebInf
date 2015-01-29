package de.tum.jobs;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation") public class AuthRankMapper extends MapReduceBase
    implements Mapper<LongWritable, Text, DoubleWritable, Text> {

  public void map(LongWritable key, Text value, OutputCollector<DoubleWritable, Text> output,
      Reporter arg3) throws IOException {
    int tabPageIndex = value.find("\t");
    int tabAuthIndex = value.find("\t", tabPageIndex + 1);

    String page = Text.decode(value.getBytes(), 0, tabPageIndex);
    String auth =
        Text.decode(value.getBytes(), tabPageIndex + 1, tabAuthIndex - (tabPageIndex + 1));

    output.collect(new DoubleWritable(Double.parseDouble(auth)), new Text(page));
  }
}
