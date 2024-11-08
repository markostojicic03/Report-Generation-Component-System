plugins {
    kotlin("jvm")
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
    testImplementation(kotlin("test"))

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}