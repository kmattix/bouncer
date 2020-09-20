package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.data.util.GuildTable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Sub class of {@code Command} that is used to display the help embed.
 */
public class Help extends Command{

	private final GuildTable GT;
	private final String NAME = "help";
	private final String VERSION = "bouncer v1.1.13-beta";

	/**
	 * @param gt Guild table utility.
	 */
	public Help(GuildTable gt){
		GT = gt;
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
	public void execute(String[] args, MessageReceivedEvent event){
		MessageChannel channel = event.getChannel();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		User milkomeda = event.getJDA().getUserById(151488174323924992L);
		String prefix = GT.getCmdPrefix(event.getGuild().getIdLong());

		embedBuilder.setTitle(VERSION);
		embedBuilder.addField(
				"Info",
				"Bouncer is still under development, please contact the creator for issues or suggestions.\n" +
						"Bouncer is a member management bot designed to assign default roles to new members. Commands" +
						" with `this` appearance needs permissions to be used.", false);
		embedBuilder.addField(
				"Commands", String.format(
						"`%sautorole` {role name} | 'disable' | 'update' - create or add existing role, turn off, add all existing members to role.\n" +
								"`%sprefix` {char} - change the command prefix.\n" +
								"`%sban` {@mention} {#days} - bans a user for set number of days, or indefinite if left empty.\n" +
								"`%sunban` {tag} - removes a temp ban from a user. Tag is like user#1234.\n" +
								"%shelp", prefix, prefix, prefix, prefix, prefix), false);
		assert milkomeda != null;
		embedBuilder.setFooter("Created by " + milkomeda.getAsTag(), milkomeda.getAvatarUrl());
		channel.sendMessage(embedBuilder.build()).queue();
	}
}
