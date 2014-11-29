package _8_2_postgresql;

import java.sql.SQLException;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

public class Koordinator {
	private int kontoNrBank1, kontoNrBank2; 
	private double betrag;
	
	//der Koordinator buch betrag € von kontoNrBank1 nach KontoNrBank2 (auch negativ möglich) TODO
	public Koordinator(int kontoNrBank1, int kontoNrBank2, double betrag){
		this.kontoNrBank1 = kontoNrBank1;
		this.kontoNrBank2 = kontoNrBank2;
		this.betrag = betrag;
	}
	
	public void start() throws ClassNotFoundException, SQLException, XAException{
		System.out.println("Umbuchung von " + this.betrag + " €");
		//Agenten erstellen
		Agent a1 = new Agent(1, "jdbc:postgresql://127.0.0.1:5432/bank1", "postgres", "postgres");
		Agent a2 = new Agent(2, "jdbc:postgresql://127.0.0.1:5432/bank2", "postgres", "postgres");
		
		System.out.println("Bank 1: Konto " + kontoNrBank1 + " - " + a1.getAmount(this.kontoNrBank1));
		System.out.println("Bank 2: Konto " + kontoNrBank2 + " - " + a2.getAmount(this.kontoNrBank2));
		
		//Buchungen durchführen
		a1.add(this.kontoNrBank1, -this.betrag);
		a2.add(this.kontoNrBank2, this.betrag);
		//prepare
		int prp1 = a1.prepare();
		int prp2 = a2.prepare();
				
		boolean do_commit = true;
		if (!((prp1 == XAResource.XA_OK) || (prp1 == XAResource.XA_RDONLY)))
	           do_commit = false;
		if (!((prp2 == XAResource.XA_OK) || (prp2 == XAResource.XA_RDONLY)))
	           do_commit = false;
		
		//entsprechen res.commit oder res.rollback => ACKs implizit durch Beenden der Methode
		if(do_commit){
			a1.commit();
			a2.commit();
			System.out.println("commit");
		}else{
			a1.rollback();
			a2.rollback();
			System.out.println("rollback");
		}
		
		System.out.println("Bank 1: Konto " + kontoNrBank1 + " - " + a1.getAmount(this.kontoNrBank1));
		System.out.println("Bank 2: Konto " + kontoNrBank2 + " - " + a2.getAmount(this.kontoNrBank2));
		
		a1.close();
		a2.close();
		
	}
}
