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
 * @author Hannes Dorfmann
 */
public class BloomFilterJoin implements DBIterator {

  DBIterator left;
  DBIterator right;
  String joinAttribute;
  int joinAttributeIndexLeft;
  int joinAttributeIndexRight;
  BloomFilter<Register[]> leftBloomFilter;
  Map<Object, List<Register[]>> leftMap = new HashMap<>();
  Queue<Register[]> temporaryResult = new LinkedList<>();

  public BloomFilterJoin(DBIterator left, DBIterator right) {
    this.left = left;
    this.right = right;
  }

  @Override public void close() {
    left.close();
    right.close();
  }

  @Override public String[] open() {
    // Find join indexes
    String[] leftHeaders = left.open();
    String[] rightHeaders = right.open();

    for (int i = 0; i < leftHeaders.length; i++) {
      if (leftHeaders[i].equals(joinAttribute)) {
        joinAttributeIndexLeft = i;
        break;
      }
    }

    for (int i = 0; i < rightHeaders.length; i++) {
      if (rightHeaders[i].equals(joinAttribute)) {
        joinAttributeIndexRight = i;
        break;
      }
    }

    // Create the BloomFilter
    leftBloomFilter = createBloomFilter();
    Register[] nextLeft = null;

    while ((nextLeft = left.next()) != null) {
      leftBloomFilter.put(nextLeft);
      putInLeftMap(nextLeft);
    }

    // TODO headers
    return new String[0];
  }

  /**
   * Put a register in the left register
   */
  private void putInLeftMap(Register[] register) {
    Object key = register[joinAttributeIndexLeft].getObject();
    List<Register[]> found = leftMap.get(key);
    if (found != null) {
      found = new ArrayList<>();
      leftMap.put(key, found);
    }

    found.add(register);
  }

  private BloomFilter<Register[]> createBloomFilter() {
    return BloomFilter.create(new Funnel<Register[]>() {
      @Override public void funnel(Register[] from, PrimitiveSink into) {
        into.putInt(from[0].getInt());
      }
    }, 10100, 0.01); // S contains ca. 10100 entries
  }

  @Override public Register[] next() {

    // If there are joined results not returned yet,
    // then return them before continue with the next right join
    if (!temporaryResult.isEmpty()) {
      return temporaryResult.poll();
    }

    // Search for next join partner in right table
    Register[] ret = null;
    Register[] nextRight = null;

    while ((nextRight = right.next()) != null) {

      // No join partner, so continue with next
      if (!leftBloomFilter.mightContain(nextRight)) {
        continue;
      }

      //
      List<Register[]> joinables = getJoinPartners(nextRight);
      for (Register[] left : joinables){
        Register[] joinResult = createJoinResult(left, nextRight);
        if (ret == null) {
          ret = joinResult;
        } else {
          temporaryResult.offer(joinResult);
        }
      }


    }

    // Nothing more to join
    return null;
  }

  /**
   * Get the list of join partners (from left) for the given right side
   * @param right
   * @return
   */
  private List<Register[]> getJoinPartners(Register[] right){
    return leftMap.get(right[0]);
  }

  /**
   * Creates a join result
   * @param left
   * @param right
   * @return
   */
  private Register[] createJoinResult(Register[] left, Register [] right){
    Register[] result = new Register[left.length + right.length - 1]; // -1 because of joining key

    int pos = 0;
    for (int i = 0; i< left.length; i++){
      result[pos++] = left[i];
    }

    for (int i = 1; i< right.length; i++){
      result[pos++] = right[i];
    }

    return result;
  }
}
