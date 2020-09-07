package milkomeda.bouncer.core.listeners;

import milkomeda.bouncer.core.BouncerDB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class RoleDelete extends ListenerAdapter{

    private BouncerDB DB;

    public RoleDelete(BouncerDB DB) {
        this.DB = DB;
    }

    @Override
    public void onRoleDelete(@NotNull RoleDeleteEvent event) {
        Guild guild = event.getGuild();
        if(event.getRole().getIdLong() == DB.getRoleID(guild.getIdLong()))
            DB.updateRoleID(guild.getIdLong());
    }
}
