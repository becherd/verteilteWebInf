package de.tum;

import de.tum.benchmark.HashMapBenchmark;
import de.tum.benchmark.MongoBenchmark;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * The main class to start the java app
 *
 * @author Hannes Dorfmann
 */
public class Main {

  public static void main(String[] args) throws Exception {

    int count = 5000000;

    TelephoneBookEntry[] entries = new TelephoneBookEntry[count];

    // Generate random persons
    for (int i = 0; i < count; i++) {
      entries[i] =
          new TelephoneBookEntry(i + "Name", RandomStringUtils.randomNumeric(10));
    }

    // java map
    new HashMapBenchmark().start(entries);

    // MongoDb
    new MongoBenchmark().start(entries);

    // Mdbm
    //new MdbmBenchmark().start(entries);
  }
}
