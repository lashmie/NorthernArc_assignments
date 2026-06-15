package aggregation.classassigment;

import aggregation.Employee;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Team {
    private String name;
    private  String nation;
    private Set<Player> playersList;
    Team(String name,String nation){
        this.name=name;
        this.nation =nation;
        this.playersList= new HashSet<>();

    }

    public String getName() {
        return name;
    }
    public void plays(Player p){
        playersList.add(p);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }


    public void detail() {
        System.out.println("The team name is "+this.getName()+" and their nation is "+this.getNation());
    }
    public Set<Player> getPlayersList(){
        return playersList;
    }



}
