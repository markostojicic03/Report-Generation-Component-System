plugins {
    kotlin("jvm")
    `java-library`
    `maven-publish`
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

}

dependencies {
    implementation(project(":specification"))
    implementation ("org.apache.logging.log4j:log4j-core:2.18.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"]) // If you're using the 'java' or 'kotlin' plugin

            groupId = "org.example"
            artifactId = "csvImplementation"
            version = "1.0.0"
        }
    }
}


kotlin {
    jvmToolchain(21)
}