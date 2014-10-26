package column_store;

import java.io.Serializable;

public class Register implements Serializable{
	public String[] states = {"Unbound", "Int", "Double", "Bool", "String"};
	private String state;
	int intValue;
	boolean boolValue;
	String stringValue;
	double doubleValue;
	
	public Register(){
		state = "Unbound";
	}
	
	//setter
	public void setUnbound() { state="Unbound"; }
	public void setInt(int value) { state="Int"; intValue=value;}
	public void setDouble(double value) { state="Double"; doubleValue=value;}
	public void setBool(boolean value) { state="Bool"; boolValue=value;}
	public void setString(String value) { state="String"; stringValue=value;}
	 
	//getter
	public String getState(){
		return this.state;
	}
	public int getInt(){
		return intValue;
	}
	public double getDouble(){
		return doubleValue;
	}
	public boolean getBool(){
		return boolValue;
	}
	public String getString(){
		return stringValue;
	}
	
	public boolean operator_gleich(Register r){
		// Make sure that the state is the same
	   if (!state.equals(r.state))
	      return false;

	   // Compare
	   switch (state) {
	      case "Unbound": return true;
	      case "Int": return intValue==r.intValue;
	      case "Double": return doubleValue==r.doubleValue;
	      case "Bool": return boolValue==r.boolValue;
	      case "String": return stringValue.equals(r.stringValue);
	   }
	   return false;
	}
	public boolean operator_kleiner(Register r){
		// Make sure that the state is the same
	   if (!state.equals(r.state))
	      return false;

	   // Compare
	   switch (state) {
	      case "Unbound": return true;
	      case "Int": return intValue<r.intValue;
	      case "Double": return doubleValue<r.doubleValue;
	      case "Bool": return false;
	      case "String": return false;
	   }
	   return false;
	}
	
	@Override
	public String toString(){
		String ret = ""; 		//state + " ";
		switch (state) {
	      case "Int": ret+= intValue;break;
	      case "Double": ret+= doubleValue;break;
	      case "Bool": ret+= boolValue;break;
	      case "String": ret+= stringValue;break;
	   }
		return ret;
	}
	
	@Override
	public boolean equals(Object obj){
		if(obj instanceof Register){
			Register r = (Register) obj;
			return this.operator_gleich(r);
		}
		return false;
	}
}
