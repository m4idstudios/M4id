pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") { name = "Fabric" }
        maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
        maven("https://maven.kikugie.dev/snapshots") { name = "KikuGie Snapshots" }
        maven("https://maven.kikugie.dev/releases") { name = "KikuGie Releases" }
        maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    }
    includeBuild("build-logic")

    plugins {
        id("com.google.devtools.ksp") version "2.3.12"
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
    id("dev.kikugie.stonecutter") version "0.9.8"
    id("dev.kikugie.loom-back-compat") version "0.4.1"
}

stonecutter {
    create(rootProject) {
        fun match(version: String, vararg loaders: String) =
            loaders.forEach { version("$version-$it", version).buildscript = "$it/build.$it.gradle.kts" }

        match("1.21.1", "fabric", "neoforge")
        //match("26.1.2", "fabric", "neoforge") // Why does mojang remake the entire ass backend each update.


        vcsVersion = "1.21.1-fabric"
    }
}



