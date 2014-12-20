package de.tum.benchmark;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import de.tum.TelephoneBookEntry;

/**
 * @author Hannes Dorfmann
 */
public class MongoBenchmark extends Benchmark<TelephoneBookEntry[]> {

  private MongoClient client;
  private DB db;
  private DBCollection telephoneBook;

  private final String COL_NAME = "name";
  private final String COL_NUMBER = "number";

  public MongoBenchmark() {
    super("MongoDB");
  }

  @Override public void insert(TelephoneBookEntry[] items) throws Exception {

    for (TelephoneBookEntry e : items) {
      BasicDBObject o = new BasicDBObject();
      o.put(COL_NAME, e.getName());
      o.put(COL_NUMBER, e.getNumber());
      telephoneBook.insert(o);
    }
  }

  @Override public void query(TelephoneBookEntry[] items) throws Exception {

    for (TelephoneBookEntry e : items) {
      DBObject found = telephoneBook.findOne(new BasicDBObject(COL_NAME, e.getName()));
      if (found == null) {
        throw new NullPointerException("No Person with name = " + e.getName() + " found");
      }
    }
  }

  @Override public void delete(TelephoneBookEntry[] items) throws Exception {

    for (TelephoneBookEntry e : items) {
      telephoneBook.remove(new BasicDBObject(COL_NAME, e.getName()));
    }
  }

  @Override public void cleanUp(TelephoneBookEntry[] items) throws Exception {
    telephoneBook.drop();
    client.close();
  }

  @Override public void setup(TelephoneBookEntry[] items) throws Exception {

    client = new MongoClient("localhost", 27017);
    db = client.getDB("bechmark-telephonebook");
    telephoneBook = db.getCollection("TelephoneBook");
    telephoneBook.createIndex(new BasicDBObject(COL_NAME, 1));
  }
}
