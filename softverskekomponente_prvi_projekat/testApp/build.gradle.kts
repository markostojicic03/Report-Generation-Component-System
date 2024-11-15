plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation(project(":specification"))
    runtimeOnly(project(":textImplementation"))
    runtimeOnly(project(":csvImplementation"))
    runtimeOnly(project(":excelImplementation"))
    runtimeOnly(project(":pdfImplementation"))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    testImplementation(kotlin("test"))

}

application{
    mainClass.set("testApp.MainKt")
}


tasks.shadowJar {
    archiveClassifier.set("all")
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    mergeServiceFiles()
}

tasks.build{
    dependsOn(tasks.shadowJar)
}



tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}