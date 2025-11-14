plugins {
    java
    application
}

application {
    // Add JVM arguments to enable native access for all unnamed modules
    // For some reason, this causes the terminal size to be zero
    // See: https://github.com/jline/jline3/issues/1067
    // applicationDefaultJvmArgs = listOf("--enable-native-access=ALL-UNNAMED")
    mainClass.set("org.pintoschneider.void_of_the_unfathomable.Main")
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

group = "org.pintoschneider.void_of_the_unfathomable"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains:annotations:26.0.2")
    implementation("org.jline:jline:3.30.6")

    // JediTerm dependencies
    implementation("org.jetbrains.pty4j:pty4j:0.13.11")
    implementation("org.slf4j:slf4j-api:2.0.17")
    implementation("org.slf4j:slf4j-jdk14:2.0.17")
    implementation(
        fileTree("libs") {
            include("*.jar") // Include all files ending with .jar in the 'libs' directory
        }
    )
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "org.pintoschneider.void_of_the_unfathomable.Main"
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
