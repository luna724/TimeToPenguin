import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.*

plugins {
    kotlin("jvm") version "1.9.10"

    // For serialization: remove if not needed
    kotlin("plugin.serialization") version "1.9.10"

    idea
    java
    id("gg.essential.loom") version "0.10.0.+"
    id("dev.architectury.architectury-pack200") version "0.1.3"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "luna724.iloveichika.penguin"
archivesName = "TimeToPenguin"
version = "1.0"
val MODID = "time2penguin"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
            enableLanguageFeature("BreakContinueInInlineLambdas")
        }
    }
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

loom {
    launchConfigs {
        "client" {
            property("mixin.debug", "true")
            property("asmhelper.verbose", "true")
        }
    }
    forge {
        pack200Provider.set(dev.architectury.pack200.java.Pack200Adapter())
    }
}

sourceSets.main {
    output.setResourcesDir(file("$buildDir/classes/java/main"))
}

repositories {
    mavenCentral()
    maven("https://repo.sk1er.club/repository/maven-public")
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

val shadowImpl: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

val shadowModImpl: Configuration by configurations.creating {
    configurations.modImplementation.get().extendsFrom(this)
}

val devenvMod: Configuration by configurations.creating {
    isTransitive = false
    isVisible = false
}

dependencies {
    // Dependencies
    shadowImpl("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.0")

    minecraft("com.mojang:minecraft:1.8.9")
    mappings("de.oceanlabs.mcp:mcp_stable:22-1.8.9")
    forge("net.minecraftforge:forge:1.8.9-11.15.1.2318-1.8.9")

    // essential
    shadowImpl("gg.essential:loader-launchwrapper:1.1.3")
    implementation("gg.essential:essential-1.8.9-forge:3662")

    // other
    implementation(kotlin("stdlib-jdk8"))
    shadowImpl("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3") {
        exclude(group = "org.jetbrains.kotlin")
    }
}

tasks.compileJava {
    dependsOn(tasks.processResources)
}


tasks.processResources {
        filesMatching("mcmod.info") {
            expand(
                mapOf(
                    "modname" to project.name,
                    "modid" to project.name.lowercase(Locale.getDefault()),
                    "version" to project.version,
                    "mcversion" to "1.8.9"
                )
            )
        }
        rename("(.+_at.cfg)", "META-INF/$1")
        exclude("**/*.pyx")
        exclude(".idea/**")
        exclude("**/__pycache__/**")
        exclude("**/.idea/**")
        exclude("**/*.gitignore")
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.withType(Jar::class) {
    archiveBaseName.set("$archivesName")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest.attributes.run {
        this["FMLCorePluginContainsFMLMod"] = "true"
        this["ForceLoadAsMod"] = "true"

        this["TweakClass"] = "gg.essential.loader.stage0.EssentialSetupTweaker"
        this["TweakOrder"] = "0"
    }
}

val remapJar by tasks.named<net.fabricmc.loom.task.RemapJarTask>("remapJar") {
    archiveClassifier.set("")
    from(tasks.shadowJar)
    input.set(tasks.shadowJar.get().archiveFile)
}

tasks.jar {
    archiveClassifier.set("without-deps")
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
}

tasks.shadowJar {
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
    archiveClassifier.set("all-dev")
    configurations = listOf(shadowImpl, shadowModImpl)
    doLast {
        configurations.forEach {
            println("Config: ${it.files}")
        }
        exclude("META-INF/versions/**")


    }
}

tasks.jar {
    archiveClassifier.set("nodeps")
    destinationDirectory.set(layout.buildDirectory.dir("badjars"))
}

tasks.assemble.get().dependsOn(tasks.remapJar)

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
