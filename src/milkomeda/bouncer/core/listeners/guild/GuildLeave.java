package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.data.util.GuildTable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a guild leaves the JDA. Purpose is to remove the guild from the database to save database
 * resources.
 */
public class GuildLeave extends ListenerAdapter{

	private final GuildTable GT;

	/**
	 * @param gt Guild table utility.
	 */
	public GuildLeave(GuildTable gt){
		GT = gt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGuildLeave(@NotNull GuildLeaveEvent event){
		Guild guild = event.getGuild();
		GT.remove(guild.getIdLong());
		System.out.println("Left server " + guild.getName() + " (ID " + guild.getIdLong() + ").");
	}
}
