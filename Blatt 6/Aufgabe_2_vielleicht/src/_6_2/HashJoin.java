package _6_2;

import java.util.ArrayList;
import java.util.HashMap;

public class HashJoin implements DBIterator{
	private DBIterator left, right;
	private ArrayList<Integer> key_left, key_right;
	private int len_left, len_right;
	private HashMap<Object, ArrayList<Register[]>> hashmap;
	private ArrayList<Register[]> found;
	private Register[] crossLeft = null;
	
	public HashJoin(DBIterator left, DBIterator right){
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String[] open(){
		this.key_left = new ArrayList<Integer>();
		this.key_right = new ArrayList<Integer>();
		this.found = new ArrayList<Register[]>();
		//jeweils öffnen -> gemeinsamen Namen suchen und anhand dessen joinen
		String[] header_left = left.open();
		this.len_left = header_left.length;
		String[] header_right = right.open();
		this.len_right = header_right.length;
		//Join-Attribut herausfinden
		for(int i=0; i<this.len_left; i++){
			for(int j=0; j<this.len_right; j++){
				if(header_left[i].equals(header_right[j])){
					key_left.add(i);
					key_right.add(j);
				}
			}
		}
		
		//Hash-Tabelle erstellen mit erstem Iterator nach erstem Matching (nur falls kein Kreuzprodukt)		
		if(!this.key_left.isEmpty()){
			hashmap = new HashMap<Object,ArrayList<Register[]>>();
			Register[] line = this.left.next();
			while(line != null){
				if(hashmap.get(line[this.key_left.get(0)].getObject()) == null){
					//neu einfügen
					ArrayList<Register[]> value = new ArrayList<Register[]>();
					value.add(line);
					hashmap.put(line[this.key_left.get(0)].getObject(), value);
					line = this.left.next();
				}else{
					//an Liste anhängen
					ArrayList<Register[]> value = hashmap.get(line[this.key_left.get(0)].getObject());
					value.add(line);
					hashmap.put(line[this.key_left.get(0)].getObject(), value);
					line = this.left.next();
				}
			}
		}

		//Header-Namen konkatenieren
		String header_new[] = new String[header_left.length + header_right.length - key_right.size()];
		int i=0;
		int pos = 0;
		for( ; i<header_left.length; i++){
			header_new[pos] = header_left[i];
			pos++;
		}
		for( ; i<header_left.length + header_right.length; i++){
			if(!this.key_right.contains(i-header_left.length)){
				header_new[pos] = header_right[i-header_left.length];
				pos++;
			}
		}
		//Kopfzeile zurückgeben
		return header_new;
	}

	@Override
	public Register[] next() {
		Register[] ret = null;
		//Kreuzprodukt? 
		if(this.key_left.isEmpty()){
			Register[] sright, sleft;
			//links gespeichert?
			if(this.crossLeft != null){
				sright = this.right.next();
				if(sright != null){
					ret = new Register[this.len_left + this.len_right - key_right.size()];
					int i=0;
					int pos = 0;
					for( ; i<this.len_left; i++){
						ret[pos] = this.crossLeft[i];
						pos++;
					}
					for( ; i<this.len_left  + this.len_right; i++){
						if(!this.key_right.contains(i-this.len_left)){
							ret[pos] = sright[i-this.len_left];
							pos++;
						}
					}
					return ret;
				}else{
					crossLeft = null;
				}
			}
			//nächste linken Tupel
			while(ret == null){
				sleft = this.left.next();
				this.crossLeft = sleft;
				if(sleft != null){
					this.right.open();
					sright = this.right.next();
					if(sright != null){
						ret = new Register[this.len_left + this.len_right - key_right.size()];
						int i=0;
						int pos = 0;
						for( ; i<this.len_left; i++){
							ret[pos] = sleft[i];
							pos++;
						}
						for( ; i<this.len_left  + this.len_right; i++){
							if(!this.key_right.contains(i-this.len_left)){
								ret[pos] = sright[i-this.len_left];
								pos++;
							}
						}
						return ret;
					}
				}else
					return null;
			}
			return ret;
		}
		//noch andere gespeichert?
		if(!found.isEmpty()){
			ret = found.get(0);
			found.remove(0);
		}else{
			//suchen
			Register[] sRight = this.right.next();
			Register[] sLeft;
			Register[] evtlRet;
			ArrayList<Register[]> posPartners;
			while(ret == null){
				if(sRight != null){
					posPartners = hashmap.get(sRight[this.key_right.get(0)].getObject());
					if(posPartners != null){
						for(int j=0; j<posPartners.size(); j++){
							sLeft = posPartners.get(j);
							//weitere Join-Parameter prüfen
							for(int i=1; i< this.key_left.size(); i++){
								if(sLeft[this.key_left.get(i)] != sRight[this.key_right.get(i)])
									continue;
							}
							//falls gematcht zurückgeben
							evtlRet = new Register[this.len_left + this.len_right - key_right.size()];
							int i=0;
							int pos = 0;
							for( ; i<this.len_left; i++){
								evtlRet[pos] = sLeft[i];
								pos++;
							}
							for( ; i<this.len_left  + this.len_right; i++){
								if(!this.key_right.contains(i-this.len_left)){
									evtlRet[pos] = sRight[i-this.len_left];
									pos++;
								}
							}
							if(ret == null)
								ret = evtlRet;
							else
								this.found.add(evtlRet);
						}
						if(ret != null)
							return ret;
					}
					sRight = this.right.next();
				}else
					return null;
			}
		}
		return ret;
	}

	@Override
	public void close() {
		this.left.close();
		this.right.close();
	}
}
