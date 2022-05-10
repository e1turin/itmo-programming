import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
    application
}

group = "com.github.e1turin"
version = "1.0-SNAPSHOT-1"


repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":util"))
    implementation(project(":common"))
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    implementation("com.google.code.gson:gson:2.9.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}
java { sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(arrayOf("--release", "8"))
}

application {
    mainClass.set("MainServerKt")
}

tasks.jar {
    archiveBaseName.set("Server-service")
    archiveVersion.set("1.0")

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

