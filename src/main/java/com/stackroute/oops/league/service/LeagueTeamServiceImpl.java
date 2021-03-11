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

    /**
     * Constructor to initialize playerDao, playerTeamDao empty ArrayList for
     * registeredPlayerList and empty TreeSet for playerTeamSet
     */
    public LeagueTeamServiceImpl() {
        PlayerDao playerDao = new PlayerDaoImpl();
        PlayerTeamDao playerTeamDao = new PlayerTeamDaoImpl();
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
        // ArrayList<String> tempplayers = new ArrayList<String>();
        if (password != "password") {
            return "Invalid credentials";
        }
        String teamTitleNew = teamTitle.toString();
        // registeredPlayerList = new ArrayList<Player>();
        List<Player> players = new ArrayList<Player>();
        PlayerTeamDao playerTeamDao = new PlayerTeamDaoImpl();
        playerTeamSet = (TreeSet<PlayerTeam>) playerTeamDao.getAllPlayerTeams();

        if (playerId == null || playerId.equals("")) {
            throw new PlayerNotFoundException();
        }
        Player player = new Player();
        String line = "", line1 = "";
        String[] lineSplit;
        int noofLines = 0;
        int flag = 0;
        try {
            fr = new FileReader(PLAYER_FILE_NAME);
            br = new BufferedReader(fr);
            if ((line = br.readLine()) == null) {
                return null;
            }

            fr1 = new FileReader(PLAYER_FILE_NAME);
            br1 = new BufferedReader(fr1);

            while ((line1 = br1.readLine()) != null) {
                lineSplit = line1.split(",");
                noofLines++;
                if (!playerId.equals(lineSplit[0])) {
                    flag++;
                }
                if (playerId.equals(lineSplit[0])) {
                    PlayerDao pd = new PlayerDaoImpl();
                    players= (ArrayList<Player>) pd.getAllPlayers();
                        for(Player p : players){
                            if(p.getPlayerId().equals(playerId)){
                                throw new PlayerAlreadyAllottedException();
                            }
                        }

                    player.setPlayerId(playerId);
                    player.setPassword(password);
                    player.setTeamTitle(teamTitleNew);
                    registeredPlayerList.add(player);

                    for (Player p : registeredPlayerList) {
                        System.out.println(p);
                    }
                    
                    return "Registered";
                }
            }




            
            
            if (flag == noofLines) {
                throw new PlayerNotFoundException();
            }
            
            
            
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
        return 0;
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

    // @Override
    // public String allotPlayersToTeam(String adminName, String password, LeagueTeamTitles teamTitle)
    //         throws TeamAlreadyFormedException, PlayerNotFoundException {
    //     if (adminName.equals(AdminCredentials.admin) && password.equals(AdminCredentials.password)) {
    //         ArrayList<Player> templist = new ArrayList<Player>();
    //         PlayerTeamDao playerTeamDao = new PlayerTeamDaoImpl();
    //         Set<PlayerTeam> getplayerteam = playerTeamDao.getAllPlayerTeams();
    //         ArrayList<Player> tempfinallist = new ArrayList<Player>();

    //         for (Player p : registeredPlayerList) {
    //             if (getplayerteam.size() == 0) {
    //                 p.setTeamTitle(teamTitle.name());
    //                 // playerTeamDao.addPlayerToTeam(p);
    //                 templist.add(p);
    //                 // tempfinallist.add(p);
    //             } else {
    //                 for (PlayerTeam pt : getplayerteam) {
    //                     String tempId = pt.getPlayerId().substring(16);
    //                     if (!p.getPlayerId().equals(tempId)) {
    //                         // p.setTeamTitle(teamTitle.name());
    //                         // playerTeamDao.addPlayerToTeam(p);
    //                         templist.add(p);
    //                         // tempfinallist.add(p);
    //                     }
    //                 }
    //             }
    //         }
    //         // for(int i=0;i<tempfinallist.size(); i++){

    //         // }

    //         method1(templist);
    //         return "Players allotted to teams";
    //     } else {
    //         return "Invalid credentials for admin";
    //     }
    // }
    // public void method1(ArrayList<Player> player) {
    //     try {
    //         fw = new FileWriter(TEAM_FILE_NAME);
    //         bw = new BufferedWriter(fw);
    //         for (Player p : player) {
    //             PlayerTeam playerTeam = new PlayerTeam(p.getPlayerId(), p.getTeamTitle());
    //             bw.append(playerTeam.toString() + "\n");
    //         }
    //         bw.close();
    //         fw.close();
    //     } catch (FileNotFoundException e) {
    //         e.printStackTrace();
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }
    // }
    @Override
    public String allotPlayersToTeam(String adminName, String password, LeagueTeamTitles teamTitle)
            throws TeamAlreadyFormedException, PlayerNotFoundException {
        if (adminName.equals(AdminCredentials.admin) && password.equals(AdminCredentials.password)) {
            List<Player> allregPlayers = new ArrayList<Player>();
            int flag=0;
            allregPlayers = getAllRegisteredPlayers();
            if(allregPlayers.size()==0)
            {
                return "No player is registered";
            }
            for (int i = 0; i < allregPlayers.size(); i++) {
                if(allregPlayers.get(i).getPlayerId().equalsIgnoreCase(registeredPlayerList.get(i).getPlayerId()) && allregPlayers.get(i).getTeamTitle()!=null)
                {
                    flag=1;
                    method1(allregPlayers);
                }
            }
            if(flag==1){
                return "Players allotted to teams";
            }
        } else {
            return "Invalid credentials for admin";
        }
        return null;
    }
public void method1(List<Player> player) {
        try {
            FileWriter fw = new FileWriter("src/main/resources/finalteam.csv");
            BufferedWriter bw = new BufferedWriter(fw);
            for (Player p : player) {
                PlayerTeam playerTeam = new PlayerTeam(p.getPlayerId(), p.getTeamTitle());
                bw.append(playerTeam.toString() + "\n");
            }
            bw.close();
            fw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
