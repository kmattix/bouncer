package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.data.util.UserBanTable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Sub class of {@code Command} that is used for unbanning users, timed ban or not.
 *
 * @author Kevin Mattix
 * @date 11-10-2020
 */
public class UnBan extends Command{

	final private String NAME = "unban";
	final private UserBanTable UBT;

	/**
	 * @param ubt User ban table utility.
	 */
	public UnBan(UserBanTable ubt){
		UBT = ubt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName(){
		return NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean canExecute(Member member){
		boolean result = false;
		if(member.hasPermission(Permission.BAN_MEMBERS))
			result = true;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(String[] args, MessageReceivedEvent event){
		Guild guild = event.getGuild();
		String tag = "";
		for(int i = 1; i < args.length; i++){
			tag += args[i];
		}
		if(UBT.isBanned(tag, guild.getIdLong())){
			UBT.remove(tag, guild.getIdLong());
			event.getMessage().getChannel().sendMessage(tag + "'s ban has been removed.").queue();
		}
		else
			event.getMessage().getChannel().sendMessage("No bans have been removed.").queue();
	}
}
