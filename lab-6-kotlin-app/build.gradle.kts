group = "com.github.e1turin"
version ="1.0-SNAPSHOT"

subprojects{
//    apply(plugin("java-library"))

    // Address https://github.com/gradle/gradle/issues/4823: Force parent project evaluation before sub-project evaluation for Kotlin build scripts
    if (gradle.startParameter.isConfigureOnDemand
        && buildscript.sourceFile?.extension?.toLowerCase() == "kts"
        && parent != rootProject) {
        generateSequence(parent) { project -> project.parent.takeIf { it != rootProject } }
            .forEach { evaluationDependsOn(it.path) }
    }
//    repositories {
//        mavenCentral()
//    }
}
