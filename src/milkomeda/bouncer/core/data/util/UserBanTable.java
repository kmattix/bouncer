package milkomeda.bouncer.core.data.util;

import milkomeda.bouncer.core.data.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.util.Date;

/**
 * Utility class for the user_ban table.
 */
public class UserBanTable{

	private final DatabaseConnection DC;
	private Connection connect;

	public UserBanTable(DatabaseConnection dc){
		DC = dc;
		connect = DC.connect;
	}

	/**
	 * There is no primary key used because the user can be banned from multiple guilds.
	 * <p>For instance user A can be banned from guild A for 2 days and guild B for 5 days. This is to conserve the
	 * ability for this bot to be used on multiple server.</p>
	 *
	 * @param userID IdLong for a user that is banned.
	 * @param guildID IdLong that the user is banned on.
	 * @param userTag The discord tag for a banned used (milkomeda#0099).
	 * @param unbanDate Date when they are unbanned.
	 */
	public void addUserBan(long userID, long guildID, String userTag, Date unbanDate){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format(
					"INSERT INTO user_ban VALUES(%d, %d, '%s', FROM_UNIXTIME(%d));",
					userID, guildID, userTag, unbanDate.getTime() / 1000));
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * {@code userID} and {@code guildID} must match in the same query for the entry to be removed.
	 *
	 * @param userID IdLong for a user that is banned.
	 * @param guildID IdLong for the guild that user's ban is being removed.
	 */
	public void removeUserBan(long userID, long guildID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("DELETE FROM user_ban WHERE user_id = %d AND guild_id = %d;",
					userID, guildID));
			statement.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * {@code userTag} and {@code guildID} must match in the same query for the entry to be removed.
	 *
	 * @param userTag Discord tag for a user that is banned (milkomeda#0099).
	 * @param guildID IdLong for the guild that user's ban is being removed.
	 */
	public void removeUserBan(String userTag, long guildID){
		try{
			Statement statement = connect.createStatement();
			statement.executeUpdate(String.format("DELETE FROM user_ban WHERE user_tag = '%s' AND guild_id = %d;",
					userTag, guildID));
			statement.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	/**
	 * This method checks if a user is banned on a given guild.
	 * <p>The if a user has a ban entry, but the current date/time is past the ban date then the ban entry is removed
	 * and this method returns false.</p>
	 *
	 * @param userID IdLong for the user that is being checked.
	 * @param guildID IdLong for the guild that user is being checked on.
	 *
	 * @return      True if user is currently banned, false if user is not banned.
	 */
	public boolean isBanned(long userID, long guildID){
		boolean result = false;
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(
					String.format("SELECT UNIX_TIMESTAMP(unban_date) FROM user_ban WHERE user_id = %d AND guild_id = %d;",
							userID, guildID));
			while(resultSet.next()){
				long unbanTimeStamp = resultSet.getLong("UNIX_TIMESTAMP(unban_date)");
				if(unbanTimeStamp <= Instant.now().getEpochSecond())
					this.removeUserBan(userID, guildID);
				else
					result = true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * This method checks if a user is banned on a given guild.
	 * <p>The if a user has a ban entry, but the current date/time is past the ban date then the ban entry is removed
	 * and this method returns false.</p>
	 *
	 * @param userTag Discord tag for the user that is being checked (milkomeda#0099).
	 * @param guildID IdLong for the guild that user is being checked on.
	 *
	 * @return      True if user is currently banned, false if user is not banned.
	 */
	public boolean isBanned(String userTag, long guildID){
		boolean result = false;
		try{
			Statement statement = connect.createStatement();
			ResultSet resultSet = statement.executeQuery(
					String.format("SELECT UNIX_TIMESTAMP(unban_date) FROM user_ban WHERE user_tag = '%s' AND guild_id = %d;",
							userTag, guildID));
			while(resultSet.next()){
				long unbanTimeStamp = resultSet.getLong("UNIX_TIMESTAMP(unban_date)");
				if(unbanTimeStamp <= Instant.now().getEpochSecond())
					this.removeUserBan(userTag, guildID);
				else
					result = true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return result;
	}
}
