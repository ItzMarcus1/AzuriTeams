package me.itzmarcus.azuriteams.events;

import me.itzmarcus.azuriteams.managers.TeamManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    TeamManager tm = new TeamManager();

    @EventHandler
    public void chat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if(tm.checkIfPlayerHasATeam(p.getName())) {
            e.setFormat("§8[§7" + tm.getPlayerPrefixTeam(p.getName()) + "§8] §r" + e.getFormat());
        }
    }
}
