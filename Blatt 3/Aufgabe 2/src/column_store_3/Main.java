package column_store_3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

public class Main {
	
	public static void main(String[] args) {		
		//import data 
		TransactionsColumn columnstore = new TransactionsColumn();
		TransactionsRows rowstore = new TransactionsRows();
		BufferedReader br;
		String strLine;
		String[] aryLine;
		try {
			br = new BufferedReader(new FileReader("3m.txt"));
			while((strLine = br.readLine()) != null){
				aryLine = strLine.split("\t");
				columnstore.add(new BigInteger(aryLine[0]), Integer.parseInt(aryLine[1]), Integer.parseInt(aryLine[2]), Integer.parseInt(aryLine[3]), Long.parseLong(aryLine[4]));
				rowstore.add(new Row(new BigInteger(aryLine[0]), Integer.parseInt(aryLine[1]), Integer.parseInt(aryLine[2]), Integer.parseInt(aryLine[3]), Long.parseLong(aryLine[4])));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Column Store:");
		//init Timer
		long time_start = System.currentTimeMillis();
		//Teilaufgabe b
		System.out.println("Anzahl der abgewickelten Transaktionen in den letzten 30 Tagen: " + columnstore.getAnzahlLetzteNTage(30));
		//Teilaufgabe c
		System.out.println("Statistiken über Umsatz und Anzahl der Kunden pro Geschäft in den letzten 30 Tagen:");
		columnstore.statistikNTage(30);
		long time_columnstore = System.currentTimeMillis()-time_start;
		System.out.println("");
		System.out.println("----------------------------------------- \nRow Store:");
		time_start = System.currentTimeMillis();
		//Teilaufgabe b
		System.out.println("Anzahl der abgewickelten Transaktionen in den letzten 30 Tagen: " + rowstore.getAnzahlLetzteNTage(30));
		//Teilaufgabe c
		System.out.println("Statistiken über Umsatz und Anzahl der Kunden pro Geschäft in den letzten 30 Tagen:");
		rowstore.statistikNTage(30);
		System.out.println("");
		System.out.println("\n-> Column Store: " + time_columnstore + " Millisekunden");
		System.out.println("\n-> Row Store: " + (System.currentTimeMillis()-time_start) + " Millisekunden");
		
	}

	/*
	 * Ergebnis mit 3 Mio. Datensätzen: 
	 * Column Store: 10936 Millisekunden
	 * Row Store: 12476 Millisekunden
	 * */
	
}
