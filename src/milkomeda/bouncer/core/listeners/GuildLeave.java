package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildLeave extends ListenerAdapter{

	private BouncerDB db;

	public GuildLeave(BouncerDB db){
		this.db = db;
	}

	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event){
		Guild guild = event.getGuild();
		db.removeGuild(guild.getIdLong());
		System.out.println("Left server " + guild.getName() + " (ID " + guild.getIdLong() + ")");
	}
}
