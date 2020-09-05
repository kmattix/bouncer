package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceived extends ListenerAdapter{

	private BouncerDB db;
	private String prefix;

	public MessageReceived(BouncerDB db){
		this.db = db;
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event){
		Guild guild = event.getGuild();
		prefix = db.getCmdPrefix(guild.getIdLong());
		String[] args = event.getMessage().getContentRaw().split(" ");
		if(args[0].equalsIgnoreCase(prefix + "autorole"))
			autoRole(event, args);
		if(args[0].equalsIgnoreCase(prefix + "help"))
			help(event);
		if(args[0].equalsIgnoreCase(prefix + "prefix"))
			prefix(event, args);
	}

	private void autoRole(MessageReceivedEvent event, String[] args){
		MessageChannel channel = event.getChannel();
		long guildID = event.getGuild().getIdLong();
		if(args.length == 1){
			if(db.getRoleID(guildID) != 0)
				channel.sendMessage("Auto role is enabled!").queue();
			else
				channel.sendMessage("Auto role is disabled!").queue();
		}
		else if(args[1].equalsIgnoreCase("disable")){
			db.updateRoleID(guildID);
			channel.sendMessage("Auto role is disabled!").queue();
		}
		else if(args.length == 2){
			try{
				Role role = event.getGuild().getRolesByName(args[1], true).get(0);
				db.updateRoleID(guildID, role.getIdLong());
				channel.sendMessage(role.getAsMention() + " is now an auto role!").queue();
			}catch(IndexOutOfBoundsException e){
				channel.sendMessage(args[1] + " is not a valid role!").queue();
			}
		}
	}

	private void help(MessageReceivedEvent event){
		MessageChannel channel = event.getChannel();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		User milkomeda = event.getJDA().getUserById(151488174323924992L);
		embedBuilder.setTitle("bouncer help");
		embedBuilder.addField("Info", "'bouncer' is still under development, " +
				"please contact the creator for issues or suggestions.", false);
		embedBuilder.addField("Commands", String.format("%sautorole role name, disable\n%sprefix char\n%shelp",
				prefix, prefix, prefix), false);
		embedBuilder.setFooter("Created by " + milkomeda.getAsTag(), milkomeda.getAvatarUrl());
		channel.sendMessage(embedBuilder.build()).queue();
	}

	private void prefix(MessageReceivedEvent event, String[] args){
		MessageChannel channel = event.getChannel();
		long guildID = event.getGuild().getIdLong();
		if(args.length == 2 && args[1].length() == 1){
			db.updateCmdPrefix(guildID, args[1]);
			channel.sendMessage("'" + db.getCmdPrefix(guildID) + "' is the new command prefix!").queue();
		}
		else if(args.length == 1)
			channel.sendMessage("'" + db.getCmdPrefix(guildID) + "' is the command prefix!").queue();
		else
			channel.sendMessage("'" + args[1] + "' is an invalid prefix!").queue();
	}
}
