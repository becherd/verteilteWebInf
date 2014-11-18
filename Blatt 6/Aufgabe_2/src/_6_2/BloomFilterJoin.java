package _6_2;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public BloomFilterJoin(DBIterator left, DBIterator right, String joinAttribute) {
    this.left = left;
    this.right = right;
    this.joinAttribute = joinAttribute;
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
   *
   * @param register
   */
  private void putInLeftMap(Register[] register){
    Object key = register[joinAttributeIndexLeft].getObject();
    List<Register[]> found = leftMap.get(key);
    if (found != null){
      found = new ArrayList<>();
      leftMap.put(key, found);
    }

    found.add(register);

  }

  private BloomFilter<Register[]> createBloomFilter() {
    return BloomFilter.create(new Funnel<Register[]>() {
      @Override public void funnel(Register[] from, PrimitiveSink into) {
        into.putInt(from[joinAttributeIndexLeft].getInt());
      }
    }, 10100, 0.001); // S contains ca. 10100 entries
  }

  @Override public Register[] next() {

    // Read the right side
    Register[] nextRight = null;

    while ((nextRight = right.next()) != null) {

      if (leftBloomFilter.mightContain(nextRight)){

      }

    }

    return new Register[0];
  }
}
