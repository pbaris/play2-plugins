import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "play2-plugins-utils"
    val appVersion      = "0.0.1"

    val appDependencies = Seq()

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
		organization := "pbaris",
		publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/m2-repo")))
    )

}