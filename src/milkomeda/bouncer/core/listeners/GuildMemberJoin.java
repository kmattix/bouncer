package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoin extends ListenerAdapter{

	private BouncerDB db;

	public GuildMemberJoin(BouncerDB db){
		this.db = db;
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event){
		Guild guild = event.getGuild();
		Role autoRole = event.getGuild().getRoleById(db.getRoleID(guild.getIdLong()));
		event.getGuild().addRoleToMember(event.getMember(), autoRole).queue();
	}
}
