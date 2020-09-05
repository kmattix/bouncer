package milkomeda.bouncer.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BouncerDB{
	final private String IP, DATABASE, USERNAME, PASSWORD;
	private Connection connect;


	public BouncerDB(String ip, String database, String username, String password){
		IP = ip;
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	public BouncerDB(String database, String username, String password) {
		IP = "localhost";
		DATABASE = database;
		USERNAME = username;
		PASSWORD = password;
		readDataBase();
	}

	private void readDataBase() {
		try{
			connect = DriverManager.getConnection("jdbc:mysql://" + IP + "/" + DATABASE + "?user=" + USERNAME + "&password=" + PASSWORD);
		}catch(SQLException e){
			System.out.println("Wrong database information...");
		}
	}

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

	public void removeGuild(long guildID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate("DELETE FROM guild WHERE guild_id = " + guildID + ";");
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

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

	public void updateGuildName(long guildID, String guildName){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET guild_name = '%s', date_modified = NOW() WHERE guild_id = %d;", guildName, guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void updateCmdPrefix(long guildID, String cmdPrefix){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET cmd_prefix = '%s', date_modified = NOW() WHERE guild_id = %d;", cmdPrefix, guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void updateRoleID(long guildID, long roleID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET role_id = %d, date_modified = NOW() WHERE guild_id = %d;", roleID, guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	public void updateRoleID(long guildID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("UPDATE guild SET role_id = DEFAULT, date_modified = NOW() WHERE guild_id = %d;", guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

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
}
