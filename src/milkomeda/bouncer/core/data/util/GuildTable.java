package milkomeda.bouncer.core.data.util;

import milkomeda.bouncer.core.data.DatabaseConnection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for the guilds table.
 */
public class GuildTable{

	private final Connection CONNECT;

	public GuildTable(DatabaseConnection dc){
		CONNECT = dc.getConnection();
	}

	/**
	 * @param guildID IdLong for a guild given by a listener.
	 * @param guildName The corresponding guild name.
	 */
	public void add(long guildID, String guildName){
		try{
			Statement statement = CONNECT.createStatement();
			statement.executeUpdate(String.format("INSERT INTO guild VALUES(%d,'%s',DEFAULT,DEFAULT,NOW());",
					guildID, guildName));
			statement.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * @param guildID IdLong for a guild that is to be deleted from the database.
	 */
	public void remove(long guildID){
		try{
			Statement statement = CONNECT.createStatement();
			statement.executeUpdate("DELETE FROM guild WHERE guild_id = " + guildID + ";");
			statement.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * Checks the differences between the databases guilds and JDA guilds and adds or removes from the database if there
	 * are any disparities.
	 *
	 * @param jda The current JDA instance that contains the guilds being serviced.
	 */
	public void update(JDA jda){
		List<Guild> actualGuilds = jda.getGuilds();
		List<Long> actualGuildIds = new ArrayList<>();
		for(Guild actualGuild : actualGuilds){
			actualGuildIds.add(actualGuild.getIdLong());
		}
		try{
			Statement statement = CONNECT.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT guild_id FROM guild;");
			List<Long> dataGuildIds = new ArrayList<>();
			while(resultSet.next()){
				dataGuildIds.add(resultSet.getLong("guild_id"));
			}
			int entriesRemoved = 0;
			for(long dataIndex : dataGuildIds){
				if(!actualGuildIds.contains(dataIndex)){
					remove(dataIndex);
					entriesRemoved++;
				}
			}
			int entriesAdded = 0;
			for(long actualIndex : actualGuildIds){
				if(!dataGuildIds.contains(actualIndex)){
					add(actualIndex, jda.getGuildById(actualIndex).getName());
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
			Statement statement = CONNECT.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT guild_name FROM guild WHERE guild_id =" + guildID + ";");
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
			Statement statement = CONNECT.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT cmd_prefix FROM guild WHERE guild_id =" + guildID + ";");
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
			Statement statement = CONNECT.createStatement();
			ResultSet resultSet = statement
					.executeQuery("SELECT role_id FROM guild WHERE guild_id =" + guildID + ";");
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
	 * Will not update if the Id is invalid.
	 *
	 * @param guildID Corresponding Id.
	 * @param guildName Guild name attached to the Id.
	 */
	public void updateGuildName(long guildID, String guildName){
		try{
			Statement statement = CONNECT.createStatement();
			statement.executeUpdate(String
					.format("UPDATE guild SET guild_name = '%s', date_modified = NOW() WHERE guild_id = %d;",
							guildName, guildID));
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
			Statement statement = CONNECT.createStatement();
			statement.executeUpdate(String
					.format("UPDATE guild SET cmd_prefix = '%s', date_modified = NOW() WHERE guild_id = %d;",
							cmdPrefix, guildID));
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
			Statement statement = CONNECT.createStatement();
			statement.executeUpdate(String
					.format("UPDATE guild SET role_id = %d, date_modified = NOW() WHERE guild_id = %d;",
							roleID, guildID));
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
			Statement statement = CONNECT.createStatement();
			statement.executeUpdate(String
					.format("UPDATE guild SET role_id = DEFAULT, date_modified = NOW() WHERE guild_id = %d;", guildID));
			statement.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
}
