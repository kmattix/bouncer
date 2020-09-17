package milkomeda.bouncer.core.listeners.message;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a reaction is added.
 */
public class MessageReactionAdd extends ListenerAdapter{

    private static long msgId, userId;
    private static String roleName;
    private final BouncerDB DB;

    /**
     * @param db Database connection.
     */
    public MessageReactionAdd(BouncerDB db) {
        DB = db;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        if(roleName != null && msgId == event.getMessageIdLong() && userId == event.getUserIdLong()){
            if(event.getReactionEmote().getName().equals("\u2705")){
                Role newAutorole = event.getGuild().getPublicRole();
                newAutorole.createCopy().setName(roleName).queue(role ->{
                    DB.updateRoleID(event.getGuild().getIdLong(), role.getIdLong());
                });
                event.getTextChannel().sendMessage(roleName + " is now an auto role!").queue();
                setMsgId(0);
                setUserId(0);
                roleName = null;
            }
            else if(event.getReactionEmote().getName().equals("\u274E")){
                event.getTextChannel().sendMessage("Autorole was not created.").queue();
                setMsgId(0);
                setUserId(0);
                roleName = null;
            }
        }
    }

    /**
     * @param msgId Sets the message Id.
     */
    public static void setMsgId(long msgId) {
        MessageReactionAdd.msgId = msgId;
    }

    /**
     * @param userId Sets the user Id.
     */
    public static void setUserId(long userId) {
        MessageReactionAdd.userId = userId;
    }

    /**
     * @param roleName Sets the role name.
     */
    public static void setRoleName(String roleName) {
        MessageReactionAdd.roleName = roleName;
    }
}