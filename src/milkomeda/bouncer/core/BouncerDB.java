package milkomeda.bouncer.core;

import java.sql.*;
import java.util.Date;

public class BouncerDB{
	private String ip, username, password;
	private Connection connect;


	public BouncerDB(String ip, String username, String password){
		this.ip = ip;
		this.username = username;
		this.password = password;
	}

	public void readDataBase() {
		try{
			connect = DriverManager.getConnection("jdbc:mysql://" + ip + "?user=" + username + "&password=" + password);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void addGuild(long guildID, String guildName, String cmdPrefix, long roleID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("INSERT INTO guild VALUES(%d,'%s','%s',%d,NOW());",
					guildID, guildName, cmdPrefix, roleID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
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
}
