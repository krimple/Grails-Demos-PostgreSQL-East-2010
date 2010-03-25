package org.playball.model

class Team {

    String name
    
    static belongsTo = [league:League, homeStadium:Stadium]

    static hasMany = [gamesForHomeTeam: Game, gamesForVisitingTeam: Game]
    static constraints = {
    }


}
