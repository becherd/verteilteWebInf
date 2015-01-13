package vwis.blatt11;

import java.util.*;

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
		HashMap<String, HashMap<Integer, Integer>> tablesMap = new HashMap<String, HashMap<Integer, Integer>>();

		try {

			// Initiate iterators
			left.open();
			right.open();

			// Result Map
			HashMap<String, Integer> iResultMap = new HashMap<String, Integer>();

			// Intermediate result list
			TreeMap<Integer, String> iResults = new TreeMap<Integer, String>();

			String locationLeft;
			String locationRight;

			Object[] rowLeft;
			Object[] rowRight;

			int iResVal;

			// Create and fill random accessible structures
			while ((rowLeft = left.next()) != null && (rowRight = right.next()) != null) {

				locationLeft = rowLeft[0].toString();
				locationRight = rowRight[0].toString();

				// Check if the value was already seen
				if(iResultMap.containsKey(locationLeft)) {
					iResVal = Integer.parseInt(rowLeft[1].toString()) + iResultMap.get(locationLeft);
					iResults.put(iResVal, locationLeft);
				} else {
					iResultMap.put(locationLeft, Integer.parseInt(rowLeft[1].toString()));
				}

				// Check if the value was already seen
				if(iResultMap.containsKey(locationRight)) {
					iResVal = Integer.parseInt(rowRight[1].toString()) + iResultMap.get(locationRight);
					iResults.put(iResVal, locationRight);
				} else {
					iResultMap.put(locationRight, Integer.parseInt(rowRight[1].toString()));
				}

				// Check if top k is fulfilled already
				if(iResults.size() >= numRows) {
					break;
				}


			}

			// Build result object
			Object[][] result = new Object[numRows][2];
			int a = 0;
			for(Map.Entry<Integer,String> entry : iResults.entrySet()) {
				Integer key = entry.getKey();
				String value = entry.getValue();

				result[a][0] = value;
				result[a][1] = key;

				// top k (numRows) results
				if(a == (numRows-1)) {
					break;
				}

				a++;
			}

			return result;




		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


	}
	
	public static void main(String[] args) {
		System.out.print("###Start NRA algorithm:### \n");
		Tablescan kindergarten;
		Tablescan mietspiegel;

		try {
			kindergarten = new Tablescan("kindergarten");
			mietspiegel = new Tablescan("mietspiegel");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		int k = 3;
		NRA nra = new NRA(kindergarten, mietspiegel, k);
		Object[][] result = nra.execute();

		System.out.println("Top-K (k = "+k+"): ");

		// Print result
		int size = result.length;
		for(int i=0; i< size; i++ ) {
			System.out.println(result[i][0] + "  :  " + result[i][1]);
		}
	}

}
