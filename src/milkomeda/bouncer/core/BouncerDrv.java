package milkomeda.bouncer.core;

import milkomeda.bouncer.core.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;

public class BouncerDrv{

	public static void main(String[] args) throws LoginException, InterruptedException{
		if(args.length != 4){
			System.out.println("Incorrect arguments");
			System.exit(1);
		}

		BouncerDB db = new BouncerDB(args[1], args[2], args[3]);
		db.readDataBase();

		JDA jda = JDABuilder.create(args[0], GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
				.addEventListeners(new GuildJoin(db), new GuildLeave(db), new GuildUpdateName(db))
				.addEventListeners(new GuildMemberJoin(db), new MessageReceived(db))
				.setActivity(Activity.watching("new members | !help"))
				.build();
		jda.awaitReady();

		// TODO: 9/3/2020 verify that there are no data base entries the JDA is not servicing
	}
}
