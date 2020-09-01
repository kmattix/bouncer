package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerConfig;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MessageReceived extends ListenerAdapter{

	private String prefix;
	private BouncerConfig cfg;

	public MessageReceived(BouncerConfig cfg) {
		this.cfg = cfg;
		this.prefix = cfg.getCmdPrefix();
	}

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event){
		Message message = event.getMessage();
		String[] args = message.getContentRaw().split(" ");
		if(args[0].equalsIgnoreCase(prefix + "autorole"))
			autoRole(event, args);
	}

	private void autoRole(MessageReceivedEvent event, String[] args){
		MessageChannel channel = event.getChannel();
		if(args.length == 1){
			if(cfg.isAutoRoleEnabled())
				channel.sendMessage("Auto role is enabled!").queue();
			else
				channel.sendMessage("Auto role is disabled!").queue();
		}
		else if(args[1].equalsIgnoreCase("disable")){
			cfg.setAutoRoleID(null);
			channel.sendMessage("Auto role is disabled!").queue();
			System.out.println("Auto role is disabled");
		}
		else if(args.length == 2){
			try{
				Role role = event.getGuild().getRolesByName(args[1], true).get(0);
				cfg.setAutoRoleID(role.getId());
				channel.sendMessage(role.getAsMention() + " is now an auto role!").queue();
				System.out.println(role.getName() + " is now an auto role");
			}catch(IndexOutOfBoundsException e){
				channel.sendMessage(args[1] + " is not a valid role!").queue();
			}
		}
		try{
			cfg.writeConfig();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
