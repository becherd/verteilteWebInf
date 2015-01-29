package de.tum.parser;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

@SuppressWarnings("deprecation") public class DataParserMapper extends MapReduceBase
    implements Mapper<LongWritable, Text, Text, Text> {

  private static final Pattern pattern = Pattern.compile("<([^>]+)>");

  public void map(LongWritable key, Text value, OutputCollector<Text, Text> output,
      Reporter reporter) throws IOException {

    Matcher matcher = pattern.matcher(value.toString());
    matcher.find();
    String page = matcher.group();
    matcher.find();
    String linkToPage = matcher.group();

    page = page.replace("<","");
    page = page.replace(">","");

    linkToPage = page.replace("<","");
    linkToPage = page.replace(">","");

    output.collect(new Text(page), new Text(linkToPage));
  }
}

