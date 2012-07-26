import sbt._
import Keys._

object ThumbsBuild extends Build {
	lazy val buildVersion = "0.0.1"
	lazy val playVersion = "2.0.2"
	
	lazy val root = Project(id = "play2-plugins-thumbs", base = file("."), settings = Project.defaultSettings).settings(
		version := buildVersion,
		
		publishTo := Some(Resolver.file("file",  new File(Path.userHome.absolutePath+"/.m2/repository"))),
		
		organization := "pbaris",
		
		resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
		
		javacOptions ++= Seq("-source","1.6","-target","1.6", "-encoding", "UTF-8"),
		javacOptions += "-Xlint:unchecked",
		
		libraryDependencies += "play" %% "play" % playVersion
	)
}
