package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.BouncerDB;
import milkomeda.bouncer.core.Command;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Sub class of {@code Command} that is used for the timed ban functionality.
 */
public class Ban extends Command{

	final private BouncerDB DB;
	final private String NAME = "ban";

	/**
	 * @param db Database connection.
	 */
	public Ban(BouncerDB db){
		DB = db;
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
		System.out.println("Ban");
	}
}
