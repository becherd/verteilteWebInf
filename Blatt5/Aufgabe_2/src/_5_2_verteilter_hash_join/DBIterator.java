package _5_2_verteilter_hash_join;

public interface DBIterator {
	public void close();
	public String[] open();
	public Register[] next();
}
