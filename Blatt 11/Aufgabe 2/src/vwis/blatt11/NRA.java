package vwis.blatt11;

public class NRA {
	
	//Mietspiegel
	private DBIterator left;
	//Kindergarten
	private DBIterator right;
	private int numRows; // <n>

	public NRA(DBIterator left, DBIterator right, int numRows){
		this.left = left;
		this.right = right;
		this.numRows = numRows;
	}
	
	/**
	 * Executes NRA ranking on the two iterators left and right, yielding
	 * at most numRows rows.
	 * @return Resulting tuple array
	 */
	public Object[][] execute(){
		//TODO Implement
		return null;
	}
	
	public static void main(String[] args) {
		//TODO Execute NRA with Tablescans of Mietspiegel and Kindergarten and print result
	}

}
