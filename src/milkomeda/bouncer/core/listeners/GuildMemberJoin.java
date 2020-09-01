package milkomeda.bouncer.core.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberJoin extends ListenerAdapter{

	@Override
	public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event){
		System.out.println(event.getUser().getName() + " joined");
		Role memberRole = event.getGuild().getRoleById("693344316470591508");
		event.getGuild().addRoleToMember(event.getMember(), memberRole).queue();
		System.out.println("Added " + memberRole.getName() + " to " + event.getUser().getName());
	}
}
