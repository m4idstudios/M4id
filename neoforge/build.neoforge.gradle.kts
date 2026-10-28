import org.gradle.api.file.DuplicatesStrategy

plugins {
    id("mod-platform")
    id("net.neoforged.moddev")
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

group = "com.github.DoctorM4id.M4id"
version = prop("mod.version")

base {
    archivesName = prop("mod.id")
}

val jvmTargetVersion = when {
    sc.current.parsed >= "26.1" -> 25
    sc.current.parsed >= "1.20.5" -> 21
    else -> 17
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(jvmTargetVersion))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget(jvmTargetVersion.toString()))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(jvmTargetVersion)
}

group = prop("mod.group")
version = prop("mod.version")

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifactId = sc.current.project
        }
    }
}

sourceSets.named("main") {
    java.srcDir(rootProject.file("neoforge/src/main/java"))
    resources.srcDir(rootProject.file("neoforge/src/main/resources"))
}

stonecutter {
    val (version, loader) = current.project.split('-', limit = 2)
    properties.tags(version, loader)

    replacements.string(current.parsed >= "1.21.11") {
        replace("ResourceLocation", "Identifier")
        replace("location()", "identifier()")
    }
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

platform {
    loader = "neoforge"
    dependencies {
        required("minecraft") {
            forgeLikeVersionRange = prop("deps.minecraft")
        }
        required("neoforge") {
            forgeLikeVersionRange.set("[1,)")
        }
    }
}

neoForge {
    version = prop("deps.neoforge")
    accessTransformers.from(rootProject.file("neoforge/src/main/resources/${stonecutter.current.version}.cfg"))
    validateAccessTransformers = true

    if (hasProperty("deps.parchment")) parchment {
        val (mc, ver) = prop("deps.parchment").split(':')
        mappingsVersion = ver
        minecraftVersion = mc
    }

    runs {
        register("client") {
            client()
            gameDirectory = file("run/")
            ideName = "NeoForge Client (${stonecutter.current.version})"
            programArgument("--username=Dev")
        }
        register("server") {
            server()
            gameDirectory = file("run/")
            ideName = "NeoForge Server (${stonecutter.current.version})"
        }
    }

    mods {
        register(prop("mod.id")) {
            sourceSet(sourceSets["main"])
        }
    }
    sourceSets["main"].resources.srcDir("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
}

repositories {
    mavenCentral()
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth") {
        name = "Modrinth"
    }

    maven {
        name = "Kotlin for Forge"
        setUrl("https://thedarkcolour.github.io/KotlinForForge/")
    }

    exclusiveContent {
        forRepository {
            maven { url = uri("https://maven.ryanhcode.dev/releases") }
        }
        filter {
            includeGroup("dev.ryanhcode.sable-companion")
        }
    }
}

dependencies {
    // implementation(libs.moulberry.mixinconstraints)
    // jarJar(libs.moulberry.mixinconstraints)
    implementation("thedarkcolour:kotlinforforge-neoforge:${prop("deps.neoforge-language-kotlin")}")

    implementation("dev.ryanhcode.sable-companion:sable-companion-common-1.21.1:1.6.0")
    jarJar("dev.ryanhcode.sable-companion:sable-companion-common-1.21.1:[1.6.0,)") {
        version {
            prefer("1.6.0")
        }
    }

    implementation(libs.serialization.core)
    implementation(libs.serialization.json)
}

tasks.named("createMinecraftArtifacts") {
    dependsOn(tasks.named("stonecutterGenerate"))
}
