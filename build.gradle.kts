plugins {
    id("java")
    id("java-library")
    id("xyz.jpenilla.run-paper") version "2.0.0"
}

group = "me.lojosho"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io")
    maven("https://repo.md-5.net/content/groups/public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.1.0-SNAPSHOT")
    compileOnlyApi("LibsDisguises:LibsDisguises:10.0.21") {
        exclude("org.spigotmc", "spigot")
    }
    compileOnly("me.clip:placeholderapi:2.11.3")
}

tasks {
    runServer {
        minecraftVersion("1.19.4")
    }
}