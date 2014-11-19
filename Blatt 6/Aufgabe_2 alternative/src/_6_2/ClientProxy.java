package _6_2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientProxy implements DBIterator {
  private int PORT;
  private String INET_ADDRESS;
  private Socket client_sock;
  private BufferedReader reader;
  private BufferedWriter writer;
  private ObjectInputStream objectReader;
  private ObjectOutputStream objectWriter;

  public ClientProxy(String serverhost, int port) {
    this.PORT = port;
    this.INET_ADDRESS = serverhost;
    try {
      client_sock = new Socket(INET_ADDRESS, PORT);
      reader = new BufferedReader(new InputStreamReader(client_sock.getInputStream()));
      writer = new BufferedWriter(new OutputStreamWriter(client_sock.getOutputStream()));
      objectReader = new ObjectInputStream(client_sock.getInputStream());
      objectWriter = new ObjectOutputStream(client_sock.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      closeSilently();
    }
  }



  private void closeSilently(){
    try{

      if (objectWriter != null){
        objectWriter.close();
      }

      if (objectReader != null){
        objectReader.close();
      }

      if (writer != null){
        writer.close();
      }

      if (reader != null){
        reader.close();
      }

      if (client_sock != null && !client_sock.isClosed()){
        client_sock.close();
      }

    } catch(Exception e){
      // Ignore --> silently :)
    }
  }


  @Override
  public String[] open() {
    //Namen auslesen
    try {
      writer.write("open");
      writer.newLine();
      writer.flush();
      String[] msg = (String[]) objectReader.readObject();
      if (msg != null) {
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
      Register[] werte = (Register[]) objectReader.readObject();
      if (werte != null) {
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
      if (msg == "closed") {
        System.out.println("Server Closed successfully");
      }
      objectReader.close();
      reader.close();
      writer.close();
      client_sock.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
