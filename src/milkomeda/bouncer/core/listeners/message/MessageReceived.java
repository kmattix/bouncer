package milkomeda.bouncer.core.listeners.message;

import milkomeda.bouncer.core.commands.Command;
import milkomeda.bouncer.core.commands.*;
import milkomeda.bouncer.core.data.util.GuildTable;
import milkomeda.bouncer.core.data.util.UserBanTable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Class listener for when the JDA retrieves a message.
 *
 * @author Kevin Mattix
 * @date 11-10-2020
 */
public class MessageReceived extends ListenerAdapter{

	private final GuildTable GT;
	private final UserBanTable UBT;

	/**
	 * @param gt Guild table utility.
	 */
	public MessageReceived(GuildTable gt, UserBanTable ubt){
		GT = gt;
		UBT = ubt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event){
		String cmdPrefix = GT.getCmdPrefix(event.getGuild().getIdLong());
		String[] args = event.getMessage().getContentRaw().split(" ");
		try{
			// TODO: 9/17/2020 Figure out why charAt is throwing an exception for !help, but the function still works...
			if(Character.toString(args[0].charAt(0)).equals(cmdPrefix)){
				String command = args[0].substring(1).toLowerCase();
				Map<String, Command> commandMap = new HashMap<>();

				AutoRole autoRole = new AutoRole(GT);
				commandMap.put(autoRole.getName(), autoRole);
				Ban ban = new Ban(UBT);
				commandMap.put(ban.getName(), ban);
				UnBan unBan = new UnBan(UBT);
				commandMap.put(unBan.getName(), unBan);
				Prefix prefix = new Prefix(GT);
				commandMap.put(prefix.getName(), prefix);
				Help help = new Help(GT);
				commandMap.put(help.getName(), help);

				if(commandMap.containsKey(command) &&
						commandMap.get(command).canExecute(event.getMember()))
					commandMap.get(command).execute(args, event);
			}
		}catch(StringIndexOutOfBoundsException ignored){}
	}
}