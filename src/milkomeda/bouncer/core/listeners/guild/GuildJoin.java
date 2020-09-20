package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.data.util.GuildTable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a guild is added to the JDA. Purpose is to initialize it to the database.
 */
public class GuildJoin extends ListenerAdapter{

	private final GuildTable GT;

	/**
	 * @param gt Guild table utility.
	 */
	public GuildJoin(GuildTable gt){
		GT = gt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGuildJoin(@NotNull net.dv8tion.jda.api.events.guild.GuildJoinEvent event){
		Guild guild = event.getGuild();
		GT.addGuild(guild.getIdLong(), guild.getName());
		System.out.println("Joined server " +  guild.getName() + " (ID " + guild.getIdLong() + ").");
	}
}
