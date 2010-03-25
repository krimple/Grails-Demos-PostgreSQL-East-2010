import org.playball.model.Season

target(main: "The description of the script goes here!") {

     def seasons = Season.findAll()
       seasons.each {
         it.delete()
       }

       2001.upto(2015) {
        def season = new Season (startDate: new Date("04/01/${it}"),
                                endDate: new Date("11/25/${it}"),
                                seasonLabel: "${it} Major League Season")
        season.save()
       }
}

setDefaultTarget(main)

