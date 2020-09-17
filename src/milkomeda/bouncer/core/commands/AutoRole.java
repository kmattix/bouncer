package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.BouncerDB;
import milkomeda.bouncer.core.Command;
import milkomeda.bouncer.core.listeners.message.MessageReactionAdd;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class AutoRole extends Command{

	final private BouncerDB DB;
	final private String NAME = "autorole";

	public AutoRole(BouncerDB db){
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
		else if(DB.getRoleID(guildID) != 0 && args[1].equalsIgnoreCase("update"))
			updateMembers(event.getGuild());
		else if(DB.getRoleID(guildID) == 0 && args.length == 2){
			try{
				Role role = event.getGuild().getRolesByName(args[1], true).get(0);
				DB.updateRoleID(guildID, role.getIdLong());
				channel.sendMessage(args[1] + " is now an auto role! **IMPORTANT:** Move the bouncer's role" +
						" above the auto role or it wont be able to manage that role!").queue();
			}catch(IndexOutOfBoundsException e){
				createAutoRole(args[1], event);
			}
		}
		else{
			channel.sendMessage("Auto role is already enabled!").queue();
		}
	}

	private void createAutoRole(String roleName, MessageReceivedEvent event){
		Message message = new MessageBuilder().setContent("No pre-existing role found, do you wish to create a new role with permissions" +
				" copied from @ everyone?").build();
		event.getChannel().sendMessage(message).queue(msg -> {
			msg.addReaction("\u2705").queue();
			msg.addReaction("\u274E").queue();
			MessageReactionAdd.setMsgId(msg.getIdLong());
		});
		MessageReactionAdd.setUserId(event.getMember().getIdLong());
		MessageReactionAdd.setRoleName(roleName);
	}

	private void updateMembers(Guild guild){
		for(Member member : guild.getMemberCache()) {
			List<Role> memberRoles = member.getRoles();
			if(!memberRoles.contains(guild.getRoleById(DB.getRoleID(guild.getIdLong())))){
				Role autoRole = guild.getRoleById(DB.getRoleID(guild.getIdLong()));
				assert autoRole != null;
				guild.addRoleToMember(member, autoRole).queue();
			}
		}
	}
}
