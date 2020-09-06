package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
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
		if(db.getRoleID(guild.getIdLong()) != 0){
			Role autoRole = event.getGuild().getRoleById(db.getRoleID(guild.getIdLong()));
			try{
				event.getGuild().addRoleToMember(event.getMember(), autoRole).queue();
			}catch(HierarchyException e){
				guild.getOwner().getUser().openPrivateChannel().flatMap(pChannel -> pChannel.sendMessage(
						"Verify the bouncer role is above your '" + autoRole.getName() + "' auto role" +
								" in " + db.getGuildName(guild.getIdLong()) + ". " + event.getUser().getAsTag() + " was" +
								" not added to the auto role.")).queue();
			}
		}
	}
}
