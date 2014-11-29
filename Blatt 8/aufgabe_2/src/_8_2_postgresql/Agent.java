package _8_2_postgresql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;

import org.postgresql.xa.PGXADataSource;

public class Agent {
	PGXADataSource ds;
	XAConnection xaConn;
	Connection conn;
	XAResource res;
	Xid xid;
	boolean abort;
	
	public Agent(int id, String url, String user, String passwd){
		abort = false;
		//inits
		try {
			Class.forName("org.postgresql.Driver");
			this.ds = new PGXADataSource();
			ds.setUrl(url);
			ds.setUser(user);
			ds.setPassword(passwd);
			
			this.xaConn = ds.getXAConnection();
			this.conn = xaConn.getConnection();
			this.res = xaConn.getXAResource();
			this.xid = XidImpl.getUniqueXid(1);
			res.start(xid, XAResource.TMNOFLAGS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void add(int kontonummer, double amount) throws SQLException{
		Double stand_alt = this.getAmount(kontonummer);
		if(stand_alt != null){
			double stand_neu = stand_alt + amount;
			PreparedStatement pstmt = 
			         conn.prepareStatement("UPDATE konto SET kontostand = " + stand_neu + "WHERE kontonummer =" + kontonummer);
			pstmt.executeUpdate();
		}else{
			this.abort = true;
		}
	}
	
	public int prepare() throws XAException{
		if (this.abort){
			return -1;
		}
		res.end(xid, XAResource.TMSUCCESS);
		return res.prepare (xid);
	}
	
	public void commit() throws XAException{
		res.commit(xid, false);
	}
	
	public void rollback() throws XAException{
		res.rollback(xid);
	}
	
	public void close() throws SQLException{
		conn.close();
		xaConn.close();
	}
	
	public Double getAmount(int kontonummer){
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement("SELECT kontostand FROM konto WHERE kontonummer = " + kontonummer);
			ResultSet rs = pstmt.executeQuery();
			int count = 0;
			double kontostand = 0.0;
			while (rs.next()) {
				kontostand = rs.getDouble("kontostand");
				count++;
	        }
			if(count != 1){
				return null;
			}
			return kontostand;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}		
	}
}
