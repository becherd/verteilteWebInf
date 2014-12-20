package de.tum.benchmark;

import de.tum.TelephoneBookEntry;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hannes Dorfmann
 */
public class HashMapBenchmark extends Benchmark<TelephoneBookEntry[]> {

  private Map<String, String> telephoneBook;

  public HashMapBenchmark() {
    super("HashMap");
  }

  @Override public void insert(TelephoneBookEntry[] items) throws Exception {
    for (TelephoneBookEntry p : items) {
      telephoneBook.put(p.getName(), p.getName());
    }
  }

  @Override public void query(TelephoneBookEntry[] items) throws Exception {
    for (TelephoneBookEntry p : items) {
      telephoneBook.get(p.getName());
    }
  }

  @Override public void delete(TelephoneBookEntry[] items) throws Exception {
    for (TelephoneBookEntry p : items) {
      telephoneBook.remove(p.getName());
    }
  }

  @Override public void cleanUp(TelephoneBookEntry[] items) throws Exception {
    telephoneBook.clear();
    telephoneBook = null;
  }

  @Override public void setup(TelephoneBookEntry[] items) throws Exception {
    telephoneBook = new HashMap<>();
  }
}
