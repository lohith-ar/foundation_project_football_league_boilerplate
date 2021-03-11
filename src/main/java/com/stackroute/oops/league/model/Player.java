package com.stackroute.oops.league.model;


/**
 * This class contains five fields playerId,playerName,password,yearExpr and
 * teamTitle It is a subclass of Comparable interface
 */
public class Player extends Thread implements Comparable{

    private String playerId;
    private String playerName;
    private String password;
    private int yearExpr;
    private String teamTitle;

    // Parameterized Constructor to initialize all properties
    public Player(String playerId, String playerName, String password, int yearExpr) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.password = password;
        this.yearExpr = yearExpr;
    }
    public Player(){
    }

    public Player(String playerId, String password, String teamTitle){
        this.playerId = playerId;
        this.password=password;
        this.teamTitle = teamTitle;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public String getPassword() {
        return this.password;
    }

    public int getYearExpr() {
        return this.yearExpr;
    }

    // Return teamTitle if it is present and not empty
    public String getTeamTitle() {
        if ( "".equals(this.teamTitle) )
            return null;
        else
            return this.teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    /**
     * This overridden method should return player details in below format
     * Player{playerId=xxxxx, playerName=xxxxxx,
     * yearExpr=xxxxxx,teamTitle=xxxxxxxx}-> if league team title is set
     * Player{playerId=xxxxx, playerName=xxxxxx, yearExpr=xxxxxx}-> if league team
     * title not set
     */
    @Override
    public String toString() {  

        if( "".equals(this.teamTitle)|| this.teamTitle ==null) {
            return "Player{playerId=" + this.playerId + ", playerName=" + this.playerName +  ", yearExpr=" + this.yearExpr + "}";    
           
        } else { 
            return "Player{playerId=" + playerId + ", playerName=" + this.playerName + ", yearExpr=" + this.yearExpr
            + ", teamTitle=" + this.teamTitle + "}";
             
        }
    }

    // Overridden equals method
    @Override
    public boolean equals(Object object) {
        return false;
    }

    // Overridden hashcode method
    @Override
    public int hashCode() {
        return 0;
    }

    // compares player object based on playerId
    @Override
    public int compareTo(Object o) {
        Player p = (Player) o;
        if (this.playerId.compareToIgnoreCase(p.playerId) > 0) {
            return 1;
        } else if (this.playerId.compareToIgnoreCase(p.playerId) == 0) {
            return 0;
        } else
            return -1;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


    public void setYearExpr(int yearExpr) {
        this.yearExpr = yearExpr;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
