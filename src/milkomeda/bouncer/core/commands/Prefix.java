package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.data.DatabaseConnection;
import milkomeda.bouncer.core.data.util.GuildTable;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Sub class of {@code Command} that is used to change the command prefix.
 */
public class Prefix extends Command{

	private final GuildTable GT;
	private final String NAME = "prefix";

	public Prefix(GuildTable gt){
		GT = gt;
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
		if(member.hasPermission(Permission.ADMINISTRATOR))
			result = true;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void execute(String[] args, MessageReceivedEvent event){
		MessageChannel channel = event.getChannel();
		long guildID = event.getGuild().getIdLong();
		if(args.length == 2 && args[1].length() == 1){
			GT.updateCmdPrefix(guildID, args[1]);
			channel.sendMessage("'" + GT.getCmdPrefix(guildID) + "' is the new command prefix!").queue();
		}
		else if(args.length == 1)
			channel.sendMessage("'" + GT.getCmdPrefix(guildID) + "' is the command prefix!").queue();
		else
			channel.sendMessage("'" + args[1] + "' is an invalid prefix!").queue();
	}
}
