package milkomeda.bouncer.core.listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceived extends ListenerAdapter{

	private String discriminator = "!";

	@Override
	public void onMessageReceived(@NotNull MessageReceivedEvent event){
		Message message = event.getMessage();
		String[] args = message.getContentRaw().split(" ");
		if(args[0].equalsIgnoreCase("!test"))
			test(event, args);
	}

	private void test(MessageReceivedEvent event, String[] args){
		for(int i = 0; i < args.length; i++){
			System.out.println(args[i]);
		}
	}

	public void setDiscriminator(String discriminator){
		this.discriminator = discriminator;
	}
}
