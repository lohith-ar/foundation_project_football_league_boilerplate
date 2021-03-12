package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.dao.PlayerDao;

import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;
import com.stackroute.oops.league.model.PlayerTeam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class implements the PlayerTeamDao interface This class has two fields
 * playerTeamSet,playerDao and a String constant for storing file name.
 */
public class PlayerTeamDaoImpl implements PlayerTeamDao {
    private static final String TEAM_FILE_NAME = "src/main/resources/finalteam.csv";
    static FileInputStream fi;
    static ObjectInputStream oi;
    static FileOutputStream fo;
    static ObjectOutputStream oo;
    static FileWriter fw;
    static BufferedWriter bw;
    static FileReader fr;
    static BufferedReader br;
    PlayerDao playerDao;
    TreeSet<PlayerTeam> playerTeamSet;

    /**
     * Constructor to initialize an empty TreeSet and PlayerDao object
     */
    public PlayerTeamDaoImpl() {
        playerTeamSet = new TreeSet<PlayerTeam>();
        playerDao = new PlayerDaoImpl();
    }

    /*
     * Returns the list of players belonging to a particular teamTitle by reading
     * from the file finalteam.csv
     */
    @Override
    public Set<PlayerTeam> getPlayerSetByTeamTitle(String teamTitle) {
        Set<PlayerTeam> tempPlayer = new TreeSet<PlayerTeam>();
        Set<PlayerTeam> resPlayer = new TreeSet<PlayerTeam>();
        for(PlayerTeam p: tempPlayer){
            if(p.getTeamTitle().equals(teamTitle)){
                resPlayer.add(p);
            }
        }
        return resPlayer;
    }

    // Add the given PlayerTeam Object to finalteam.csv file
    @Override
    public boolean addPlayerToTeam(Player player) throws PlayerNotFoundException {
        PlayerTeam playerTeam = new PlayerTeam(player.getPlayerId(), player.getTeamTitle());
        try {
            fw = new FileWriter(TEAM_FILE_NAME);
            bw = new BufferedWriter(fw);
            bw.write(playerTeam.toString() + "\n");
            bw.close();
            fw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Return the set of all PlayerTeam by reading the file content from
    // finalteam.csv file
    @Override
    public Set<PlayerTeam> getAllPlayerTeams() {
        String line = "";
        String[] stringSplit;
        PlayerTeam[] pTeam = new PlayerTeam[500];
        try {
            fr = new FileReader(TEAM_FILE_NAME);
            br = new BufferedReader(fr);
            int i = 0;
            while ((line = br.readLine()) != null) {
                stringSplit = line.split(",");
                pTeam[i] = new PlayerTeam();
                pTeam[i].setPlayerId(stringSplit[0]);
                pTeam[i].setTeamTitle(stringSplit[1]);
                playerTeamSet.add(pTeam[i]);
                i++;
            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return playerTeamSet;
    }
}
