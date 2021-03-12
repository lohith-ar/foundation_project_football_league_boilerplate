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

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements leagueTeamService This has four fields: playerDao,
 * playerTeamDao and registeredPlayerList and playerTeamSet
 */
public class LeagueTeamServiceImpl implements LeagueTeamService {
    static ArrayList<Player> registeredPlayerList;
    static Set<PlayerTeam> playerTeamSet;
    private static final String PLAYER_FILE_NAME = "src/main/resources/player.csv";

    FileWriter fw;
    BufferedWriter bw;
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
        List<Player> players = playerDao.getAllPlayers();
        if (players.isEmpty()) {
            return null;
        }
        Player p = playerDao.findPlayer(playerId);
        if(p == null){
            throw new PlayerNotFoundException();
        }
        if (p.getPassword().equals(password)) {
                    playerTeamSet = playerTeamDao.getAllPlayerTeams();
                    PlayerTeam playerTeam = new PlayerTeam(p.getPlayerId(), p.getTeamTitle());
                    if(playerTeamSet.contains(playerTeam)){
                        throw new PlayerAlreadyAllottedException();
                    }
                
                    if (playerTeamDao.getPlayerSetByTeamTitle(teamTitle.name()).size() < 11) {
                        p.setTeamTitle(teamTitle.name());
                        registeredPlayerList.add(p);
                        playerTeamSet.add(new PlayerTeam(playerId, teamTitle.name()));
                        return "Registered";
                    } else {
                        throw new TeamAlreadyFormedException();
                    }
        } else {
            return "Invalid credentials";
        } 
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

    /**
     * static nested class to initialize admin credentials admin name='admin' and
     * password='pass'
     */
    static class AdminCredentials {
        private static String admin = "admin";
        private static String password = "pass";
    }
}
