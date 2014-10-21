package verteilter_nested_loop_join;

import nested_loop_join.DBIterator;
import nested_loop_join.Register;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerProxy {
	private ServerSocket socket;
	private DBIterator it;
	BufferedReader reader;
	BufferedWriter writer;
	ObjectOutputStream writer_obj;
	
	public ServerProxy(ServerSocket socket, DBIterator it){
		this.socket = socket;
		this.it = it;
	}
	
	public void start(){
		try {
			//Verbindung erstellen; Reader initialisieren
			Socket connection = socket.accept();
			System.out.println("Client hat sich verbunden.");
			reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
			writer_obj = new ObjectOutputStream(connection.getOutputStream());
			
			//nötiges schicken
			String command = reader.readLine();
			while(!command.equals("close")){
				if(command == null || command.length() <= 0){
					System.out.printf("End connection to %s\n", connection.getRemoteSocketAddress());
					connection.close();
				}else if(command.equals("open")){
					String[] header = it.open();
					writer_obj.writeObject(header);
					writer_obj.flush();
				}else if(command.equals("next")){
					Register[] header = it.next();
					writer_obj.writeObject(header);
					writer_obj.flush();
				}
				command = reader.readLine();
			}
			//Verbindung beenden
			writer.write("closed");
			writer.newLine();
			writer.flush();
			writer_obj.close();
			writer.close();
			reader.close();
			connection.close();
			socket.close();
			it.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e) {}
			it.close();
		}
	}

}
