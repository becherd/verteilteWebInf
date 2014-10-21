package nested_loop_join;

public interface DBIterator {
	public void close();
	public String[] open();
	public Register[] next();
}
