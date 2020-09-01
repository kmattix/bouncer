package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerConfig;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
		System.out.println("autorole test");
	}
}
