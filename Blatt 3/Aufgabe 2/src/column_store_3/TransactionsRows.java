package column_store_3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionsRows {
  /**
   * Sort the rows according timestamp.
   * It's some kind of simple database index

  private static class TreeComparator implements Comparator<Row> {
    @Override public int compare(Row o1, Row o2) {
      if (o1.getTimestamp() < o2.getTimestamp()) {
        return 1;
      }
      if (o1.getTimestamp() > o2.getTimestamp()) {
        return -1;
      }
      return 0; // equal
    }
  }
   */

  //TreeSet<Row> rows;
  ArrayList<Row> rows;

  public TransactionsRows() {
    rows = new ArrayList<Row>();
    //rows = new TreeSet<>(new TreeComparator());
  }

  public void add(Row row) {
    rows.add(row);
  }

  public int getAnzahlLetzteNTage(int tage) {
    int anz = 0;
    //jetzt berechnen
    long unixTime = System.currentTimeMillis() / 1000L;
    long timestamp = tage * 24 * 3600;
    //Anfrage
    for (Row row : rows) {
      if (unixTime - row.getTimestamp() < timestamp) {
        anz++;
      } else {
        // Entries are sorted and hence there are no more rows in the list that match criteria
        //break;
      }
    }
    return anz;
  }

  /*
   SELECT StoreID, count(DISTINCT customerID), sum(amount)
  FROM Transactions
  WHERE now() - timestamp < 2592000
  GROUP BY StoreID
 */
  public QueryResult statistikNTage(int tage) {
    ArrayList<Integer> res_storeID = new ArrayList<Integer>();
    ArrayList<BigInteger> res_count = new ArrayList<BigInteger>();
    ArrayList<BigInteger> res_sum = new ArrayList<BigInteger>();
    HashMap<Integer, Integer> storeToIndexMap = new HashMap<>();
    long unixTime = System.currentTimeMillis() / 1000L;
    long timestamp = 24 * 3600 * tage;
    int i = 0;
    for (Row row : rows) {
      if (unixTime - row.getTimestamp() < timestamp) {
        //in Statisik aufnehmen
        Integer index = storeToIndexMap.get(row.getStoreID());
        if (index != null) {
          //StoreID schon einmal aufgetreten
          int unboxedIndex = index;
          res_count.set(unboxedIndex, res_count.get(unboxedIndex).add(new BigInteger("1")));
          res_sum.set(index, res_sum.get(unboxedIndex).add(new BigInteger("" + row.getAmount())));
        } else {
          //StoreID neu
          res_storeID.add(row.getStoreID());
          res_count.add(new BigInteger("1"));
          res_sum.add(new BigInteger("" + row.getAmount()));
          storeToIndexMap.put(row.getStoreID(), i);
          i++;
        }
      } else {
        //break;
      }
    }

    return new QueryResult(res_storeID, res_count, res_sum);
  }

  @Override
  public String toString() {
    StringBuilder res =
        new StringBuilder("TokenID \t\t CostumerID \t StoreID \t amount \t timestamp\n");
    for (Row row : rows) {
      res.append(row.toString());
      res.append("\n");
    }
    return res.toString();
  }
}
