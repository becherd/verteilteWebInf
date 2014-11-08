package _5_2_verteilter_hash_join;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import _5_2_verteilter_hash_join.Register;

public class ClientProxy implements _5_2_verteilter_hash_join.DBIterator{
	private int PORT;
	private String INET_ADDRESS;
	private Socket client_sock;
	private BufferedReader reader;
	private BufferedWriter writer;
	private ObjectInputStream reader_obj;
	
	public ClientProxy(String serverhost, int port){
		this.PORT = port;
		this.INET_ADDRESS = serverhost;
		try {
			client_sock = new Socket(INET_ADDRESS, PORT);
			reader  = new BufferedReader(new InputStreamReader(client_sock.getInputStream()));
			writer = new BufferedWriter(new OutputStreamWriter(client_sock.getOutputStream()));
			reader_obj = new ObjectInputStream(client_sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String[] open() {
		//Namen auslesen
		try {
			writer.write("open");
			writer.newLine();
			writer.flush();
			String[] msg = (String[]) reader_obj.readObject();
			if(msg != null){
				return msg;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Register[] next() {
		//nächsten Wert anfordern
		try {
			writer.write("next");
			writer.newLine();
			writer.flush();
			Register[] werte = (Register[])reader_obj.readObject();
			if(werte != null){
				return werte;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void close() {
		//Verbindung beenden
		try {
			writer.write("close");
			writer.newLine();
			writer.flush();
			String msg = reader.readLine();
			if(msg =="closed"){
				System.out.println("Server Closed successfully");
			}
			reader_obj.close();
			reader.close();
			writer.close();
			client_sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
