package nested_loop_join;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Tablescan implements DBIterator{
	private String filename;
	private BufferedReader reader;
	private String table_types[] = null;
	
	public Tablescan(String filename){
		this.filename = filename;
		try {
			reader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] open() {
		String line;
		String[] result = null;
		try {
			reader.close();
			reader = new BufferedReader(new FileReader(filename));
			//Namen
			if ((line = this.reader.readLine()) != null) {
				result = line.split("\t");
			}
			//Typen
			if ((line = this.reader.readLine()) != null) {
				table_types = line.split("\t");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Register[] next() {
		String line = "";
		//Daten auslesen
		try {
			line = this.reader.readLine();
		} catch (IOException e) {}
		if(line == null || line.trim().equals("")){
			//leeren Vektor zurückgeben
			return null;
		}
		String[] values = line.split("\t");
		
		Register reg[] = new Register[values.length];
		for(int i=0; i<values.length; i++){
			Register reg_local = new Register();
			switch(table_types[i]){
			    case "Integer": reg_local.setInt(Integer.parseInt(values[i]));break;
			    case "Double": 	reg_local.setDouble(Double.parseDouble(values[i]));break;
			    case "Bool": 	reg_local.setBool(Boolean.parseBoolean(values[i]));break;
			    case "String": 	reg_local.setString(values[i].replace('"', ' ').trim());break;
			}
			reg[i] = reg_local;
		}
		return reg;
	}
	
	@Override
	public void close(){
		try {
			this.reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
