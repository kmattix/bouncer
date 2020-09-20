package milkomeda.bouncer.core.listeners.role;

import milkomeda.bouncer.core.data.util.GuildTable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a role gets deleted.
 */
public class RoleDelete extends ListenerAdapter{

    private final GuildTable GT;

    /**
     * @param gt Guild table utility.
     */
    public RoleDelete(GuildTable gt) {
        GT = gt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        Guild guild = event.getGuild();
        if(event.getRole().getIdLong() == GT.getRoleID(guild.getIdLong()))
            GT.updateRoleID(guild.getIdLong());
    }
}
