package milkomeda.bouncer.core.listeners.role;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Class listener for when a role gets deleted.
 */
public class RoleDelete extends ListenerAdapter{

    private final BouncerDB DB;

    /**
     * @param db Database connection.
     */
    public RoleDelete(BouncerDB db) {
        DB = db;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        Guild guild = event.getGuild();
        if(event.getRole().getIdLong() == DB.getRoleID(guild.getIdLong()))
            DB.updateRoleID(guild.getIdLong());
    }
}
