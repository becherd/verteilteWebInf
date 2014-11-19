package _6_2;

public interface DBIterator {
	public void close();
	public String[] open();
	public Register[] next();
}
