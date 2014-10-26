package column_store_3;

import java.math.BigInteger;
import java.util.ArrayList;

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
	long unixTime = System.currentTimeMillis() / 1000L;
	for(int i=0; i<this.rows.size(); i++){
		if(unixTime - this.rows.get(i).getTimestamp() < 24*3600*tage){
			//in Statisik aufnehmen
			if(res_storeID.contains(this.rows.get(i).getStoreID())){
				//StoreID schon einmal aufgetreten
				int index = res_storeID.indexOf(this.rows.get(i).getStoreID());
				res_count.set(index, res_count.get(index).add(new BigInteger("1")));
				res_sum.set(index, res_sum.get(index).add(new BigInteger(""+this.rows.get(i).getAmount())));
			}else{
				//StoreID neu
				res_storeID.add(this.rows.get(i).getStoreID());
				res_count.add(new BigInteger("1"));
				res_sum.add(new BigInteger(""+this.rows.get(i).getAmount()));
			}
		}
	}
	//print out
	System.out.println("StoreID \t count(customerID) \t sum(amount)");
	for(int i=0; i<res_storeID.size(); i++){
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
