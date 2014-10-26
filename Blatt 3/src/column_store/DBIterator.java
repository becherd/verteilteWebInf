package column_store;

public interface DBIterator {
	public void close();
	public String[] open();
	public Register[] next();
}
