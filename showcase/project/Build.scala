import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "showcase"
    val appVersion      = "1.0"

	val appDependencies = Seq(
    	"pbaris" %% "play2-plugins-utils" % "0.0.1",
    	"pbaris" %% "play2-plugins-thumbs" % "1.0.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
		//resolvers += "pbaris" at "https://github.com/pbaris/m2-repo/raw/master"
		resolvers += "pbaris" at "file://" + Path.userHome.absolutePath + "/.m2/m2-repo"
    )
}
