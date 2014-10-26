package column_store;

public class Main {

	public static void main(String[] args) {
		test_join();
		test_column_store();
	}
	public static void test_join(){
		Tablescan tablescan = new Tablescan("Vorlesungen.data");
		Tablescan tablescan2 = new Tablescan("Professoren.data");
		NLJoin join = new NLJoin(tablescan, tablescan2);
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
	public static void test_column_store(){
             Tablescan tablescan = new Tablescan("Vorlesungen.data");
             ColumnStore cs = new ColumnStore(tablescan);
             cs.print();
        }

}
