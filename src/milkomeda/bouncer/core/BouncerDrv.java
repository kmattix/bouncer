package milkomeda.bouncer.core;

import milkomeda.bouncer.core.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;

public class BouncerDrv{

	public static void main(String[] args) throws LoginException, InterruptedException, IOException {
		if(args.length != 1){
			System.out.println("Please provide a token as the first argument!");
			System.exit(1);
		}

		BouncerConfig cfg;
		File cfgFile = new File("bouncer.cfg");
		if(cfgFile.exists()) {
			cfg = BouncerConfig.readConfig(cfgFile);
			System.out.println(cfgFile.getName() + " read");
		}
		else{
			cfg = new BouncerConfig();
			System.out.println(cfgFile.getName() + " created");
		}

		JDA jda = JDABuilder.create(args[0], GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES)
				.addEventListeners(new GuildMemberJoin(), new MessageReceived(cfg))
				.setActivity(Activity.watching("for new members"))
				.build();
		jda.awaitReady();
	}
}
