import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm") version "1.4.21"
}

group = "me.vfoeh"
version = "1.0-SNAPSHOT"

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

/*tasks.create("runStuff") {
    exec {
        commandLine("C:\\Users\\vfoeh\\.jdks\\openjdk-15.0.1\\bin\\java.exe \"-javaagent:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2020.3\\lib\\idea_rt.jar=55636:C:\\Program Files\\JetBrains\\IntelliJ IDEA 2020.3\\bin\" -Dfile.encoding=UTF-8 -classpath C:\\Users\\vfoeh\\IdeaProjects\\Playground\\out\\production\\Playground;C:\\Users\\vfoeh\\AppData\\Roaming\\JetBrains\\IntelliJIdea2020.3\\plugins\\Kotlin\\kotlinc\\lib\\kotlin-stdlib.jar;C:\\Users\\vfoeh\\AppData\\Roaming\\JetBrains\\IntelliJIdea2020.3\\plugins\\Kotlin\\kotlinc\\lib\\kotlin-reflect.jar;C:\\Users\\vfoeh\\AppData\\Roaming\\JetBrains\\IntelliJIdea2020.3\\plugins\\Kotlin\\kotlinc\\lib\\kotlin-test.jar MainKt")
    }
}*/

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "MainKt"
        }
    }
}