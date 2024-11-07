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
    implementation("org.apache.poi:poi:5.2.3") // For .xls format
    implementation("org.apache.poi:poi-ooxml:5.2.3")
    implementation ("org.apache.logging.log4j:log4j-core:2.18.0")// For .xlsx format
    implementation(project(":specification"))
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
            artifactId = "excelImplementation"
            version = "1.0.0"
        }
    }
}



kotlin {
    jvmToolchain(21)
}