package _6_2;

import com.google.common.hash.BloomFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BloomFilterServerProxy {
  private ServerSocket socket;
  private DBIterator it;
  private BufferedReader reader;
  private BufferedWriter writer;
  private ObjectOutputStream objectWriter;
  private ObjectInputStream objectReader;
  private BloomFilter<Register[]> bloomFilter;

  public BloomFilterServerProxy(ServerSocket socket, DBIterator it) {
    this.socket = socket;
    this.it = it;
  }

  public void start() {
    Socket connection = null;
    try {
      //Verbindung erstellen; Reader initialisieren
      connection = socket.accept();
      //System.out.println("Client hat sich verbunden.");
      reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
      objectWriter = new ObjectOutputStream(connection.getOutputStream());
      objectReader = new ObjectInputStream(connection.getInputStream());

      //nötiges schicken
      String command = reader.readLine();
      while (!command.equals("close")) {
        if (command == null || command.length() <= 0) {
          System.out.printf("End connection to %s\n", connection.getRemoteSocketAddress());
          connection.close();
        } else if (command.equals("open")) {
          String[] header = it.open();
          objectWriter.writeObject(header);
          objectWriter.flush();
        } else if (command.equals("bloomfilter")) {
          bloomFilter = (BloomFilter<Register[]>) objectReader.readObject();
        } else if (command.equals("next")) {
          Register[] header = getNext();
          objectWriter.writeObject(header);
          objectWriter.flush();
        }
        command = reader.readLine();
      }
      //Verbindung beenden
      writer.write("closed");
      writer.newLine();
      writer.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      closeSilently(connection);
      it.close();
    }
  }

  /**
   * Get the next Register filtered by bloom filter
   * @return
   */
  private Register[] getNext() {
    Register[] next;
    while ((next = it.next()) != null) {
      if (bloomFilter.mightContain(next)){
        return  next;
      }
    }

    return null;
  }

  private void closeSilently(Socket connection) {
    try {
      if (objectWriter != null) objectWriter.close();

      if (writer != null) writer.close();

      if (reader != null) reader.close();

      if (connection != null && !connection.isClosed()) connection.close();

      if (socket != null && !socket.isClosed()) socket.close();
      it.close();
    } catch (IOException e) {
      // Silent :)
    }
  }
}
