package me.itzmarcus.azuriteams.events;

import me.itzmarcus.azuriteams.managers.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class TeamStats implements Listener {

    TeamManager tm = new TeamManager();

    @EventHandler
    public void death(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        Player death = e.getEntity().getPlayer();
        if(tm.checkIfPlayerHasATeam(killer.getName())) {
            tm.increaseTeamKills(tm.getPlayerTeam(killer.getName()));
        }
        if(tm.checkIfPlayerHasATeam(death.getName())) {
            tm.increaseTeamDeaths(tm.getPlayerTeam(death.getName()));
        }
    }
}
