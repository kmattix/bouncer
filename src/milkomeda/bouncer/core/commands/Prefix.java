package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.BouncerDB;
import milkomeda.bouncer.core.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Prefix extends Command{

	private final BouncerDB DB;
	private final String NAME = "prefix";

	public Prefix(BouncerDB db){
		DB = db;
	}

	@Override
	public String getName(){
		return NAME;
	}

	@Override
	public boolean canExecute(Member member){
		boolean result = false;
		if(member.hasPermission(Permission.ADMINISTRATOR))
			result = true;
		return result;
	}

	@Override
	public void execute(String[] args, MessageReceivedEvent event){
		MessageChannel channel = event.getChannel();
		long guildID = event.getGuild().getIdLong();
		if(args.length == 2 && args[1].length() == 1){
			DB.updateCmdPrefix(guildID, args[1]);
			channel.sendMessage("'" + DB.getCmdPrefix(guildID) + "' is the new command prefix!").queue();
		}
		else if(args.length == 1)
			channel.sendMessage("'" + DB.getCmdPrefix(guildID) + "' is the command prefix!").queue();
		else
			channel.sendMessage("'" + args[1] + "' is an invalid prefix!").queue();
	}
}
