package milkomeda.bouncer.core.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Data base connector class using the JDBC connector.
 */
public class DatabaseConnection{

	private final String IP, DATABASE, USERNAME, PASSWORD;
	public Connection connect;
	
	/**
	 * Use when connecting to an external IP.
	 *
	 * @param ip Valid IP for a MySQL server connection.
	 * @param database MySQL database name.
	 * @param username User for the MySQL database.
	 * @param password Password for the MySQL database.
	 */
	public DatabaseConnection(String ip, String database, String username, String password){
		IP = ip;
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	public DatabaseConnection(String database, String username, String password) {    // TODO: 9/6/2020 figure out why this doesn't work
		IP = "localhost";
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	/**
	 * Used to test the integrity of the database connection.
	 *
	 * @return      if the database is connected or not.
	 */
	public boolean testConnection(){
		boolean status;
		try{
			connect.createStatement();
			status = true;
		} catch(NullPointerException | SQLException e){
			status = false;
		}
		return status;
	}

	/**
	 * Uses the JDBC driver to connect to the data base.
	 */
	private void readDataBase() {
		try{
			connect = DriverManager.getConnection("jdbc:mysql://" + IP + "/" + DATABASE + "?user=" + USERNAME + "&password=" + PASSWORD);
		}catch(SQLException e){
			System.out.println("Wrong database information...");
		}
	}
}
