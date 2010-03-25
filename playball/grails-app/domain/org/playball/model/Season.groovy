package org.playball.model

class Season {

  static constraints = {
    seasonLabel(size:1..150, blank: false )
    startDate(validator: { value, domain ->
      if (value == null)
         return ["default.null.message"]
      if (domain.endDate < value) {
        return ["daterangeInvalid", value, domain.endDate]
      }
    })
    endDate(null: false)
  }

  static mapping = {
    table 'seasons'
    version false
    id column: 'season_id'
    cache true
    startDate column: 'starting_date'
    endDate column: 'ending_date'
    seasonLabel column: 'season', index: 'season_idx'
  }

    String seasonLabel

    Date startDate

    Date endDate
  
}
