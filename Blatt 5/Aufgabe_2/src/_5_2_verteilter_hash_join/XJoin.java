package _5_2_verteilter_hash_join;

import java.util.ArrayList;
import java.util.HashMap;

public class XJoin implements DBIterator{

	private DBIterator left, right;
	private ArrayList<Integer> key_left, key_right;
	private int len_left, len_right;
	private HashMap<Object, ArrayList<Register[]>> hashmap_left, hashmap_right;
	private ArrayList<Register[]> found;
	private Register[] crossLeft = null;
	private boolean xCurrentLeft, leftEmpty, rightEmpty;
	
	public XJoin(DBIterator left, DBIterator right){
		this.left = left;
		this.right = right;
	}
	
	@Override
	public String[] open(){
		this.key_left = new ArrayList<Integer>();
		this.key_right = new ArrayList<Integer>();
		this.found = new ArrayList<Register[]>();
		this.hashmap_left = new HashMap<Object, ArrayList<Register[]>>();
		this.hashmap_right = new HashMap<Object, ArrayList<Register[]>>();
		this.xCurrentLeft = true;
		this.leftEmpty = false;
		this.rightEmpty = false;
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
		//XJoin: 
		//noch andere gespeichert?
		if(!found.isEmpty()){
			ret = found.get(0);
			found.remove(0);
		}else{
			//suchen
			ret = findNext();
		}
		return ret;
	}
	
	public Register[] findNext(){
		Register[] newReg, res = null, evtlRet;
		//aktuelles Set holen
		if(this.xCurrentLeft){
			newReg = this.left.next();
			if(newReg == null){
				this.leftEmpty = true;
				if(this.rightEmpty)
					return null;
				else{
					this.xCurrentLeft = false;
					return findNext();
				}
			}
			//in Hashtabelle speichern
			if(hashmap_left.get(newReg[this.key_left.get(0)].getObject()) == null){
				//neu einfügen
				ArrayList<Register[]> value = new ArrayList<Register[]>();
				value.add(newReg);
				hashmap_left.put(newReg[this.key_left.get(0)].getObject(), value);
			}else{
				//an Liste anhängen
				ArrayList<Register[]> value = hashmap_left.get(newReg[this.key_left.get(0)].getObject());
				value.add(newReg);
				hashmap_left.put(newReg[this.key_left.get(0)].getObject(), value);
			}
			//in anderer Hashtabelle was zu finden? 
			ArrayList<Register[]> posPartners = hashmap_right.get(newReg[this.key_left.get(0)].getObject());
			if(posPartners != null){
				Register[] secSide;
				//andere Parameter testen
				for(int j=0; j<posPartners.size(); j++){
					secSide = posPartners.get(j);
					//weitere Join-Parameter prüfen
					for(int i=1; i< this.key_left.size(); i++){
						if(secSide[this.key_left.get(i)] != newReg[this.key_right.get(i)])
							continue;
					}
					//falls gematcht zurückgeben
					evtlRet = new Register[this.len_left + this.len_right - key_right.size()];
					int i=0;
					int pos = 0;
					for( ; i<this.len_left; i++){
						evtlRet[pos] = newReg[i];
						pos++;
					}
					for( ; i<this.len_left  + this.len_right; i++){
						if(!this.key_right.contains(i-this.len_left)){
							evtlRet[pos] = secSide[i-this.len_left];
							pos++;
						}
					}
					if(res == null)
						res = evtlRet;
					else
						this.found.add(evtlRet);
				}
			}
		}else{
			newReg = this.right.next();
			if(newReg == null){
				this.rightEmpty = true;
				if(this.leftEmpty)
					return null;
				else{
					this.xCurrentLeft = true;
					return findNext();
				}
			}
			//in Hashtabelle speichern
			if(hashmap_right.get(newReg[this.key_right.get(0)].getObject()) == null){
				//neu einfügen
				ArrayList<Register[]> value = new ArrayList<Register[]>();
				value.add(newReg);
				hashmap_right.put(newReg[this.key_right.get(0)].getObject(), value);
			}else{
				//an Liste anhängen
				ArrayList<Register[]> value = hashmap_right.get(newReg[this.key_right.get(0)].getObject());
				value.add(newReg);
				hashmap_right.put(newReg[this.key_right.get(0)].getObject(), value);
			}
			//in anderer Hashtabelle was zu finden? 
			ArrayList<Register[]> posPartners = hashmap_left.get(newReg[this.key_right.get(0)].getObject());
			if(posPartners != null){
				Register[] secSide;
				//andere Parameter testen
				for(int j=0; j<posPartners.size(); j++){
					secSide = posPartners.get(j);
					//weitere Join-Parameter prüfen
					for(int i=1; i< this.key_right.size(); i++){
						if(secSide[this.key_right.get(i)] != newReg[this.key_left.get(i)])
							continue;
					}
					//falls gematcht zurückgeben
					evtlRet = new Register[this.len_left + this.len_right - key_right.size()];
					int i=0;
					int pos = 0;
					for( ; i<this.len_left; i++){
						evtlRet[pos] = secSide[i];
						pos++;
					}
					for( ; i<this.len_left  + this.len_right; i++){
						if(!this.key_right.contains(i-this.len_left)){
							evtlRet[pos] = newReg[i-this.len_left];
							pos++;
						}
					}
					if(res == null)
						res = evtlRet;
					else
						this.found.add(evtlRet);
				}
			}
		}
		
		if(res == null){
			this.xCurrentLeft = !this.xCurrentLeft;
			return findNext();
		}else
			return res;
	}

	@Override
	public void close() {
		this.left.close();
		this.right.close();
	}
}
