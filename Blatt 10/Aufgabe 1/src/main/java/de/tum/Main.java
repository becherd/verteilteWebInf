package de.tum;

import de.tum.benchmark.Benchmark;
import de.tum.benchmark.HashMapBenchmark;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * The main class to start the java app
 *
 * @author Hannes Dorfmann
 */
public class Main {

  public static void main(String[] args) throws Exception {

    int count = 5000000;

    TelephoneBookEntry[] telephoneBookEntries = new TelephoneBookEntry[count];

    // Generate random persons
    for (int i = 0; i < count; i++) {
      telephoneBookEntries[i] = new TelephoneBookEntry(i + "Name", RandomStringUtils.randomNumeric(10));
    }

    // test java map
    Benchmark<TelephoneBookEntry[]> mapBenchmark = new HashMapBenchmark().start(
        telephoneBookEntries);
  }
}
