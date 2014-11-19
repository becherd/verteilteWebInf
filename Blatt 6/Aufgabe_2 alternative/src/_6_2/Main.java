package _6_2;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    System.out.println("\n----------------------------------------");
    System.out.println("BloomFilter-Join");
    System.out.println("----------------------------------------");
    testBloomFilterJoin();
  }

  public static void testBloomFilterJoin() {

    //
    try {

      // Right side (big data)
      BloomFilterServerProxy server = new BloomFilterServerProxy(5677, new Tablescan("R.data"));
      server.start();

      BloomFilterClientProxy clientProxy = new BloomFilterClientProxy("localhost", 5677);

      // Left side
      BloomFilterJoin join = new BloomFilterJoin(new Tablescan("S.data"), clientProxy);

      //Zeitmessung
      long time_start_gesamt = System.currentTimeMillis();
      long time_start_next = System.currentTimeMillis();
      String[] op = join.open();
      /*
      for(int i=0; i<op.length; i++){
        System.out.print(op[i] + "\t");
      }
      System.out.println("");
      */
      Register[] result = join.next();
      long time_stop_next = System.currentTimeMillis();
      while (result != null) {
      /*
      for(int i=0; i<result.length; i++){
				System.out.print(result[i] + "\t");
			}
			System.out.println("");
			*/
        result = join.next();
      }
      join.close();
      long time_stop_gesamt = System.currentTimeMillis();
      System.out.println("Zeit gesamt: " + (time_stop_gesamt - time_start_gesamt) + " ms");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
