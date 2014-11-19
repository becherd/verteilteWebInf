package _6_2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
      List<Register[]> resultList = new ArrayList<>();
      long time_start_gesamt = System.currentTimeMillis();
      long time_start_next = System.currentTimeMillis();
      String[] headers = join.open();
      Register[] result = join.next();
      long time_stop_next = System.currentTimeMillis();

      if (result != null){
        resultList.add(result);
      }

      while (result != null) {
      /*
      for(int i=0; i<result.length; i++){
				System.out.print(result[i] + "\t");
			}
			System.out.println("");
			*/
        resultList.add(result);
        result = join.next();

      }
      join.close();
      long time_stop_gesamt = System.currentTimeMillis();
      for (String h : headers){
        System.out.print(h+"\t");
      }
      System.out.println();

      for (Register[] register : resultList){
        for (Register r : register){
          System.out.print(r.getObject()+"\t");
        }
        System.out.println();
      }


      System.out.println("----------------------------------------");
      System.out.println("Zeit erstes Ergebnis: "+(time_stop_next - time_start_next)+" ms");
      System.out.println("Zeit gesamt: " + (time_stop_gesamt - time_start_gesamt) + " ms");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
