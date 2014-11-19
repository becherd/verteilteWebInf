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

public class BloomFilterServerProxy extends Thread {
  private ServerSocket socket;
  private DBIterator it;
  private BufferedReader reader;
  private BufferedWriter writer;
  private ObjectOutputStream objectWriter;
  private ObjectInputStream objectReader;
  private BloomFilter<Integer> clientBloomFilter;
  private int joinAttributeIndex;

  public BloomFilterServerProxy(int port, DBIterator it) throws IOException {
    this.socket = new ServerSocket(port);
    this.it = it;
  }

  public void run() {
    try {
      //Verbindung erstellen; Reader initialisieren
      Socket connection = socket.accept();
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
        } else if (command.startsWith("bloomfilter")) {
          joinAttributeIndex = Integer.parseInt(command.split(" ")[1]);
          clientBloomFilter = (BloomFilter<Integer>) objectReader.readObject();
        } else if (command.equals("next")) {
          Register[] item = getNextFiltered();
          objectWriter.writeObject(item);
          objectWriter.flush();
        }
        command = reader.readLine();
      }
      //Verbindung beenden
      writer.write("closed");
      writer.newLine();
      writer.flush();
      objectWriter.close();
      writer.close();
      reader.close();
      connection.close();
      socket.close();
      it.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (IOException e) {
      }
      it.close();
    }
  }

  /**
   * Get the next filtered (is in the bloom filter)
   */
  private Register[] getNextFiltered() {

    Register[] next;
    while ((next = it.next()) != null) {
      if (clientBloomFilter.mightContain(next[joinAttributeIndex].getInt())) {
        return next;
      }
    }
    return null;
  }
}
