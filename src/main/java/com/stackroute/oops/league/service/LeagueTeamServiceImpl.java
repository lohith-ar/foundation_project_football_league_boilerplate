package com.stackroute.oops.league.service;

import com.stackroute.oops.league.dao.PlayerDao;
import com.stackroute.oops.league.dao.PlayerDaoImpl;
import com.stackroute.oops.league.dao.PlayerTeamDao;
import com.stackroute.oops.league.dao.PlayerTeamDaoImpl;
import com.stackroute.oops.league.exception.PlayerAlreadyAllottedException;
import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.exception.TeamAlreadyFormedException;
import com.stackroute.oops.league.model.Player;
import com.stackroute.oops.league.model.PlayerTeam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

/**
 * This class implements leagueTeamService This has four fields: playerDao,
 * playerTeamDao and registeredPlayerList and playerTeamSet
 */
public class LeagueTeamServiceImpl implements LeagueTeamService {
    static ArrayList<Player> registeredPlayerList;
    static TreeSet<PlayerTeam> playerTeamSet;
    private static final String PLAYER_FILE_NAME = "src/main/resources/player.csv";
    private static final String TEAM_FILE_NAME = "src/main/resources/finalteam.csv";

    FileWriter fw;
    BufferedWriter bw;
    FileReader fr, fr1;
    BufferedReader br, br1;
    PlayerDao playerDao;
    PlayerTeamDao playerTeamDao;

    /**
     * Constructor to initialize playerDao, playerTeamDao empty ArrayList for
     * registeredPlayerList and empty TreeSet for playerTeamSet
     */
    public LeagueTeamServiceImpl() {
        playerDao = new PlayerDaoImpl();
        playerTeamDao = new PlayerTeamDaoImpl();
        registeredPlayerList = new ArrayList<Player>();
        playerTeamSet = new TreeSet<PlayerTeam>();
    }

    // Add player data to file using PlayerDao object
    @Override
    public boolean addPlayer(Player player) throws PlayerAlreadyExistsException {
        try {
            fw = new FileWriter(PLAYER_FILE_NAME);
            bw = new BufferedWriter(fw);
            if (player.getPassword().length() > 6 && player.getYearExpr() > 0) {
                bw.write(player.getPlayerId() + "," + player.getPlayerName() + "," + player.getYearExpr() + "\n");
                bw.close();
                fw.close();
                return true;
            } else
                return false;
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Register the player for the given teamTitle Throws PlayerNotFoundException if
     * the player does not exists Throws PlayerAlreadyAllottedException if the
     * player is already allotted to team Throws TeamAlreadyFormedException if the
     * maximum number of players has reached for the given teamTitle Returns null if
     * there no players available in the file "player.csv" Returns "Registered" for
     * successful registration Returns "Invalid credentials" when player credentials
     * are wrong
     */
    @Override
    public synchronized String registerPlayerToLeague(String playerId, String password, LeagueTeamTitles teamTitle)
            throws PlayerNotFoundException, TeamAlreadyFormedException, PlayerAlreadyAllottedException {
        // registeredPlayerList = new ArrayList<Player>();
        // List<Player> players = new ArrayList<Player>();
        // PlayerDao playerDao = new PlayerDaoImpl();

        // if (password != "password") {
        // return "Invalid credentials";
        // }
        // if (playerId == null || playerId.equals("")) {
        // throw new PlayerNotFoundException();
        // }
        // players = playerDao.getAllPlayers();
        // if (players.size() == 0)
        // return null;
        // Player player = new Player();
        // int noofLines = 0;
        // int flag = 0;

        // for (Player p : players) {
        // noofLines++;
        // if (!playerId.equals(p.getPlayerId())) {
        // flag++;
        // }
        // }
        // for (Player pl : players) {
        // if(pl.getPlayerId().equals(playerId)){
        // player.setPlayerId(playerId);
        // player.setPassword(password);
        // player.setTeamTitle(teamTitle.name());
        // registeredPlayerList.add(player);
        // }else{
        // if (flag == noofLines) {
        // throw new PlayerNotFoundException();
        // }
        // }
        // for (Player p2 : registeredPlayerList) {
        // System.out.println(p2);
        // }
        // return "Registered";
        // }
        // return null;
        List<Player> players = playerDao.getAllPlayers();
        if (players.isEmpty()) {
            return null;
        }
        Player p = playerDao.findPlayer(playerId);
        if (p.getPassword().equals(password)) {
            for (PlayerTeam pt : playerTeamDao.getAllPlayerTeams()) {
                if (pt.getPlayerId().equals(playerId) && pt.getTeamTitle() == null
                        || (!pt.getPlayerId().equals(playerId))) {
                    if (playerTeamDao.getPlayerSetByTeamTitle(teamTitle.name()).size() < 11) {
                        p.setTeamTitle(teamTitle.name());
                        registeredPlayerList.add(p);
                        playerTeamSet.add(new PlayerTeam(playerId, teamTitle.name()));
                        return "Registered";
                    } else {
                        throw new TeamAlreadyFormedException();
                    }
                } 
                else {
                    throw new PlayerAlreadyAllottedException();
                }
            }
        } else {
            return "Invalid credentials";
        } return null;
    }

    /**
     * Return the list of all registered players
     */
    @Override
    public List<Player> getAllRegisteredPlayers() {
        return registeredPlayerList;
    }

    /**
     * Return the existing number of players for the given title
     */
    @Override
    public int getExistingNumberOfPlayersInTeam(LeagueTeamTitles teamTitle) {
        return playerTeamDao.getPlayerSetByTeamTitle(teamTitle.name()).toArray().length;
    }

    /**
     * Admin credentials are authenticated and registered players are allotted to
     * requested teams if available If the requested teams are already formed,admin
     * randomly allocates to other available teams PlayerTeam object is added to
     * "finalteam.csv" file allotted by the admin using PlayerTeamDao Return "No
     * player is registered" when registeredPlayerList is empty Throw
     * TeamAlreadyFormedException when maximum number is reached for all teams
     * Return "Players allotted to teams" when registered players are successfully
     * allotted Return "Invalid credentials for admin" when admin credentials are
     * wrong
     */

    @Override
    public String allotPlayersToTeam(String adminName, String password, LeagueTeamTitles teamTitle)
            throws TeamAlreadyFormedException, PlayerNotFoundException {

        if (AdminCredentials.admin.equals(adminName) && AdminCredentials.password.equals(password)) {
            List<Player> playerList = playerDao.getAllPlayers();
            if (playerList.isEmpty()) {
                return "No player is registered";
            }
            int count = 0;
            for (Player player : playerList) {
                if (player.getTeamTitle() == null)
                    continue;
                if (player.getTeamTitle().equalsIgnoreCase(teamTitle.name())) {
                    count++;
                }
            }
            if (count >= 11)
                throw new TeamAlreadyFormedException();

            for (Player player : playerList) {
                if (player.getTeamTitle() == null) {
                    player.setTeamTitle(teamTitle.name());
                    playerTeamDao.addPlayerToTeam(player);
                }
            }
            return "Players allotted to teams";
        } else {
            return "Invalid credentials for admin";
        }
    }
    // public String allotPlayersToTeam(String adminName, String password,
    // LeagueTeamTitles teamTitle)
    // throws TeamAlreadyFormedException, PlayerNotFoundException {
    // if (adminName.equals(AdminCredentials.admin) &&
    // password.equals(AdminCredentials.password)) {
    // ArrayList<Player> templist = new ArrayList<Player>();
    // PlayerTeamDao playerTeamDao = new PlayerTeamDaoImpl();
    // Set<PlayerTeam> getplayerteam = playerTeamDao.getAllPlayerTeams();
    // ArrayList<Player> tempfinallist = new ArrayList<Player>();

    // for (Player p : registeredPlayerList) {
    // if (getplayerteam.size() == 0) {
    // p.setTeamTitle(teamTitle.name());
    // templist.add(p);
    // } else {
    // for (PlayerTeam pt : getplayerteam) {
    // String tempId = pt.getPlayerId().substring(16);
    // if (!p.getPlayerId().equals(tempId)) {
    // templist.add(p);
    // }
    // }
    // }
    // }
    // for (Player p : templist) {
    // System.out.println(p.toString());
    // }
    // method1(templist);
    // return "Players allotted to teams";
    // } else {
    // return "Invalid credentials for admin";
    // }
    // }

    // public void method1(ArrayList<Player> player) {
    // try {
    // fw = new FileWriter(TEAM_FILE_NAME);
    // bw = new BufferedWriter(fw);
    // for (Player p : player) {
    // PlayerTeam playerTeam = new PlayerTeam(p.getPlayerId(), p.getTeamTitle());
    // bw.append(playerTeam.toString() + "\n");
    // }
    // bw.close();
    // fw.close();
    // } catch (FileNotFoundException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    /**
     * static nested class to initialize admin credentials admin name='admin' and
     * password='pass'
     */
    static class AdminCredentials {
        private static String admin = "admin";
        private static String password = "pass";
    }
}
