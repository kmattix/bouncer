package milkomeda.bouncer.core.listeners;

import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class GuildMemberLeave extends ListenerAdapter{

	@Override
	public void onGuildMemberLeave(@NotNull GuildMemberLeaveEvent event){
		System.out.println(event.getUser().getAsTag() + " left");
	}
}
