package milkomeda.bouncer.core.listeners.message;

import milkomeda.bouncer.core.BouncerDB;
import milkomeda.bouncer.core.Command;
import milkomeda.bouncer.core.commands.AutoRole;
import milkomeda.bouncer.core.commands.Help;
import milkomeda.bouncer.core.commands.Prefix;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Class listener for when the JDA retrieves a message.
 */
public class MessageReceived extends ListenerAdapter{

	private final BouncerDB DB;

	/**
	 * @param db Database connection.
	 */
	public MessageReceived(BouncerDB db){
		DB = db;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event){
		String cmdPrefix = DB.getCmdPrefix(event.getGuild().getIdLong());
		String[] args = event.getMessage().getContentRaw().split(" ");
		try{
			if(Character.toString(args[0].charAt(0)).equals(cmdPrefix)){ // TODO: 9/17/2020 Figure out why charAt is throwing an exception for !help, but the function still works...
				String command = args[0].substring(1);
				Map<String, Command> commandMap = new HashMap<>();

				AutoRole autoRole = new AutoRole(DB);
				commandMap.put(autoRole.getName(), autoRole);
				Prefix prefix = new Prefix(DB);
				commandMap.put(prefix.getName(), prefix);
				Help help = new Help(DB);
				commandMap.put(help.getName(), help);

				if(commandMap.containsKey(command) &&
						commandMap.get(command).canExecute(event.getMember()))
					commandMap.get(command).execute(args, event);
			}
		}catch(StringIndexOutOfBoundsException ignored){}
	}
}