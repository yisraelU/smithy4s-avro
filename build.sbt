// https://typelevel.org/sbt-typelevel/faq.html#what-is-a-base-version-anyway
ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "io.github.yisraelu"
ThisBuild / organizationName := "smithy4s-avro"
ThisBuild / startYear := Some(2023)
ThisBuild / licenses := Seq(License.Apache2)
ThisBuild / developers := List(
  // your GitHub handle and name
  tlGitHubDev("yisraelu", "Yisrael Union")
)

Global / onChangedBuildSource := ReloadOnSourceChanges
// publish to s01.oss.sonatype.org (set to true to publish to oss.sonatype.org instead)
ThisBuild / tlSonatypeUseLegacyHost := false

// publish website from this branch
ThisBuild / tlSitePublishBranch := Some("main")

val Scala213 = "2.13.12"
ThisBuild / crossScalaVersions := Seq(Scala213, "3.3.1")
ThisBuild / scalaVersion := Scala213 // the default Scala

lazy val root = tlCrossRootProject.aggregate(core,traits)

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/core"))
  .settings(
    name := "smithy4s-avro-core",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % "2.10.0",
      "org.typelevel" %%% "cats-effect" % "3.5.3",
       "com.github.fd4s" %% "vulcan" % "1.10.1",
      "com.disneystreaming.smithy4s" %%% "smithy4s-core" % "0.18.8",
      "org.scalameta" %%% "munit" % "0.7.29" % Test,
      "org.typelevel" %%% "munit-cats-effect-3" % "1.0.7" % Test
    )
  ).dependsOn(traits)

lazy val traits = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("modules/traits"))
  .settings(
    name := "smithy4s-avro-traits",
    libraryDependencies ++= Seq(
   "software.amazon.smithy" % "smithy-build" % "1.41.1"

),
    Compile / packageSrc / mappings := (Compile / packageSrc / mappings).value
      .filterNot { case (file, path) =>
        path.equalsIgnoreCase("META-INF/smithy/manifest")
      },
    resolvers += Resolver.mavenLocal,
    javacOptions ++= Seq("--release", "11")
  )

lazy val docs = project.in(file("site")).enablePlugins(TypelevelSitePlugin)
