package column_store_3;

import java.math.BigInteger;

public class Row {
	private BigInteger tokenID;
	private int customerID;
	private int storeID;
	private int amount;
	private long timestamp;
	
	public Row(BigInteger tokenID, Integer customerID, Integer storeID, Integer amount, Long timestamp){
		this.tokenID = tokenID;
		this.customerID = customerID;
		this.storeID = storeID;
		this.amount = amount;
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString(){
		return tokenID + "\t" + customerID + "\t\t" + storeID + "\t\t" + amount + "\t\t" + timestamp;
	}
	
	public BigInteger getTokenID(){
		return tokenID;
	}
	
	public int getCustomerID(){
		return customerID;
	}
	
	public int getStoreID(){
		return storeID;
	}
	
	public int getAmount(){
		return amount;
	}
	
	public long getTimestamp(){
		return timestamp;
	}
}
