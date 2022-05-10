import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
}

group = "com.github.e1turin"
version = "6.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.code.gson:gson:2.9.0")
}


tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets {
    main {
        java {
//            srcDirs(listOf("src"))//work 1
            srcDirs(listOf("src/main/kotlin", "/src/main/java"))
        }
    }
    test {
        java {
            setSrcDirs(listOf("src/test"))
        }
    }
}

// holds classes included into client.jar and util.jar, so they are to be excluded from server.jar
//var serverExcludes by extra(mutableListOf<String>("/client/**"))

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

tasks.register<Jar>("jar-client"){
    archiveBaseName.set("Client-app")
    archiveVersion.set("1.0")
    from(sourceSets.main.get().output){
//        include("/main/kotlin/client/**")
//        serverExcludes.addAll(includes)
        exclude("/main/kotlin/server/**") //exclude useless server files for client
    }
    manifest {
        attributes["Main-Class"] = "main.kotlin.client.MainClientKt"
    }
    configurations["compileClasspath"].forEach { file: File -> //zip files to .jar
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.register<Jar>("jar-server"){
    archiveBaseName.set("Server-app")
    archiveVersion.set("1.0")
    from(sourceSets.main.get().output){
//        include("/main/kotlin/server/**")
        exclude("/main/kotlin/client/**") //exclude useless client files for server
    }
    manifest {
        attributes["Main-Class"] = "main.kotlin.server.MainServerKt"
    }
    configurations["compileClasspath"].forEach { file: File -> //zip files to .jar
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}
