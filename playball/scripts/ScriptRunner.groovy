
// see http://amorproximi.blogspot.com/2008/07/more-on-grails-bootstrapping.html
import org.codehaus.groovy.grails.support.PersistenceContextInterceptor
import org.springframework.orm.hibernate3.SessionFactoryUtils
import org.springframework.orm.hibernate3.SessionHolder
import org.springframework.transaction.support.TransactionSynchronizationManager


includeTargets << grailsScript("_GrailsInit")
includeTargets << new File("${grailsHome}/scripts/Bootstrap.groovy")

target('default': "Runs scripts in the test/local directory") {

grailsHome = Ant.antProject.properties."env.GRAILS_HOME"

    //we need one arg, the script to run, with more args optional. For example, running:
    //>grails script-runner Merge arg1 arg2
    //will run $PROJECT_ROOT/scripts/Merge.groovy with the fully 
    //bootstrapped environment
    if (!args) {
        throw new RuntimeException("[fail] This script requires an argument - the script to run.")
    }

    String[] argsSplit=args.split("\n")
    String[] argsRest=[]
    if (argsSplit.size()>1) {
    	argsRest = argsSplit[1..argsSplit.size()-1]
    }
    
    overrideCompilerPaths()
    
    depends(configureProxy, packageApp, classpath, bootstrap)
    classLoader = new URLClassLoader([classesDir.toURI().toURL()] as URL[], rootLoader)
    Thread.currentThread().setContextClassLoader(classLoader)
    loadApp()
    configureApp()
    configureHibernateSession()

    def interceptor = null
    def beanNames = appCtx.getBeanNamesForType(PersistenceContextInterceptor)
    if (beanNames && beanNames.size() == 1) {
        interceptor = appCtx.getBean(beanNames[0])
    }
    try {        
        interceptor?.init()
        def scriptFile = "scripts/${argsSplit[0]}.groovy"
        if (!new File(scriptFile).exists()) {
          throw new IllegalArgumentException("File does not exist.")
        }
        new GroovyScriptEngine(ant.antProject.properties."base.dir", classLoader)
            .run(scriptFile, new Binding(argsRest))
        interceptor?.flush()
    } catch (Exception e) {
        e.printStackTrace()
        interceptor?.clear()
    } finally {
        interceptor?.destroy()
    }
}

def configureHibernateSession() {
	// without this you'll get a lazy initialization exception when using a many-to-many relationship
	def sessionFactory = appCtx.getBean("sessionFactory")
	def session = SessionFactoryUtils.getSession(sessionFactory, true)
	TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session))
}

def overrideCompilerPaths() {
	//closure below overrides part of the standard compile script to add the
	//sources of other required projects
	// nb copy in RunXApp
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
