package column_store;

import java.util.ArrayList;

public class NLJoin implements DBIterator{

	private DBIterator left, right;
        private ArrayList<Integer> join_keys_left = new ArrayList();
        private ArrayList<Integer> join_keys_right = new ArrayList();
	private int len_left = 0, len_right = 0;
	private Register[] last_opened_left;
	
	public NLJoin(DBIterator left, DBIterator right){
		this.left = left;
		this.right = right;	
        }
	
	@Override
	public String[] open(){
		//jeweils öffnen -> gemeinsamen Namen suchen und anhand dessen joinen
		String[] header_left = left.open();
		this.len_left = header_left.length;
		String[] header_right = right.open();
		this.len_right = header_right.length;
		
    
		//Join-Attribut herausfinden
		for(int i=0; i<this.len_left; i++){
			for(int j=0; j<this.len_right; j++){
				if(header_left[i].equals(header_right[j])){
					join_keys_left.add(i);
					join_keys_right.add(j);  
				}
			}
                         
		}
		
		//Header-Namen konkatenieren
		String header_new[] = new String[header_left.length + header_right.length];
		int i=0;
		for( ; i<header_left.length; i++){
			header_new[i] = header_left[i];
		}
		for( ; i<header_left.length + header_right.length; i++){
			header_new[i] = header_right[i-header_left.length];
		}
		//erstes Tupel öffnen
		this.last_opened_left = this.left.next();
		//Kopfzeile zurückgeben
		return header_new;
	}

	@Override
	public Register[] next() {
		if(!join_keys_left.isEmpty() && !join_keys_right.isEmpty()){
			Register[] next_left = this.last_opened_left;
                        boolean tuplesJoin;
			while(next_left != null){
				Register[] next_right = this.right.next();
				while(next_right != null){
                                        tuplesJoin=true;
                                        for(int i=0; i<join_keys_left.size(); i++){
                                            if(!next_right[join_keys_right.get(i)].equals(next_left[join_keys_left.get(i)])){
                                                tuplesJoin=false;
                                            }
                                        }
					//bei Gleichheit zurück geben
					if(tuplesJoin){
						//Zeilen zusammen in ein Array kopieren
						Register[] join = new Register[len_right+len_left];
						int k=0;
						for( ; k<len_left; k++){
							join[k] = next_left[k];
						}
						for( ; k<len_left+len_right; k++){
							join[k] = next_right[k-len_left];
						}
						return join;
					}
					//rechte Zeile auslesen
					next_right = this.right.next();
				}
				//Linke Zeile auslesen
				this.right.open();
				next_left = this.left.next();
				this.last_opened_left = next_left;
			}
		}
		return null;
	}

	@Override
	public void close() {
		this.left.close();
		this.right.close();
	}
}
