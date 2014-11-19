package _6_2;

import com.google.common.hash.BloomFilter;

public interface BloomFilterDBIterator extends DBIterator{
  public void writeBloomFilter(BloomFilter<Integer> bloomFilter, int joinAttributeIndex);
}
