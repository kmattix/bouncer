package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildJoin extends ListenerAdapter{
	final private BouncerDB DB;

	public GuildJoin(BouncerDB db){
		DB = db;
	}

	@Override
	public void onGuildJoin(@NotNull net.dv8tion.jda.api.events.guild.GuildJoinEvent event){
		Guild guild = event.getGuild();
		DB.addGuild(guild.getIdLong(), guild.getName());
		System.out.println("Joined server " +  guild.getName() + " (ID " + guild.getIdLong() + ").");
	}
}
