import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.vfoeh"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation(kotlin("script-runtime"))
    implementation("org.json:json:20201115")
    implementation("log4j:log4j:1.2.17")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets.main {
    java.srcDirs("src/main/myKotlin")
}

tasks {
     jar {
        description = "Create a self-contained jar file for GameTracker."
        manifest {
            attributes["Main-Class"] = "MainKt"
        }

        from(sourceSets.main.get().output)

        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }

    create("runGameTracker") {
        doLast {
            exec {
                commandLine("java -jar build\\libs\\GameTracker-1.0-SNAPSHOT.jar".split(" "))
            }
        }
    }
}