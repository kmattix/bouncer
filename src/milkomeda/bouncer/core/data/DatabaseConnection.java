package milkomeda.bouncer.core.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Data base connector class using the JDBC connector.
 */
public class DatabaseConnection{

	private final String IP, DATABASE, USERNAME, PASSWORD;
	private Connection connection;
	
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

	// TODO: 9/6/2020 figure out why this doesn't work
	public DatabaseConnection(String database, String username, String password){
		IP = "localhost";
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	/**
	 * @return The data base connection.
	 */
	public Connection getConnection(){
		return connection;
	}

	/**
	 * Used to test the integrity of the database connection.
	 *
	 * @return      if the database is connected or not.
	 */
	public boolean testConnection(){
		boolean status;
		try{
			connection.createStatement();
			status = true;
		}catch(NullPointerException | SQLException e){
			status = false;
		}
		return status;
	}

	/**
	 * Uses the JDBC driver to connect to the data base.
	 */
	private void readDataBase(){
		try{
			connection = DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?autoReconnect=true",
					IP, DATABASE), USERNAME, PASSWORD);
		}catch(SQLException e){
			System.out.println("Wrong database information...");
		}
	}
}
