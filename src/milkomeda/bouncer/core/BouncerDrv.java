package milkomeda.bouncer.core;

import milkomeda.bouncer.core.data.DatabaseConnection;
import milkomeda.bouncer.core.data.util.GuildTable;
import milkomeda.bouncer.core.data.util.UserBanTable;
import milkomeda.bouncer.core.listeners.guild.GuildJoin;
import milkomeda.bouncer.core.listeners.guild.GuildLeave;
import milkomeda.bouncer.core.listeners.guild.GuildMemberJoin;
import milkomeda.bouncer.core.listeners.guild.GuildUpdateName;
import milkomeda.bouncer.core.listeners.message.MessageReactionAdd;
import milkomeda.bouncer.core.listeners.message.MessageReceived;
import milkomeda.bouncer.core.listeners.role.RoleDelete;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

/**
 * Entry class for JDA initialization.
 */
public class BouncerDrv{

	/**
	 * @param args Expecting a MySQL login info in order of IP, database, username, and password split by spaces.
	 *
	 * @throws LoginException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws LoginException, InterruptedException{
		if(args.length < 4 || args.length > 5){
			System.out.println("Incorrect arguments");
			System.exit(1);
		}

		DatabaseConnection dc;
		if(args.length == 5) dc = new DatabaseConnection(args[1], args[2], args[3], args[4]);
		else dc = new DatabaseConnection(args[1], args[2], args[3]);

		GuildTable gt = new GuildTable(dc);
		UserBanTable ubt = new UserBanTable(dc);

		JDA jda = JDABuilder.create(args[0], GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
				.addEventListeners(new GuildJoin(gt), new GuildLeave(gt), new GuildUpdateName(gt), new RoleDelete(gt))
				.addEventListeners(new GuildMemberJoin(gt, ubt), new MessageReceived(gt, ubt), new MessageReactionAdd(gt))
				.setActivity(Activity.watching("new members | !help"))
				.build();
		jda.awaitReady();

		if(dc.testConnection()) {
			gt.updateGuildEntries(jda);
			System.out.println("Database connected successfully...");
		}
		else{
			System.out.print("Check launch parameters and database status...");
			System.exit(1);
		}
	}
}
