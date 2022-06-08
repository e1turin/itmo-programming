import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"
//    kotlin("plugin.serialization") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.20"
    application
}

group = "com.github.e1turin"
version = "1.0-SNAPSHOT-1"


repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    testImplementation(kotlin("test"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.3.2")

    implementation("org.jetbrains.exposed", "exposed-core", "0.38.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.38.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.38.1")
    implementation("org.jetbrains.exposed:exposed-jodatime:0.38.2")
    implementation("org.postgresql","postgresql", "42.3.4")
    implementation("org.slf4j:slf4j-simple:1.7.36")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
}

//tasks.test {
//    useJUnitPlatform()
//}
kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(8))
    }
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
//    sourceCompatibility = JavaVersion.VERSION_1_8
//    targetCompatibility = JavaVersion.VERSION_1_8
}
//tasks.withType<JavaCompile> {
//    options.compilerArgs.addAll(arrayOf("--release", "8"))
//}

application {
    mainClass.set("MainServerServiceKt")
}

tasks.jar {
    archiveBaseName.set("Server-service-async")
    archiveVersion.set("4.0")
    manifest {
        attributes["Main-Class"] = ".MainServerServiceKt"
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

