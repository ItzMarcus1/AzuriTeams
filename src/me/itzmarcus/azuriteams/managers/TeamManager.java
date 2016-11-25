package me.itzmarcus.azuriteams.managers;

import me.itzmarcus.azuriteams.Core;
import me.itzmarcus.azuriteams.utils.MyConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamManager {

    public static final String PREFIX = "§8[§3Teams§8] §r";
    
    static MyConfig file = Core.config;

    public static ArrayList<String> teams = new ArrayList<>();
    public static HashMap<String, List<String>> teamMembers = new HashMap<>();
    public static HashMap<String, String> teamLeaders = new HashMap<>();
    public static HashMap<String, Integer> teamKills = new HashMap<>();
    public static HashMap<String, Integer> teamDeaths = new HashMap<>();
    public static HashMap<String, Integer> teamPoints = new HashMap<>();

    public void createTeam(String name, Player creator) {
        if(name.length() < 3 || name.length() > 13) {
            creator.sendMessage(PREFIX + "§aDit Team navn skal være mere end 3 bogstaver og mindre end 13 bogstaver.");
            return;
        }
        if(name.contains("'") || name.contains("`") || name.contains(",") || name.contains("^") || name.contains(".") || name.contains("!") || name.contains("<") || name.contains(">") || name.contains("_") || name.contains("#") || name.contains("¤") || name.contains("%") || name.contains("&") || name.contains("/") || name.contains("(") || name.contains(")") || name.contains("=") || name.contains("?")) {
            creator.sendMessage(PREFIX + "§aDit Team navn må ikke indeholde specielle bogstaver.");
            return;
        }
        if(checkIfPlayerHasATeam(creator.getName())) {
            creator.sendMessage(PREFIX + "§aDu skal forlade dit nuværende team. §8(§6/team forlad§8)");
            return;
        }
        if(teams.contains(name)) {
            creator.sendMessage(PREFIX + "§aNavnet er allerede taget.");
            return;
        }
        if(name.equalsIgnoreCase("null")) {
            creator.sendMessage(PREFIX + "§aDit Team navn kan ikke være 'null'");
            return;
        }
        String player = creator.getName();
        String teamName = name;
        List<String> members = new ArrayList<String>();

        teams.add(teamName);
        teamMembers.put(teamName, members);
        teamLeaders.put(teamName, creator.getName());
        teamKills.put(teamName, 0);
        teamDeaths.put(teamName, 0);
        teamPoints.put(teamName, 0);
        Bukkit.getServer().broadcastMessage(PREFIX + "§c" + creator.getName() + " §ahar lavet et nyt Team §8(§6" + teamName + "§8)");
        saveTeam(teamName);
    }

    public void addMember(String team, String member) {
        String t = team;
        String player = member;
        List<String> newMembers = teamMembers.get(t);
        newMembers.add(player);
        teamMembers.put(t, newMembers);
        saveTeam(t);
    }

    public void kickMember(String team, String member) {
        String t = team;
        String player = member;
        List<String> newMembers = teamMembers.get(t);
        newMembers.remove(player);
        teamMembers.put(t, newMembers);
        Bukkit.getServer().getPlayer(player).sendMessage(PREFIX + "§aDu er blevet smidt ud af fra dit Team §8(§6" + t + "§8)");
        saveTeam(t);
    }

    public void saveTeam(String t) {
        file.set("teams." + t, t);
        file.set("teams." + t + ".members", teamMembers.get(t));
        file.set("teams." + t + ".kills", teamKills.get(t));
        file.set("teams." + t + ".deaths", teamDeaths.get(t));
        file.set("teams." + t + ".leader", teamLeaders.get(t));
        file.set("teams." + t + ".points", teamPoints.get(t));
        file.saveConfig();
    }

    public static void saveTeams() {
        for(String t : teams) {
            // Bukkit.getServer().broadcastMessage("Saving teams..."); // For debugging
            // Bukkit.getServer().broadcastMessage(t + ", members: " + teamMembers.get(t) + ", leader: " + teamLeaders.get(t)); // For debugging
            file.set("teams." + t, t);
            file.set("teams." + t + ".members", teamMembers.get(t));
            file.set("teams." + t + ".kills", teamKills.get(t));
            file.set("teams." + t + ".deaths", teamDeaths.get(t));
            file.set("teams." + t + ".leader", teamLeaders.get(t));
            file.set("teams." + t + ".points", teamPoints.get(t));
            file.saveConfig();
        }
    }

    public int getTeamKills(String team) {
        return teamKills.get(team);
    }

    public int getTeamPoints(String team) {
        return teamPoints.get(team);
    }

    public void giveTeamPoints(String team, int amount) {
        teamPoints.put(team, getTeamPoints(team) + amount);
    }

    public void removeTeamPoints(String team, int amount) {
        if(amount > getTeamPoints(team)) {
            teamPoints.put(team, 0);
            return;
        }
        teamPoints.put(team, getTeamPoints(team) - amount);
    }

    public int getTeamDeaths(String team) {
        return teamDeaths.get(team);
    }

    public double getTeamDeathsKDR(String team) {
        return teamDeaths.get(team);
    }

    public double getTeamKDR(String team) {
        double kd = getTeamKills(team) / getTeamDeathsKDR(team);
        //kd = Math.round(kd * 100);
        //kd = kd/100;
        return kd;
    }
    // Line 59
    public String getPlayerTeam(String player) {
        String teamName = "";
        for (String s : teams) {
            if (teamMembers.get(s).contains(player)) {
                teamName = s;
            }
            if (getTeamLeader(s) != null && getTeamLeader(s).contains(player)) { // line 153
                teamName = s;
            }
        }
        return teamName; // Line 67
    }

    public String getPlayerPrefixTeam(String player) {
        for(String s : teams) {
            if(checkIfPlayerHasATeam(player) && getTeamMembers(s).contains(player)) {
                if(getTeamKills(getPlayerTeam(player)) >= 2500) {
                    return "§6" + s + "§r";
                } else {
                    return "§3" + s + "§r";
                }
            } else if(checkIfPlayerHasATeam(player) && getTeamLeader(s).equals(player)) {
                if(getTeamKills(getPlayerTeam(player)) >= 2500) {
                    return "§6*" + s + "§r";
                } else {
                    return "§3*" + s + "§r";
                }
            }
        }
        return ""; // Line 67
    }

    public String getTeamLeader(String team) {
        return teamLeaders.get(team);
    }

    public List<String> getTeamMembers(String team) {
        return teamMembers.get(team);
    }

    static Map<Integer, String> data = new HashMap<>();
    static List<Integer> c = new ArrayList<>(data.keySet());

    public void loadAllInfo() {
        data.clear();
        c.clear();
        for(String s : teams) {
            if(!data.containsValue(s)) {
                data.put(getTeamKills(s), s);
                c.add(getTeamKills(s));
                Collections.sort(c);
                Collections.reverse(c);
            }
        }
    }

    /*
    public void sendInformation(Player player) {
        if(data.size() <= 0) {
            player.sendMessage("§8[§3DarkStarPvP§8] §cIntet at vise her...");
            return;
        }
        player.sendMessage("§3§l=-= §c§lTeam Top §3§l=-=");
        player.sendMessage("");
        int counter = 1;
        if(data.size() <= 10) {
            for(int i = 0; i < data.size(); i++) {
                if(counter == 1) {
                    player.sendMessage("§c§l★ §6" + counter + " §c§l★ §3" + data.get(c.get(i)) + " §7§l| §c§l" + c.get(i));
                    counter++;
                } else {
                    player.sendMessage("§8- §6" + counter + " §8- §3" + data.get(c.get(i)) + " §7§l| §c§l" + c.get(i));
                    counter++;
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                if(counter == 1) {
                    player.sendMessage("§c§l★ §6" + counter + " §c§l★ §3" + data.get(c.get(i)) + " §7§l| §c§l" + c.get(i));
                    counter++;
                } else {
                    player.sendMessage("§8- §6" + counter + " §8- §3" + data.get(c.get(i)) + " §7§l| §c§l" + c.get(i));
                    counter++;
                }
            }
        }
        player.sendMessage("");
        player.sendMessage("§3§l=-= §c§lTeam Top §3§l=-=");
    }
    */

    public void loadTeams() {
        for(String s : file.getConfigurationSection("teams").getKeys(false)) {
            teams.add(s);
            teamMembers.put(s, file.getStringList("teams." + s + ".members"));
            teamLeaders.put(s, file.getString("teams." + s + ".leader"));
            teamKills.put(s, file.getInt("teams." + s + ".kills"));
            teamDeaths.put(s, file.getInt("teams." + s + ".deaths"));
            teamPoints.put(s, file.getInt("teams." + s + ".points"));
        }
    }

    public void increaseTeamKills(String team) {
        teamKills.put(team, teamKills.get(team) + 1);
    }

    public void increaseTeamDeaths(String team) {
        teamDeaths.put(team, teamDeaths.get(team) + 1);
    }

    public void removeTeam(String team) {
        if(teams.contains(team)) {
            teams.remove(team);
            teamLeaders.remove(team);
            teamMembers.remove(team);
            teamKills.remove(team);
            teamDeaths.remove(team);
            teamPoints.remove(team);
            file.set("teams." + team, null);
            file.saveConfig();
        }
    }

    public void leaveTeam(String team, String member) {
        teamMembers.get(team).remove(member);
        saveTeam(team);
    }

    public boolean checkIfPlayerHasATeam(String player) {
        if(teams.size() == 0) {
            return false;
        }
        for(String s : teams) {
            if (teamMembers.get(s).contains(player) || teamLeaders.get(s).contains(player)) {
                return true;
            }
        }
        return false;
    }
}
