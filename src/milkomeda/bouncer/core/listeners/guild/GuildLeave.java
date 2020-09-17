package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a guild leaves the JDA. Purpose is to remove the guild from the database to save database
 * resources.
 */
public class GuildLeave extends ListenerAdapter{

	private final BouncerDB DB;

	/**
	 * @param db Database connection.
	 */
	public GuildLeave(BouncerDB db){
		DB = db;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event){
		Guild guild = event.getGuild();
		DB.removeGuild(guild.getIdLong());
		System.out.println("Left server " + guild.getName() + " (ID " + guild.getIdLong() + ").");
	}
}
