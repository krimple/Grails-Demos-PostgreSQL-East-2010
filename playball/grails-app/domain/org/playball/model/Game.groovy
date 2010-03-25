package org.playball.model

class Game {

    static belongsTo = [homeTeam:Team,
            visitorTeam:Team, stadium:Stadium]

    Date scheduledDate

    Date actualDate

    static constraints = {
    }
}
