package _5_2_verteilter_hash_join;

public class NLJoin implements DBIterator{

	private DBIterator left, right;
	private int key_left = -1, key_right = -1;
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
		int anz = 0;
		//Join-Attribut herausfinden
		for(int i=0; i<this.len_left; i++){
			for(int j=0; j<this.len_right; j++){
				if(header_left[i].equals(header_right[j])){
					key_left = i;
					key_right = j;
					anz++;
				}
			}
		}
		//Uneindeutigkeit bzgl. des zu joinenden Felds
		if(anz>1){
			Exception exception = new Exception("Die Zuordnung der Felder ist nicht eindeutig!");
			try {
				throw exception;
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
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
		if(key_left >= 0 && key_right >=0){
			Register[] next_left = this.last_opened_left;
			while(next_left != null){
				Register[] next_right = this.right.next();
				while(next_right != null){					
					//bei Gleichheit zurück geben
					if(next_right[key_right].equals(next_left[key_left])){
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
