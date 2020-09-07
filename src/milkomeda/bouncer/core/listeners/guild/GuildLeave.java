package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeave extends ListenerAdapter{

	private final BouncerDB DB;

	public GuildLeave(BouncerDB db){
		DB = db;
	}

	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event){
		Guild guild = event.getGuild();
		DB.removeGuild(guild.getIdLong());
		System.out.println("Left server " + guild.getName() + " (ID " + guild.getIdLong() + ").");
	}
}
