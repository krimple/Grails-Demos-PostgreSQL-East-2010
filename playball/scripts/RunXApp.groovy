/**
 * Gant script that executes Grails using an embedded Jetty server
 * using additional project dependencies
 */

includeTargets << grailsScript("_GrailsRun")

target('default': "Runs a Grails application in Jetty") {
	overrideCompilerPaths()
    depends(checkVersion, configureProxy, packageApp, parseArguments)
    if(argsMap.https) {
        runAppHttps()
    }
    else {
        runApp()
    }
    watchContext()
}


def overrideCompilerPaths() {
	//closure below overrides part of the standard compile script to add the
	//sources of other required projects
	// nb copy in ScriptRunner
	compilerPaths = { String classpathId, boolean compilingTests ->

		def excludedPaths = ["views", "i18n", "conf"] // conf gets special handling
		for(dir in new File("${basedir}/grails-app").listFiles()) {
			if(!excludedPaths.contains(dir.name) && dir.isDirectory())
				src(path:"${dir}")
		}
		// Handle conf/ separately to exclude subdirs/package misunderstandings
		src(path: "${basedir}/grails-app/conf")
		// This stops resources.groovy becoming "spring.resources"
		src(path: "${basedir}/grails-app/conf/spring")		
		src(path:"${basedir}/src/groovy")
		src(path:"${basedir}/src/java")
		javac(classpathref:classpathId, debug:"yes")
		if(compilingTests) {
			src(path:"${basedir}/test/unit")
			src(path:"${basedir}/test/integration")
		}
	}
}
