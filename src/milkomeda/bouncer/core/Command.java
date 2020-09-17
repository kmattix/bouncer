package milkomeda.bouncer.core;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command{

	public abstract String getName();

	public boolean canExecute(Member member){
		return  true;
	}

	public abstract void execute(String[] args, MessageReceivedEvent event);
}
