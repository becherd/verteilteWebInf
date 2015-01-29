package de.tum;

import de.tum.jobs.FromPagesMapper;
import de.tum.jobs.FromPagesReducer;
import de.tum.jobs.CalculateMapper;
import de.tum.jobs.CalculateReducer;
import de.tum.jobs.AuthRankMapper;
import de.tum.jobs.HubRankMapper;
import de.tum.jobs.InitAuthHubMapper;
import de.tum.jobs.InitAuthHubReducer;
import de.tum.parser.DataInputFormat;
import de.tum.parser.DataParserMapper;
import de.tum.parser.DataParserReducer;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

@SuppressWarnings("deprecation") public class Hits {

  private static NumberFormat format = new DecimalFormat("00");
  private static int iterations = 5;

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception {
    Hits hits = new Hits();

    // get outgoing links
    hits.runXmlParsing(args[0], "wiki/linkout");

    // get incoming links
    hits.getLinkIn("wiki/linkout", "wiki/linkin");

    // initialization
    hits.initialize("wiki/linkout", "wiki/linkin", "wiki/HITS/iter00");

    if (args.length == 2) iterations = Integer.parseInt(args[1]);
    int runs = 0;
    for (; runs < iterations; runs++) {
      hits.runHITSCalculation("wiki/HITS/iter" + format.format(runs),
          "wiki/HITS/iter" + format.format(runs + 1));
    }

    hits.runRankOrdering("wiki/HITS/iter" + format.format(runs), "wiki/HITS/result");
  }

  public void runXmlParsing(String inputPath, String outputPath) throws IOException {
    JobConf conf = new JobConf(Hits.class);

    // Input / Mapper
    FileInputFormat.setInputPaths(conf, new Path(inputPath));
    conf.setInputFormat(DataInputFormat.class);
    conf.setMapperClass(DataParserMapper.class);

    // Output / Reducer
    FileOutputFormat.setOutputPath(conf, new Path(outputPath));
    conf.setOutputFormat(TextOutputFormat.class);
    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);
    conf.setReducerClass(DataParserReducer.class);

    JobClient.runJob(conf);
  }

  public void getLinkIn(String inputPath, String outputPath) throws IOException {
    JobConf conf = new JobConf(Hits.class);

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(inputPath));
    FileOutputFormat.setOutputPath(conf, new Path(outputPath));

    conf.setMapperClass(FromPagesMapper.class);
    conf.setReducerClass(FromPagesReducer.class);

    JobClient.runJob(conf);
  }

  private void initialize(String inputPath1, String inputPath2, String outputPath)
      throws IOException {
    JobConf conf = new JobConf(Hits.class);

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.addInputPath(conf, new Path(inputPath1));
    FileInputFormat.addInputPath(conf, new Path(inputPath2));
    FileOutputFormat.setOutputPath(conf, new Path(outputPath));

    conf.setMapperClass(InitAuthHubMapper.class);
    conf.setReducerClass(InitAuthHubReducer.class);

    JobClient.runJob(conf);
  }

  private void runHITSCalculation(String inputPath, String outputPath) throws IOException {
    JobConf conf = new JobConf(Hits.class);

    conf.setOutputKeyClass(Text.class);
    conf.setOutputValueClass(Text.class);

    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(inputPath));
    FileOutputFormat.setOutputPath(conf, new Path(outputPath));

    conf.setMapperClass(CalculateMapper.class);
    conf.setReducerClass(CalculateReducer.class);

    JobClient.runJob(conf);
  }

  private void runRankOrdering(String inputPath, String outputPath) throws IOException {
    JobConf conf = new JobConf(Hits.class);

    conf.setOutputKeyClass(DoubleWritable.class);
    conf.setOutputValueClass(Text.class);
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(inputPath));

    FileOutputFormat.setOutputPath(conf, new Path(outputPath + "/byAuth"));
    conf.setMapperClass(AuthRankMapper.class);
    JobClient.runJob(conf);

    conf = new JobConf(Hits.class);

    conf.setOutputKeyClass(DoubleWritable.class);
    conf.setOutputValueClass(Text.class);
    conf.setInputFormat(TextInputFormat.class);
    conf.setOutputFormat(TextOutputFormat.class);

    FileInputFormat.setInputPaths(conf, new Path(inputPath));

    FileOutputFormat.setOutputPath(conf, new Path(outputPath + "/byHub"));
    conf.setMapperClass(HubRankMapper.class);
    JobClient.runJob(conf);
  }
}
