plugins {
    java
    application
    id("org.beryx.jlink") version "3.1.3"
}

application {
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
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.jline:jline:3.30.0")
}

tasks.test {
    useJUnitPlatform()
}

jlink {
    options.addAll("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
}

tasks.register<Jar>("fatJar") {
    manifest {
        attributes["Main-Class"] = "org.pintoschneider.void_of_the_unfathomable.Main"
    }

    from(sourceSets.main.get().output)
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
