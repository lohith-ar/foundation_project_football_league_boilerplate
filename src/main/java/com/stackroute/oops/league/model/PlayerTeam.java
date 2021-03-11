package com.stackroute.oops.league.model;

import java.io.Serializable;

/**
 * This class contains four fields playerId,teamTitle,season and experience.
 * This is a subclass of Serializable and Comparable interface
 */
public class PlayerTeam implements Serializable, Comparable {
    private String playerId;
    private String teamTitle;
    // private String season;
    // private int experience;


    //Parameterized Constructor to initialize all properties
    public PlayerTeam(String playerId, String teamTitle) {
        this.playerId = playerId;
        this.teamTitle = teamTitle;
        // this.season = season;
        // this.experience = experience;
    }

   public PlayerTeam(){
       
   }
    public String getPlayerId() {
        return this.playerId;
    }

    public String getTeamTitle() {
        return this.teamTitle;
    }

    /**
     * This overridden method should return player details in below format
     * Player{playerId=xxxxx, teamTitle=xxxxxx}
     */
    @Override
    public String toString() {
        return "Player{playerId="+playerId+", teamTitle="+teamTitle+"}";
    }

    //Overridden compare method based on playerId
    @Override
    public int compareTo(Object object) {
        PlayerTeam p = (PlayerTeam) object;
        if(this.playerId.compareToIgnoreCase(p.playerId)>0){
            return 1;
        }else if(this.playerId.compareToIgnoreCase(p.playerId)==0){
            return 0;
        }else 
        return -1;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }
}
