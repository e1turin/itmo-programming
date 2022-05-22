import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    kotlin("plugin.serialization") version "1.6.10"
    application
}

group = "com.github.e1turin"
version = "1.0-SNAPSHOT-1"


repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
//    implementation(project(":library"))
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("--release", "8"))
}

application {
    mainClass.set("com.github.e1turin.MainClientKt")
}

tasks.jar {
    archiveBaseName.set("Client-app")
    archiveVersion.set("2.0")
    manifest {
        attributes["Main-Class"] = "com.github.e1turin.MainClientKt"
    }

    configurations["compileClasspath"].forEach { file: File -> //zip files to .jar
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}


/*
tasks.register<Jar>("jar-util"){
    archiveBaseName.set("Util-lib")
    archiveVersion.set("1.0")
    from(sourceSets.main.get().output){
//        include("/util/**")
//        serverExcludes.addAll(includes)
        exclude( //exclude useless client and server files for util
            "/main/kotlin/client",
            "/main/kotlin/server"
        )
    }
    configurations["compileClasspath"].forEach { file: File -> //zip files to .jar
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
 */

