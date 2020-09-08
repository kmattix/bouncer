package milkomeda.bouncer.core.listeners.message;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactionAdd extends ListenerAdapter{

    private static long msgId, userId;
    private static String roleName;
    private final BouncerDB DB;

    public MessageReactionAdd(BouncerDB db) {
        DB = db;
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        if(roleName != null && msgId == event.getMessageIdLong() && userId == event.getUserIdLong()){
            if(event.getReactionEmote().getName().equals("\u2705")){
                Role newAutorole = event.getGuild().getPublicRole();
                newAutorole.createCopy().setName(roleName).queue();
                DB.updateRoleID(event.getGuild().getIdLong(), newAutorole.getIdLong());
                event.getTextChannel().sendMessage(roleName + " is now an auto role! " +
                        "**IMPORTANT:** Move the bouncer's role above the auto role or it wont be able to manage that role!").queue();
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

    public static void setMsgId(long msgId) {
        MessageReactionAdd.msgId = msgId;
    }

    public static void setUserId(long userId) {
        MessageReactionAdd.userId = userId;
    }

    public static void setRoleName(String roleName) {
        MessageReactionAdd.roleName = roleName;
    }
}
