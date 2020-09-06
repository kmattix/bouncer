package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildUpdateName extends ListenerAdapter{
	final private BouncerDB DB;

	public GuildUpdateName(BouncerDB db){
		this.DB = db;
	}

	@Override
	public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event){
		Guild guild = event.getGuild();
		DB.updateGuildName(guild.getIdLong(), event.getNewName());
	}
}
