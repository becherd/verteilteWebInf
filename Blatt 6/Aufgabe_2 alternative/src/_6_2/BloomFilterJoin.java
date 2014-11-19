package _6_2;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Assumption: There is only one join attribute: the first one with index 0
 *
 * @author Hannes Dorfmann
 */
public class BloomFilterJoin implements DBIterator {

  DBIterator left;
  BloomFilterDBIterator right;
  BloomFilter<Register[]> bloomFilter;
  Map<Object, List<Register[]>> leftMap = new HashMap<>();
  Queue<Register[]> temporaryResult = new LinkedList<>();
  int leftJoinAttributeIndex;
  int rightJoinAttributeIndex;

  public BloomFilterJoin(DBIterator left, BloomFilterDBIterator right) {
    this.left = left;
    this.right = right;
  }

  @Override public void close() {
    left.close();
    right.close();
  }

  private void findJoinAttribute(String[] leftHeaders, String[] rightHeaders) {
    for (int i = 0; i < leftHeaders.length; i++) {
      for (int j = 0; j < rightHeaders.length; j++) {

        if (leftHeaders[i].equals(rightHeaders[j])) {
          leftJoinAttributeIndex = i;
          rightJoinAttributeIndex = j;
          return;
        }
      }
    }
    throw new RuntimeException("No joinable attributes found");
  }

  @Override public String[] open() {
    // Find join indexes
    String[] leftHeaders = left.open();
    String[] rightHeaders = right.open();

    findJoinAttribute(leftHeaders, rightHeaders);

    // Create the BloomFilter
    bloomFilter = createBloomFilter();
    Register[] nextLeft = null;

    while ((nextLeft = left.next()) != null) {
      bloomFilter.put(nextLeft);
      putInLeftMap(nextLeft);
    }

    // Write BloomFilter
    right.writeBloomFilter(bloomFilter);

    String[] headers = new String[leftHeaders.length + rightHeaders.length - 1];
    int pos = 0;
    for (int i = 0; i < leftHeaders.length; i++) {
      headers[pos++] = leftHeaders[i];
    }

    for (int i = 0; i < rightHeaders.length; i++) {
      if (i != rightJoinAttributeIndex) {
        headers[pos++] = rightHeaders[i];
      }
    }

    return headers;
  }

  /**
   * Put a register in the left register
   */
  private void putInLeftMap(Register[] register) {
    Object key = register[leftJoinAttributeIndex].getObject();
    List<Register[]> found = leftMap.get(key);
    if (found == null) {
      found = new ArrayList<>();
      leftMap.put(key, found);
    }

    found.add(register);
  }

  private BloomFilter<Register[]> createBloomFilter() {
    return BloomFilter.create(new Funnel<Register[]>() {
      @Override public void funnel(Register[] from, PrimitiveSink into) {
        into.putInt(from[leftJoinAttributeIndex].getInt());
      }
    }, 10100, 0.01); // S contains ca. 10100 entries
  }

  private void sendBloomFilter(){

  }

  @Override public Register[] next() {

    // If there are joined results not returned yet,
    // then return them before continue with the next right join
    if (!temporaryResult.isEmpty()) {
      return temporaryResult.poll();
    }

    // Search for next join partner in right table
    Register[] returnValue = null;
    Register[] nextRight;

    while ((nextRight = right.next()) != null) {

      List<Register[]> joinables = getJoinPartners(nextRight);
      if (joinables != null) { // check for false drops
        for (Register[] left : joinables) {
          Register[] joinResult = createJoinResult(left, nextRight);
          if (returnValue == null) {
            returnValue = joinResult;
          } else {
            temporaryResult.offer(joinResult);
          }
        }

        if (returnValue != null) {
          return returnValue;
        }
      }
    }

    // No more joins
    return null;
  }

  /**
   * Get the list of join partners (from left) for the given right side
   */
  private List<Register[]> getJoinPartners(Register[] right) {
    return leftMap.get(right[rightJoinAttributeIndex].getObject());
  }

  /**
   * Creates a join result
   */
  private Register[] createJoinResult(Register[] left, Register[] right) {
    Register[] result = new Register[left.length + right.length - 1]; // -1 because of joining key

    int pos = 0;
    for (int i = 0; i < left.length; i++) {
      result[pos++] = left[i];
    }

    for (int i = 0; i < right.length; i++) {
      if (i != rightJoinAttributeIndex) {
        result[pos++] = right[i];
      }
    }

    return result;
  }

}
