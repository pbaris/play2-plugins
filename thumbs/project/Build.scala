import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2-plugins-thumbs"
    val appVersion      = "1.0.1"

    val appDependencies = Seq(
    	"commons-io" % "commons-io" % "2.0.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
		organization := "pbaris",
		publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
    )

}