plugins {
    kotlin("jvm") version "1.6.10"
    id("application")
}

group = "com.github.e1turin"
version = "RELEASE-CANDIDATE"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.github.e1turin.lab5.MainKt"
    }

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }

    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

application {
    mainClass.set("com.github.e1turin.lab5.MainKt")
}
