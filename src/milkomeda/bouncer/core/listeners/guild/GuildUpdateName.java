package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a guild changes its name. Purpose is to update the databases entry in case a guild changes
 * its name.
 */
public class GuildUpdateName extends ListenerAdapter{

	private final BouncerDB DB;

	/**
	 * @param db Database connection.
	 */
	public GuildUpdateName(BouncerDB db){
		DB = db;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event){
		Guild guild = event.getGuild();
		DB.updateGuildName(guild.getIdLong(), event.getNewName());
	}
}