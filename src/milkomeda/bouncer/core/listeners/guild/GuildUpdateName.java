package milkomeda.bouncer.core.listeners.guild;

import milkomeda.bouncer.core.data.util.GuildTable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a guild changes its name. Purpose is to update the databases entry in case a guild changes
 * its name.
 *
 * @author Kevin Mattix
 * @date 11-10-2020
 */
public class GuildUpdateName extends ListenerAdapter{

	private final GuildTable GT;

	/**
	 * @param gt Database connection.
	 */
	public GuildUpdateName(GuildTable gt){
		GT = gt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event){
		Guild guild = event.getGuild();
		GT.updateGuildName(guild.getIdLong(), event.getNewName());
	}
}
