package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceived extends ListenerAdapter{

	final private BouncerDB DB;
	private String prefix;

	public MessageReceived(BouncerDB db){
		DB = db;
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event){
		Guild guild = event.getGuild();
		prefix = DB.getCmdPrefix(guild.getIdLong());
		String[] args = event.getMessage().getContentRaw().split(" ");
		if(args[0].equalsIgnoreCase(prefix + "autorole") && isAdmin(event))
			autoRole(event, args);
		if(args[0].equalsIgnoreCase(prefix + "help"))
			help(event);
		if(args[0].equalsIgnoreCase(prefix + "prefix") && isAdmin(event))
			prefix(event, args);
	}

	private boolean isAdmin(MessageReceivedEvent event){
		boolean result = false;
		if(event.getMember().hasPermission(Permission.ADMINISTRATOR))
			result = true;
		return result;
	}

	private void autoRole(MessageReceivedEvent event, String[] args){
		MessageChannel channel = event.getChannel();
		long guildID = event.getGuild().getIdLong();
		if(args.length == 1){
			if(DB.getRoleID(guildID) != 0)
				channel.sendMessage("Auto role is enabled!").queue();
			else
				channel.sendMessage("Auto role is disabled!").queue();
		}
		else if(args[1].equalsIgnoreCase("disable")){
			DB.updateRoleID(guildID);
			channel.sendMessage("Auto role is disabled!").queue();
		}
		else if(args.length == 2){
			try{
				Role role = event.getGuild().getRolesByName(args[1], true).get(0);
				DB.updateRoleID(guildID, role.getIdLong());
				channel.sendMessage(args[1] + " is now an auto role! **IMPORTANT:** Move the bouncer's role" +
						" above the auto role or it wont be able to manage that role!").queue();
			}catch(IndexOutOfBoundsException e){
				createAutoRole(args[1], event);
			}
		}
	}

	private void createAutoRole(String roleName, MessageReceivedEvent event){
		Message message = new MessageBuilder().setContent("No pre-existing role found, do you wish to create a new role with permissions" +
				" copied from @ everyone?").build();
		event.getChannel().sendMessage(message).queue(msg -> {
			msg.addReaction("\u2705").queue();
			msg.addReaction("\u274E").queue();
		});
		MessageReactionAdd.setUser(event.getAuthor());
		MessageReactionAdd.setRoleName(roleName);
	}

	private void help(MessageReceivedEvent event){
		MessageChannel channel = event.getChannel();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		User milkomeda = event.getJDA().getUserById(151488174323924992L);

		embedBuilder.setTitle("bouncer v1.0.2-beta");
		embedBuilder.addField(
				"Info",
				"Bouncer is still under development, please contact the creator for issues or suggestions.\n" +
						"Bouncer is a member management bot designed to assign default roles to new members. Commands" +
						" with `this` appearance need administrator permissions to be used.", false);
		embedBuilder.addField(
				"Commands", String.format(
						"`%sautorole` {role name} | 'disable'\n" +
								"`%sprefix` {char}\n" +
								"%shelp", prefix, prefix, prefix), false);
		assert milkomeda != null;
		embedBuilder.setFooter("Created by " + milkomeda.getAsTag(), milkomeda.getAvatarUrl());
		channel.sendMessage(embedBuilder.build()).queue();
	}

	private void prefix(MessageReceivedEvent event, String[] args){
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