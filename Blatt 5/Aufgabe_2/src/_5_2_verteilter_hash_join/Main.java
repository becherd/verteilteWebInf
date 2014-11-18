package _5_2_verteilter_hash_join;
import java.io.IOException;
import java.net.ServerSocket;

import _5_2_verteilter_hash_join.Register;
import _5_2_verteilter_hash_join.Tablescan;

public class Main {

	public static void main(String[] args) throws IOException{
		System.out.println("----------------------------------------");
		System.out.println("Hash-Join");
		System.out.println("----------------------------------------");
		testHashJoin();
		System.out.println("\n----------------------------------------");
		System.out.println("X-Join");
		System.out.println("----------------------------------------");
		testXJoin();
		System.out.println("\n----------------------------------------");
		System.out.println("NL-Join");
		System.out.println("----------------------------------------");
		testNLJoin();
	}
	
	public static void testXJoinLocal(){
		Tablescan tab = new Tablescan("S.data");
		Tablescan tab2 = new Tablescan("R.data");
		XJoin join = new XJoin(tab2, tab);
		String[] op = join.open();
		for(int i=0; i<op.length; i++){
			System.out.print(op[i] + "\t");
		}
		System.out.println("");
		Register[] result = join.next();
		while (result != null){
			for(int i=0; i<result.length; i++){
				System.out.print(result[i] + "\t");
			}
			System.out.println("");
			result = join.next();
		}
		join.close();
	}
	
	public static void testXJoin(){
		Runnable s1 = new Runnable(){
			public void run(){
				//Server erstellen
				ServerSocket socket;
				try {
					socket = new ServerSocket(5679);
					DBIterator tab = new Tablescan("S.data");
					ServerProxy server = new ServerProxy(socket, tab);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t1 = new Thread(s1);
		t1.start();
		
		//Server 2 erstellen
		Runnable s2 = new Runnable(){
			public void run(){
				//Server erstellen
				ServerSocket socket;
				try {
					socket = new ServerSocket(5680);
					DBIterator tab = new Tablescan("R.data");
					ServerProxy server = new ServerProxy(socket, tab);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t2 = new Thread(s2);
		t2.start();
				
		//Vorlesungen öffnen, Client erstellen, joinen
		ClientProxy client = new ClientProxy("localhost", 5679);
		ClientProxy client2 = new ClientProxy("localhost", 5680);
		XJoin join = new XJoin(client, client2);
		//Zeitmessung
		long time_start_gesamt = System.currentTimeMillis();
		long time_start_next = System.currentTimeMillis();
		String[] op = join.open();
		/*for(int i=0; i<op.length; i++){
			System.out.print(op[i] + "\t");
		}
		System.out.println("");*/
		Register[] result = join.next();
		long time_stop_next = System.currentTimeMillis();
		while (result != null){
			/*for(int i=0; i<result.length; i++){
				System.out.print(result[i] + "\t");
			}
			System.out.println("");*/
			result = join.next();
		}
		join.close();
		long time_stop_gesamt = System.currentTimeMillis();
		System.out.println("Zeit bis erstes Ergebnis: " + (time_stop_next - time_start_next) + " ms");
		System.out.println("Zeit gesamt: " + (time_stop_gesamt - time_start_gesamt) + " ms");
	}

	public static void testHashJoin(){
		Runnable s1 = new Runnable(){
			public void run(){
				//Server erstellen
				ServerSocket socket;
				try {
					socket = new ServerSocket(5677);
					DBIterator tab = new Tablescan("S.data");
					ServerProxy server = new ServerProxy(socket, tab);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t1 = new Thread(s1);
		t1.start();
		
		//Server 2 erstellen
		Runnable s2 = new Runnable(){
			public void run(){
				//Server erstellen
				ServerSocket socket;
				try {
					socket = new ServerSocket(5678);
					DBIterator tab = new Tablescan("R.data");
					ServerProxy server = new ServerProxy(socket, tab);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t2 = new Thread(s2);
		t2.start();
				
		//Vorlesungen öffnen, Client erstellen, joinen
		ClientProxy client = new ClientProxy("localhost", 5677);
		ClientProxy client2 = new ClientProxy("localhost", 5678);
		HashJoin join = new HashJoin(client, client2);
		//Zeitmessung
		long time_start_gesamt = System.currentTimeMillis();
		long time_start_next = System.currentTimeMillis();
		String[] op = join.open();
		/*for(int i=0; i<op.length; i++){
			System.out.print(op[i] + "\t");
		}
		System.out.println("");*/
		Register[] result = join.next();
		long time_stop_next = System.currentTimeMillis();
		while (result != null){
			/*for(int i=0; i<result.length; i++){
				System.out.print(result[i] + "\t");
			}
			System.out.println("");*/
			result = join.next();
		}
		join.close();
		long time_stop_gesamt = System.currentTimeMillis();
		System.out.println("Zeit bis erstes Ergebnis: " + (time_stop_next - time_start_next) + " ms");
		System.out.println("Zeit gesamt: " + (time_stop_gesamt - time_start_gesamt) + " ms");
	}

	public static void testNLJoin(){
		Runnable s1 = new Runnable(){
			public void run(){
				//Server erstellen
				ServerSocket socket;
				try {
					socket = new ServerSocket(5677);
					DBIterator tab = new Tablescan("S.data");
					ServerProxy server = new ServerProxy(socket, tab);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t1 = new Thread(s1);
		t1.start();
		
		//Server 2 erstellen
		Runnable s2 = new Runnable(){
			public void run(){
				//Server erstellen
				ServerSocket socket;
				try {
					socket = new ServerSocket(5678);
					DBIterator tab = new Tablescan("R.data");
					ServerProxy server = new ServerProxy(socket, tab);
					server.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		Thread t2 = new Thread(s2);
		t2.start();
				
		//Vorlesungen öffnen, Client erstellen, joinen
		ClientProxy client = new ClientProxy("localhost", 5677);
		ClientProxy client2 = new ClientProxy("localhost", 5678);
		NLJoin join = new NLJoin(client, client2);
		//Zeitmessung
		long time_start_gesamt = System.currentTimeMillis();
		long time_start_next = System.currentTimeMillis();
		String[] op = join.open();
		/*for(int i=0; i<op.length; i++){
			System.out.print(op[i] + "\t");
		}
		System.out.println("");*/
		Register[] result = join.next();
		long time_stop_next = System.currentTimeMillis();
		while (result != null){
			/*for(int i=0; i<result.length; i++){
				System.out.print(result[i] + "\t");
			}
			System.out.println("");*/
			result = join.next();
		}
		join.close();
		long time_stop_gesamt = System.currentTimeMillis();
		System.out.println("Zeit bis erstes Ergebnis: " + (time_stop_next - time_start_next) + " ms");
		System.out.println("Zeit gesamt: " + (time_stop_gesamt - time_start_gesamt) + " ms");
	}
	
}
