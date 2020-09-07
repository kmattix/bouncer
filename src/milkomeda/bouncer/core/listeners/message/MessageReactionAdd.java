package milkomeda.bouncer.core.listeners.message;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReactionAdd extends ListenerAdapter{

    private static User user;
    private static String roleName;
    private final BouncerDB DB;

    public MessageReactionAdd(BouncerDB db) {
        DB = db;
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event){
        if(roleName != null && user.getIdLong() == event.getUserIdLong()){  // TODO: 9/6/2020 this could cause a unique bug where someone reacts with the set emotes on another message it triggers this
            if(event.getReactionEmote().getName().equals("\u2705")){
                Role newAutorole = event.getGuild().getPublicRole();
                newAutorole.createCopy().setName(roleName).queue();
                DB.updateRoleID(event.getGuild().getIdLong(), newAutorole.getIdLong());
                event.getTextChannel().sendMessage(roleName + " is now an auto role! " +
                        "**IMPORTANT:** Move the bouncer's role above the auto role or it wont be able to manage that role!").queue();
                user = null;
                roleName = null;
            }
            else if(event.getReactionEmote().getName().equals("\u274E")){
                event.getTextChannel().sendMessage("Autorole was not created.").queue();
                user = null;
                roleName = null;
            }
        }
    }

    public static void setUser(User user) {
        MessageReactionAdd.user = user;
    }

    public static void setRoleName(String roleName) {
        MessageReactionAdd.roleName = roleName;
    }
}
