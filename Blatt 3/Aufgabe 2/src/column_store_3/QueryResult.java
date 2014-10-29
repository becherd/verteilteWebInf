package column_store_3;

import java.math.BigInteger;
import java.util.List;

/**
 * Simple class to represents the statistics query result
 * @author Hannes Dorfmann
 */
public class QueryResult {

  List<Integer> storeIds;
  List<BigInteger> counts;
  List<BigInteger> sums;

  public QueryResult(List<Integer> storeIds, List<BigInteger> counts, List<BigInteger> sums) {
    this.storeIds = storeIds;
    this.counts = counts;
    this.sums = sums;
  }

  public int size(){
    return storeIds.size();
  }

  public void print(){
    System.out.println("StoreID \t count(customerID) \t sum(amount)");
    for(int i=0; i<storeIds.size(); i++){
      System.out.println(storeIds.get(i) + "\t\t" + counts.get(i) + "\t\t\t" + sums.get(i));
    }
  }
}
