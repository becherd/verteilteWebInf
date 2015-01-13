package vwis.blatt11;


import java.util.*;

public class Threshold {

	//Mietspiegel
	private DBIterator left;
	//Kindergarten
	private DBIterator right;
	private int numRows;

	public Threshold(DBIterator left, DBIterator right, int numRows){
		this.left = left;
		this.right = right;
		this.numRows = numRows;
	}
	
	/**
	 * Executes Threshold ranking on the two iterators left and right, yielding
	 * at most numRows rows.
	 * @return Resulting tuple array
	 */
	public Object[][] execute(){


		HashMap<String, HashMap<Integer, Integer>> tablesMap = new HashMap<String, HashMap<Integer, Integer>>();

		try {

			// Initiate iterators
			left.open();
			right.open();

			// 2 HashMaps for random access
			HashMap<String, Integer> leftMap = new HashMap<String, Integer>();
			HashMap<String, Integer> rightMap = new HashMap<String, Integer>();

			// 2 ArrayLists that represent the order within the HashMaps
			List<String> leftMapOrder = new ArrayList<String>();
			List<String> rightMapOrder = new ArrayList<String>();

			String locationLeft;
			String locationRight;

			Object[] rowLeft;
			Object[] rowRight;

			// Create and fill random accessible structures
			while((rowLeft = left.next()) != null && (rowRight = right.next()) != null) {

				locationLeft = rowLeft[0].toString();
				locationRight = rowRight[0].toString();

				// Fill the HashMap with the values of the left table
				leftMap.put(locationLeft, Integer.parseInt(rowLeft[1].toString()));
				rightMap.put(rowRight[0].toString(), Integer.parseInt(rowRight[1].toString()));

				leftMapOrder.add(locationLeft);
				rightMapOrder.add(locationRight);

			}

			// Both tables have the same size (at least the join result is one table)
			// Perform the actual TH algorithm
			int tableSize = leftMapOrder.size();
			int threshold;
			int leftValue;
			int rightValue;

			// Intermediate result list
			TreeMap<Integer, String> iResults = new TreeMap<Integer, String>();

			// Result list
			TreeMap<Integer, String> resultList = new TreeMap<Integer, String>();

			for(int i=0; i<tableSize; i++) {
				locationLeft = leftMapOrder.get(i);
				locationRight = rightMapOrder.get(i);

				// Calculate current threshold
				threshold = leftMap.get(locationLeft) + rightMap.get(locationRight);

				// Calculate value of "left" table with random access in right
				leftValue = leftMap.get(locationLeft) + rightMap.get(locationLeft);

				// Calculate value of "right" table with random access in left
				rightValue = leftMap.get(locationRight) + rightMap.get(locationRight);

				// System.out.println("TH = "+threshold + " " + locationLeft + " " + leftValue + " " + locationRight + " " + rightValue);

				iResults.put(leftValue, locationLeft);
				iResults.put(rightValue, locationRight);

				// Check intermediate results
				for(Map.Entry<Integer,String> entry : iResults.entrySet()) {
					Integer key = entry.getKey();
					String value = entry.getValue();

					// Check if there are values below the threshold
					if(key <= threshold) {
						// numRows specifies top k
						if(resultList.size() < numRows) {
							resultList.put(key, value);
						}
					}

					//System.out.println(key + " => " + value);
				}


				//System.out.println("Value = "+leftMapOrder.get(i));
			}

			// Build result object
			Object[][] result = new Object[resultList.size()][2];
			int a = 0;
			for(Map.Entry<Integer,String> entry : resultList.entrySet()) {
				Integer key = entry.getKey();
				String value = entry.getValue();

				result[a][0] = value;
				result[a][1] = key;

				a++;
			}

			return result;



		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}


		//System.out.print(left.next());

		//return null;
	}
	
	public static void main(String[] args) {

		System.out.print("###Start Threshold algorithm:### \n");
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
		Threshold th = new Threshold(kindergarten, mietspiegel, k);
		Object[][] result = th.execute();

		System.out.println("Top-K (k = "+k+"): ");

		// Print result
		int size = result.length;
		for(int i=0; i< size; i++ ) {

			System.out.println(result[i][0] + "  :  " + result[i][1]);
		}


	}

}
