plugins {
    id("mod-platform")
    id("dev.kikugie.loom-back-compat")
    `maven-publish`
}

base {
    archivesName = prop("mod.id")
}

val jvmTargetVersion = if (sc.current.parsed >= "1.20.5") 21 else 17

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

stonecutter {
    val (version, loader) = current.project.split('-', limit = 2)
    properties.tags(version, loader)

    replacements.string(current.parsed >= "1.21.11") {
        replace("ResourceLocation", "Identifier")
        replace("location()", "identifier()")
    }
    replacements.string(current.parsed >= "26.1.2") {
        replace("FabricDataOutput", "FabricPackOutput")
    }
}

platform {
    loader = "fabric"
    dependencies {
        required("minecraft") {
            fabricLikeVersionRange = prop("deps.minecraft")
        }
        required("fabric-api") {
            slug("fabric-api")
            fabricLikeVersionRange = ">=${prop("deps.fabric-api")}"
        }
        required("fabricloader") {
            fabricLikeVersionRange = ">=${prop("deps.fabric-loader")}"
        }
        optional("modmenu") {}
    }
}

loom {
    accessWidenerPath = rootProject.file("src/main/resources/aw/${sc.current.version}.accesswidener")
    runs.named("client") {
        client()
        ideConfigGenerated(true)
        runDir = "run/"
        environment = "client"
        programArgs("--username=Dev")
        configName = "Fabric Client"
    }
    runs.named("server") {
        server()
        ideConfigGenerated(true)
        runDir = "run/"
        environment = "server"
        configName = "Fabric Server"
    }
}

fabricApi {
    configureDataGeneration {
        outputDirectory = file("${rootDir}/versions/datagen/${sc.current.version.split("-")[0]}/src/main/generated")
        client = true
    }
}

repositories {
    mavenCentral()

    strictMaven("https://maven.terraformersmc.com/", "com.terraformersmc") { name = "TerraformersMC" }
    strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

configurations.all {
    resolutionStrategy {
        force("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("deps.minecraft")}")

    if (sc.current.parsed < "26") {
        mappings(loom.layered {
            officialMojangMappings()
            if (hasProperty("deps.parchment"))
                parchment("org.parchmentmc.data:parchment-${prop("deps.parchment")}@zip")
        })
    }

    modImplementation("net.fabricmc:fabric-loader:${prop("deps.fabric-loader")}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${prop("deps.fabric-api")}")

    implementation(libs.serialization.core)
    include(libs.serialization.core)

    implementation(libs.serialization.json)
    include(libs.serialization.json)

    implementation(kotlin("stdlib"))
    include(kotlin("stdlib"))
}
