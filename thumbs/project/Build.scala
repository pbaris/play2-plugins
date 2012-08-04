import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "thumbs"
    val appVersion      = "1.0.1"

    val appDependencies = Seq(
      // Add your project dependencies here,
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
		organization := "pbaris",
		thumbnail tag
		publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository")))
    )

}