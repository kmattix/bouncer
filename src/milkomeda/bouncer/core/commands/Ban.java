package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.data.DatabaseConnection;
import milkomeda.bouncer.core.data.util.UserBanTable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Calendar;
import java.util.Date;

/**
 * Sub class of {@code Command} that is used for the timed ban functionality.
 */
public class Ban extends Command{

	final private UserBanTable UBT;
	final private String NAME = "ban";

	/**
	 * @param ubt User ban table utility.
	 */
	public Ban(UserBanTable ubt){
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
		try{
			Member member = event.getMessage().getMentionedMembers().get(0);
			if(args.length == 2)
				guild.ban(member, 0).queue();
			else if(args.length == 3){
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new Date());
				calendar.add(Calendar.DATE, Integer.parseInt(args[2]));
				UBT.addUserBan(member.getIdLong(), guild.getIdLong(), member.getUser().getAsTag(), calendar.getTime());
				guild.kick(member).queue();
			}
		} catch(IndexOutOfBoundsException | NumberFormatException e){
			event.getMessage().getChannel().sendMessage("Please use a valid statement.").queue();
		}
	}
}
