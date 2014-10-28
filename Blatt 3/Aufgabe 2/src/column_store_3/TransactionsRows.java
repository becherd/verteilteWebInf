package column_store_3;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionsRows {

	ArrayList<Row> rows;
	
	public TransactionsRows(){
		rows = new ArrayList<Row>();
	}
	
	public void add(Row row){
		rows.add(row);
	}
	
	public int getAnzahlLetzteNTage(int tage){
		int anz = 0;
		//jetzt berechnen
		long unixTime = System.currentTimeMillis() / 1000L;
		//Anfrage
		for(int i=0; i<rows.size(); i++){
			if(unixTime-rows.get(i).getTimestamp() < (tage*24*3600)){
				anz++;
			}
		}
		return anz;
	}
	
	/*
 	SELECT StoreID, count(DISTINCT customerID), sum(amount)
	FROM Transactions
	WHERE now() - timestamp < 2592000
	GROUP BY StoreID
 */
public Object[] statistikNTage(int tage){
	ArrayList<Integer> res_storeID = new ArrayList<Integer>();
	ArrayList<BigInteger> res_count = new ArrayList<BigInteger>();
	ArrayList<BigInteger> res_sum = new ArrayList<BigInteger>();
  HashMap<Integer, Integer> storeToIndexMap = new HashMap<>();
	long unixTime = System.currentTimeMillis() / 1000L;
  long timestamp = 24*3600*tage;
  int i = 0;
	for(Row row : rows){
		if(unixTime - row.getTimestamp() < timestamp){
			//in Statisik aufnehmen
      Integer index = storeToIndexMap.get(row.getStoreID());
			if(index != null){
				//StoreID schon einmal aufgetreten
				int unboxedIndex = index;
				res_count.set(unboxedIndex, res_count.get(unboxedIndex).add(new BigInteger("1")));
				res_sum.set(index, res_sum.get(unboxedIndex).add(new BigInteger(""+row.getAmount())));
			}else{
				//StoreID neu
				res_storeID.add(row.getStoreID());
				res_count.add(new BigInteger("1"));
				res_sum.add(new BigInteger(""+row.getAmount()));
        storeToIndexMap.put(row.getStoreID(), i);
        i++;
			}
		}
	}
	//print out
	System.out.println("StoreID \t count(customerID) \t sum(amount)");
	for(i=0; i<res_storeID.size(); i++){
		System.out.println(res_storeID.get(i) + "\t\t" + res_count.get(i) + "\t\t\t" + res_sum.get(i));
	}
	Object[] ret = {res_storeID, res_count, res_sum};
	return ret;
}
	
	@Override
	public String toString(){
		String res = "TokenID \t\t CostumerID \t StoreID \t amount \t timestamp\n";
		for(int i=0; i<rows.size(); i++){
			res += rows.get(i).toString() + "\n";
		}
		return res;
	}
}
