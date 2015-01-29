package de.tum.parser;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.FileSplit;
import org.apache.hadoop.mapred.InputSplit;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.io.Text;

/**
 * @author Hannes Dorfmann
 */
public class DataInputFormat extends TextInputFormat{

  @SuppressWarnings("deprecation")
  public class XMLInputFormat extends TextInputFormat {

    @Override
    public RecordReader<LongWritable,Text> getRecordReader(InputSplit inputSplit, JobConf jobConf,
        Reporter reporter) throws IOException {
      return new LineRecordReader(jobConf, (FileSplit) inputSplit);
    }
  }

}
