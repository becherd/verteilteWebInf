package de.tum.benchmark;

import com.yahoo.db.mdbm.MdbmInterface;
import com.yahoo.db.mdbm.MdbmProvider;
import de.tum.TelephoneBookEntry;

/**
 * @author Hannes Dorfmann
 */
public class MdbmBenchmark extends Benchmark<TelephoneBookEntry[]> {
  private MdbmInterface mdbm;

  public MdbmBenchmark() {
    super("Mdbm");
  }

  @Override public void insert(TelephoneBookEntry[] items) throws Exception {

  }

  @Override public void query(TelephoneBookEntry[] items) throws Exception {

  }

  @Override public void delete(TelephoneBookEntry[] items) throws Exception {

  }

  @Override public void cleanUp(TelephoneBookEntry[] items) throws Exception {

    mdbm.close();

  }

  @Override public void setup(TelephoneBookEntry[] items) throws Exception {

    mdbm = MdbmProvider.open("mdbm", 0,0,0,0);

  }
}
