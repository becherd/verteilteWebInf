package _13_1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;


public class Pagerank {
	PGSimpleDataSource ds;
	Connection conn;
	double[][] P;
	int size;
	
	public Pagerank(String postgresql) throws ClassNotFoundException, SQLException, IOException{
		//inits
		Class.forName("org.postgresql.Driver");
		this.ds = new PGSimpleDataSource();
		ds.setUrl(postgresql);
		ds.setUser("postgres");
		ds.setPassword("postgres");
		this.conn = ds.getConnection();
		
		loadData();
		
		size = sizeHaltestellen();
		P = new double[size][size];
		System.out.println("size: " + size);
		
		// fill P
		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				P[i][j] = 0;
			}
		}
		fillP();
		// printP();
	}
	
	public void fillP() throws SQLException{
		PreparedStatement pstmt = 
				  conn.prepareStatement("SELECT * FROM verbindungen");
		ResultSet rs = pstmt.executeQuery();
		
		int start, ziel;
		int[] spaltensumme = new int[size];
		for(int i = 0; i<size; i++){
			spaltensumme[i] = 0;
		}
		
		while(rs.next()){
			start = rs.getInt("start");
			ziel = rs.getInt("ziel");
			P[ziel-1][start-1]++;
			spaltensumme[start-1]++;
		}
		
		for(int i=0; i<size; i++){
			for(int j = 0; j<size; j++){
				P[i][j] = P[i][j]/spaltensumme[j];
			}
		}
	}
	
	public void printP(){
		for(int i = 0; i<size; i++){
			for(int j = 0; j<size; j++){
				System.out.print(P[j][i] + " - ");
			}
			System.out.println("");
		}
	}
	
	public void loadData() throws IOException, SQLException{
		// load data to postgres database
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("pagerank.sql"), "ISO-8859-1"));
		String strLine = "";
		
		while((strLine = br.readLine()) != null){
			strLine = strLine.replace("`", "");
			strLine = strLine.replace("int(11) NOT NULL AUTO_INCREMENT", "SERIAL");
			strLine = strLine.replace("int(11)", "integer");
			// System.out.println(strLine);
			PreparedStatement pstmt = conn.prepareStatement(strLine);
			pstmt.execute();
		}
		br.close();
	}
	
	public int sizeHaltestellen() throws SQLException{
		PreparedStatement pstmt = 
				  conn.prepareStatement("SELECT count(*) AS count FROM haltestellen");
		ResultSet rs = pstmt.executeQuery();
		int count = 0;
		while (rs.next()) {
			count = rs.getInt("count");
        }
		return count;
	}
		
	public double[] berechne(double[] v){
		double[][] x = P.clone();
		
		int n = 1;
		
		double[] w = new double[size];
		// mult: v*x --> w
		for(int i = 0; i<size; i++){
			w[i] = 0.0;
			for(int j = 0; j<size; j++){
				w[i] += x[i][j]*v[j];
			}
		}
		while(!equalsVector(v, w)){
			v = w;			
			w = new double[size];
			
			// mult: v*x --> w
			for(int i = 0; i<size; i++){
				w[i] = 0.0;
				for(int j = 0; j<size; j++){
					w[i] += x[i][j]*v[j];
				}
			}
			n++;
		}
		
		System.out.println("Berechnung benötigte " + n + " Schritte.");
		
		return v;
	}
	
	public double[] getGarchingVector() throws SQLException{
		double[] v = new double[size];
		for(int i = 0; i<size; i++){
			v[i] = 0;
		}
		
		PreparedStatement pstmt = 
				  conn.prepareStatement("SELECT id FROM haltestellen WHERE name='Garching Forschungszentrum'");
		ResultSet rs = pstmt.executeQuery();
		rs.next();
		int position = rs.getInt("id") - 1;
		v[position] = 1;
		
		return v;
	}
	
	public double[] getVerteilterVector() throws SQLException {
		double[] v = new double[size];
		for(int i = 0; i<size; i++){
			v[i] = 0;
		}
		
		// 20%: Hauptbahnhof, Sendlinger Tor, Scheidplatz, Innsbrucker Ring, Odeonsplatz
		PreparedStatement pstmt = 
				  conn.prepareStatement("SELECT id FROM haltestellen WHERE name IN "
				  		+ "('Hauptbahnhof', 'Sendlinger Tor', 'Scheidplatz', 'Innsbrucker Ring', 'Odeonsplatz')");
		ResultSet rs = pstmt.executeQuery();
		for(int i = 0; i<5; i++){
			rs.next();
			int position = rs.getInt("id") - 1;
			v[position] = 0.2;
		}
		
		return v;
	}
	
	public void decode(double[] v) throws SQLException{
		PreparedStatement pstmt = 
				  conn.prepareStatement("SELECT * FROM haltestellen ORDER BY id");
		ResultSet rs = pstmt.executeQuery();
		
		int i = 0;
		while(rs.next()){
			i = rs.getInt("id");
			System.out.println(i + ": " + String.format("%.5f", v[i-1]) + " - "+ rs.getString("name"));
		}
	}
	
	private boolean equalsVector(double[] x, double[] y){
		if(x.length != y.length)
			return false;
		
		for(int i = 0; i<x.length; i++){
			if(!String.format("%.5f", x[i]).equals(String.format("%.5f", y[i])))
				return false;
		}
		return true;
	}
		
	public static void main(String[] args){
		try {
			Pagerank p1 = new Pagerank("jdbc:postgresql://127.0.0.1:5432/ubahn");
			
			System.out.println("Start in Hauptbahnhof, Sendlinger Tor, Scheidplatz, Innsbrucker Ring oder Odeonsplatz:");
			double[] w = p1.berechne(p1.getVerteilterVector());
			p1.decode(w);
			
			System.out.println("\nStart in Garching Forschungszentrum:");
			w = p1.berechne(p1.getGarchingVector());
			p1.decode(w);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
