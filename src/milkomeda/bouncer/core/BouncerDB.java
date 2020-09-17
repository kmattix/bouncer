package milkomeda.bouncer.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data base retrieval class using the JDBC connector.
 */
public class BouncerDB{
	private final String IP, DATABASE, USERNAME, PASSWORD;
	private Connection connect;


	/**
	 * Use when connecting to an external IP.
	 *
	 * @param ip Valid IP for a MySQL server connection.
	 * @param database MySQL database name.
	 * @param username User for the MySQL database.
	 * @param password Password for the MySQL database.
	 */
	public BouncerDB(String ip, String database, String username, String password){
		IP = ip;
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	public BouncerDB(String database, String username, String password) {    // TODO: 9/6/2020 figure out why this doesn't work
		IP = "localhost";
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	/**
	 * @param guildID IdLong for a guild given by a listener.
	 * @param guildName The corresponding guild name.
	 */
	public void addGuild(long guildID, String guildName){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("INSERT INTO guild VALUES(%d,'%s',DEFAULT,DEFAULT,NOW());",
					guildID, guildName));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * @param guildID IdLong for a guild that is to be deleted from the database.
	 */
	public void removeGuild(long guildID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate("DELETE FROM guild WHERE guild_id = " + guildID + ";");
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Checks the differences between the databases guilds and JDA guilds and adds or removes from the database if there
	 * are any disparities.
	 *
	 * @param jda The current JDA instance that contains the guilds being serviced.
	 */
	public void updateGuildEntries(JDA jda){
		List<Guild> actualGuilds = jda.getGuilds();
		List<Long> actualGuildIds = new ArrayList<>();
		for(Guild actualGuild : actualGuilds){
			actualGuildIds.add(actualGuild.getIdLong());
		}
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT guild_id FROM guild");
			List<Long> dataGuildIds = new ArrayList<>();
			while(resultSet.next()){
				dataGuildIds.add(resultSet.getLong("guild_id"));
			}
			int entriesRemoved = 0;
			for(long dataIndex : dataGuildIds){
				if(!actualGuildIds.contains(dataIndex)){
					removeGuild(dataIndex);
					entriesRemoved++;
				}
			}
			int entriesAdded = 0;
			for(long actualIndex : actualGuildIds){
				if(!dataGuildIds.contains(actualIndex)){
					addGuild(actualIndex, jda.getGuildById(actualIndex).getName());
					entriesAdded++;
				}
			}
			if(entriesRemoved > 0)
				System.out.println("Removed " + entriesRemoved + " old entries from database.");
			if(entriesAdded > 0)
				System.out.println("Added " + entriesAdded + " new entries to database.");
			statement.close();
			resultSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Does not return a guild name if the Id is not in the data base.
	 *
	 * @param guildID IdLong to retrieve the corresponding guild name.
	 *
	 * @return      Guild name from corresponding Id.
	 */
	public String getGuildName(long guildID){
		String output = "";
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT guild_name FROM guild WHERE guild_id =" + guildID + ";");
			resultSet.next();
			output = resultSet.getString("guild_name");
			statement.close();
			resultSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Does not return a command prefix if the Id is not in the data base.
	 *
	 * @param guildID IdLong to retrieve the corresponding command prefix.
	 *
	 * @return      Command prefix from the corresponding Id.
	 */
	public String getCmdPrefix(long guildID){
		String output = "";
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT cmd_prefix FROM guild WHERE guild_id =" + guildID + ";");
			resultSet.next();
			output = resultSet.getString("cmd_prefix");
			statement.close();
			resultSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Does not return an auto role Id if the guild Id is not in the data base.
	 *
	 * @param guildID IdLong to retrieve the corresponding auto role Id.
	 *
	 * @return      Auto role Id from the corresponding Id.
	 */
	public long getRoleID(long guildID){
		long output = 0;
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT role_id FROM guild WHERE guild_id =" + guildID + ";");
			resultSet.next();
			output = resultSet.getLong("role_id");
			statement.close();
			resultSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Does not return a timestamp if the Id is not in the data base.
	 *
	 * @param guildID IdLong to retrieve a timestamp for the last date modified.
	 *
	 * @return      Timestamp from the corresponding Id.
	 */
	public Date getDateModified(long guildID){
		Date output = null;
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT date_modified FROM guild WHERE guild_id =" + guildID + ";");
			resultSet.next();
			output = resultSet.getDate("date_modified");
			statement.close();
			resultSet.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return output;
	}

	/**
	 * Will not update if the Id is invalid.
	 *
	 * @param guildID Corresponding Id.
	 * @param guildName Guild name attached to the Id.
	 */
	public void updateGuildName(long guildID, String guildName){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET guild_name = '%s', date_modified = NOW() WHERE guild_id = %d;", guildName, guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Will not update if the Id is invalid.
	 *
	 * @param guildID Corresponding Id.
	 * @param cmdPrefix Command prefix attached to the Id.
	 */
	public void updateCmdPrefix(long guildID, String cmdPrefix){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET cmd_prefix = '%s', date_modified = NOW() WHERE guild_id = %d;", cmdPrefix, guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Will not update if the guild Id is invalid.
	 *
	 * @param guildID Corresponding guild Id.
	 * @param roleID Auto role Id attached to the guild Id.
	 */
	public void updateRoleID(long guildID, long roleID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET role_id = %d, date_modified = NOW() WHERE guild_id = %d;", roleID, guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Overloaded method that changes the auto role Id to 0.
	 *
	 * @param guildID Corresponding guild Id.
	 */
	public void updateRoleID(long guildID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET role_id = DEFAULT, date_modified = NOW() WHERE guild_id = %d;", guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
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
