package verteilter_nested_loop_join;
import nested_loop_join.Register;
import nested_loop_join.Tablescan;
import nested_loop_join.NLJoin;

public class Main {

	public static void main(String[] args) {
		//Vorlesungen öffnen, Client erstellen, joinen
		Tablescan tab1 = new Tablescan("Vorlesungen.data");
		ClientProxy client = new ClientProxy("localhost", 5677);
		NLJoin join = new NLJoin(tab1, client);
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
}
