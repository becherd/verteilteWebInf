package _6_2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Hannes Dorfmann
 */
public class BloomFilterServer extends Thread {

  DBIterator iterator;
  ServerSocket socket;

  public BloomFilterServer(DBIterator iterator, int port) throws IOException {
    this.iterator = iterator;
    socket = new ServerSocket(port);
  }



  public void run(){

    try {
      Socket client = socket.accept();


    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (socket != null){
        try {
          socket.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }




}
