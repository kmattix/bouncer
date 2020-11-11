package milkomeda.bouncer.core.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Abstract {@code Command} super class used as a template for child commands.
 *
 * @author Kevin Mattix
 * @date 11-10-2020
 */
public abstract class Command{

	/**
	 * @return      Name of the command that will be used as a key in the {@code commandMap}.
	 */
	public abstract String getName();

	/**
	 * @param member The object from the {@code MessageReceivedEvent}.
	 *
	 * @return      True if the member has given permission, false if not.
	 */
	public boolean canExecute(Member member){
		return  true;
	}

	/**
	 * @param args Arguments from a valid user command.
	 * @param event Event from the command message.
	 */
	public abstract void execute(String[] args, MessageReceivedEvent event);
}
