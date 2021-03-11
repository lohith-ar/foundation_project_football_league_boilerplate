package com.stackroute.oops.league.dao;

import com.stackroute.oops.league.exception.PlayerAlreadyExistsException;
import com.stackroute.oops.league.exception.PlayerNotFoundException;
import com.stackroute.oops.league.model.Player;

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
import java.util.ArrayList;
import java.util.List;

/**
 * This class is implementing the PlayerDao interface This class has one field
 * playerList and a String constant for storing file name
 */
public class PlayerDaoImpl implements PlayerDao {
    private static final String PLAYER_FILE_NAME = "src/main/resources/player.csv";
    private static List<Player> playerList;

    static FileOutputStream fo;
    static ObjectOutputStream oo;
    static FileInputStream fi;
    static ObjectInputStream oi;

    FileReader fr,fr5;
    BufferedReader br,br5;
    FileWriter fw;
    BufferedWriter bw;

    /**
     * Constructor to initialize an empty ArrayList for playerList
     */
    public PlayerDaoImpl() {
        playerList = new ArrayList<>();

    }

    /**
     * Return true if player object is stored in "player.csv" as comma separated
     * fields successfully when password length is greater than six and yearExpr is
     * greater than zero
     */
    @Override
    public boolean addPlayer(Player player) throws PlayerAlreadyExistsException {
        
        // try {
        //     fo = new FileOutputStream(PLAYER_FILE_NAME);

        //     oo = new ObjectOutputStream(fo);
        //     if (player.getPassword().length() > 6 && player.getYearExpr() > 0) {
        //         oo.writeObject(player + "\n");
        //         System.out.println(player);
        //         oo.close();
        //         fo.close();
        //         return true;
        //     } else
        //         return false;
        // } catch (FileNotFoundException e) {
        //     System.out.println(e.getMessage());
        //     return false;
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        //     return false;
        // }

        try {
        fw = new FileWriter(PLAYER_FILE_NAME);
        bw = new BufferedWriter(fw);
        if (player.getPassword().length() > 6 && player.getYearExpr() > 0) {
        bw.write(player.getPlayerId() + "," + player.getPlayerName() + "," +
        player.getYearExpr() + "\n");
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

    // Return the list of player objects by reading data from the file "player.csv"
    @Override
    public List<Player> getAllPlayers() {
        Player[] tempPlayer = new Player[500];
        String[] stringSplit = null;
        String line5;
        

        try {
            fr5 = new FileReader(PLAYER_FILE_NAME);
            br5 = new BufferedReader(fr);
            int i = 0;
            while ((line5 = br5.readLine()) != null) {
                // line = br.readLine();
                stringSplit = line5.split(",");
                tempPlayer[i] = new Player();
                tempPlayer[i].setPlayerId(stringSplit[0]);
                tempPlayer[i].setPlayerName(stringSplit[1]);
                tempPlayer[i].setYearExpr(Integer.parseInt(stringSplit[2]));
                playerList.add(tempPlayer[i]);
                i++;
            }
            br.close();
            fr.close();
            return playerList;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return playerList;
    }

    /**
     * Return Player object given playerId to search
     */
    @Override
    public Player findPlayer(String playerId) throws PlayerNotFoundException {
        // Player[] tempPlayer = new Player[500];
        // String[] stringSplit = null;
        // String line;
        // int i = 0;
        // try {
        // br = new BufferedReader(new FileReader(PLAYER_FILE_NAME));
        // while ((line = br.readLine()) != null) {
        // stringSplit = line.split(",");
        // if (playerId.equalsIgnoreCase(stringSplit[0])) {
        // tempPlayer[i] = new Player();
        // tempPlayer[i].setPlayerId(stringSplit[0]);
        // tempPlayer[i].setPlayerName(stringSplit[1]);
        // tempPlayer[i].setYearExpr(Integer.parseInt(stringSplit[2]));
        // return tempPlayer[i];
        // } else if ("".equalsIgnoreCase(stringSplit[0])) {
        // throw new PlayerNotFoundException();
        // } else if (!playerId.equalsIgnoreCase(stringSplit[0])) {
        // throw new PlayerNotFoundException();
        // } else if (" ".equalsIgnoreCase(stringSplit[0])) {
        // throw new PlayerNotFoundException();
        // }
        // i++;
        // }
        // br.close();
        // fr.close();
        // } catch (Exception e) {
        // System.out.println(e.getMessage());
        // }
        // return tempPlayer[i];

        if(playerList.isEmpty()){
            throw new PlayerNotFoundException();
        }
        if (playerId == null) {
            throw new PlayerNotFoundException();
        } else {
            playerList = getAllPlayers();
            if(playerList.isEmpty()){
                throw new PlayerNotFoundException();
            }
            for (Player p : playerList) {
                if (playerId.equalsIgnoreCase(p.getPlayerId())) {
                    return p;
                } else if ("".equalsIgnoreCase(p.getPlayerId())) {
                    throw new PlayerNotFoundException();
                } else if (!playerId.equalsIgnoreCase(p.getPlayerId())) {
                    throw new PlayerNotFoundException();
                } else if (" ".equalsIgnoreCase(p.getPlayerId())) {
                    throw new PlayerNotFoundException();
                }
            }
        }
        return null;
    }
}
