package milkomeda.bouncer.core.commands;

import milkomeda.bouncer.core.BouncerDB;
import milkomeda.bouncer.core.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

/**
 * Sub class of {@code Command} that is used to display the help embed.
 */
public class Help extends Command{

	private final BouncerDB DB;
	private final String NAME = "help";
	private final String VERSION = "bouncer v1.0.8-beta";

	/**
	 * @param db Database connection.
	 */
	public Help(BouncerDB db){
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
	public void execute(String[] args, MessageReceivedEvent event){
		MessageChannel channel = event.getChannel();
		EmbedBuilder embedBuilder = new EmbedBuilder();
		User milkomeda = event.getJDA().getUserById(151488174323924992L);
		String prefix = DB.getCmdPrefix(event.getGuild().getIdLong());

		embedBuilder.setTitle(VERSION);
		embedBuilder.addField(
				"Info",
				"Bouncer is still under development, please contact the creator for issues or suggestions.\n" +
						"Bouncer is a member management bot designed to assign default roles to new members. Commands" +
						" with `this` appearance need administrator permissions to be used.", false);
		embedBuilder.addField(
				"Commands", String.format(
						"`%sautorole` {role name} | 'disable' | 'update' - create or add existing role, turn off, add all existing members to role.\n" +
								"`%sprefix` {char} - change the command prefix.\n" +
								"%shelp", prefix, prefix, prefix), false);
		assert milkomeda != null;
		embedBuilder.setFooter("Created by " + milkomeda.getAsTag(), milkomeda.getAvatarUrl());
		channel.sendMessage(embedBuilder.build()).queue();
	}
}
