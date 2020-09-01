package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerConfig;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoin extends ListenerAdapter{

	private BouncerConfig cfg;

	public GuildMemberJoin(BouncerConfig cfg){
		this.cfg = cfg;
	}

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event){
		System.out.println(event.getUser().getAsTag() + " joined");
		try{
			Role autoRole = event.getGuild().getRoleById(cfg.getAutoRoleID());
			event.getGuild().addRoleToMember(event.getMember(), autoRole).queue();
			System.out.println("Added " + autoRole.getName() + " to " + event.getUser().getAsTag());
		}catch(NumberFormatException e){
			System.out.println("No auto role specified");
		}
	}
}
