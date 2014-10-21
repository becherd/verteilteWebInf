package verteilter_nested_loop_join;

import java.io.IOException;
import java.net.ServerSocket;

import nested_loop_join.DBIterator;
import nested_loop_join.Tablescan;

public class Main_Server {

	public static void main(String[] args) {
		try {
			//Socket erstellen, Tablescan erstellen, Server starten
			ServerSocket socket = new ServerSocket(5677);
			DBIterator tab = new Tablescan("Professoren.data");
			ServerProxy server = new ServerProxy(socket, tab);
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
