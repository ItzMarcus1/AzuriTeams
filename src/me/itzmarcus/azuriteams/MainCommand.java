package me.itzmarcus.azuriteams;

import me.itzmarcus.azuriteams.managers.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

    TeamManager tm = new TeamManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase("team")) {
            Player p = (Player) sender;
            String player = p.getName();
            if(args.length == 0) {
                p.sendMessage("");
                p.sendMessage("§3> §b/team opret <team navn> §7- Opret et nyt team.");
                p.sendMessage("§3> §b/team forlad §7- Slet / forlad dit team.");
                p.sendMessage("");
            }
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("forlad")) {
                    if(!tm.checkIfPlayerHasATeam(player)) {
                        p.sendMessage(TeamManager.PREFIX + "§bDu er ikke medlem af noget team.");
                        return true;
                    }
                    String team = tm.getPlayerTeam(player);
                    if(team == null) {
                        p.sendMessage(TeamManager.PREFIX + "§cDu er ikke medlem af noget team.");
                        return true;
                    }else if(tm.getTeamLeader(tm.getPlayerTeam(player)).equals(player)) {
                        p.sendMessage(TeamManager.PREFIX + "§bDu var ejeren af dit team. Det er nu slettet.");
                        tm.removeTeam(tm.getPlayerTeam(player));
                        return true;
                    } else {
                        tm.leaveTeam(team, player);
                        p.sendMessage(TeamManager.PREFIX + "§bDu har forladt dit team!");
                        return true;
                    }
                }
            }
            if(args.length == 2) {
                if(args[0].equalsIgnoreCase("opret")) {
                    String teamName = args[1];
                    tm.createTeam(teamName, p);
                }
            }
        }
        return false;
    }
}
