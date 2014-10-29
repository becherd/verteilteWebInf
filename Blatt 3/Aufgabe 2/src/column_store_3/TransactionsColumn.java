package column_store_3;

import java.math.BigInteger;
import java.util.ArrayList;

public class TransactionsColumn {
	ArrayList<BigInteger> tokenID;
	ArrayList<Integer> customerID;
	ArrayList<Integer> storeID;
	ArrayList<Integer> amount;
	ArrayList<Long> timestamp;

	public TransactionsColumn(){
		this.tokenID = new ArrayList<BigInteger>();
		this.customerID = new ArrayList<Integer>();
		this.storeID = new ArrayList<Integer>();
		this.amount = new ArrayList<Integer>();
		this.timestamp = new ArrayList<Long>();
	}
	
	public void add(BigInteger tokenID, Integer customerID, Integer storeID, Integer amount, Long timestamp){
		this.tokenID.add(tokenID);
		this.customerID.add(customerID);
		this.storeID.add(storeID);
		this.amount.add(amount);
		this.timestamp.add(timestamp);
	}
	
	public int getAnzahlLetzteNTage(int tage){
		int anz = 0;
		//jetzt berechnen
		long unixTime = System.currentTimeMillis() / 1000L;
		//Anfrage
		for(int i=0; i<timestamp.size(); i++){
			if(unixTime-timestamp.get(i) < (tage*24*3600)){
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
        ArrayList<Integer> res_customerID = new ArrayList<>();

		long unixTime = System.currentTimeMillis() / 1000L;
        int custSize = this.customerID.size();
        int preCalcTime = 24*3600*tage;
        boolean distinctCustomerIDAdd = false;

		for(int i=0; i<custSize; i++){

			if(unixTime - this.timestamp.get(i) < preCalcTime){

				//in Statisik aufnehmen
				if(res_storeID.contains(this.storeID.get(i))){

                    // Check ob customer id bereits enthalten ist
                    if(res_customerID.contains(this.customerID.get(i))) {
                        distinctCustomerIDAdd = false;
                    } else {
                        res_customerID.add(this.customerID.get(i));
                        distinctCustomerIDAdd = true;
                    }

					//StoreID schon einmal aufgetreten
					int index = res_storeID.indexOf(this.storeID.get(i));
                    if(distinctCustomerIDAdd) {
                        System.out.println("NOW");
                        res_count.set(index, res_count.get(index).add(new BigInteger("1")));
                    }
					res_sum.set(index, res_sum.get(index).add(new BigInteger(""+this.amount.get(i))));
				}else{
					//StoreID neu
					res_storeID.add(this.storeID.get(i));
                    res_count.add(new BigInteger("1"));
                    res_customerID.add(this.customerID.get(i));
					res_sum.add(new BigInteger(""+this.amount.get(i)));
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
		String str = "TokenID \t\t CostumerID \t StoreID \t amount \t timestamp\n";
		for(int i=0; i<this.tokenID.size(); i++){
			str += this.tokenID.get(i) + "\t";
			str += this.customerID.get(i) + "\t\t";
			str += this.storeID.get(i) + "\t\t";
			str += this.amount.get(i) + "\t\t";
			str += this.timestamp.get(i) + "\n";
		}
		return str;
	}
}
