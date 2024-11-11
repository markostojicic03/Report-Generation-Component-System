plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "softverskekomponente_prvi_projekat"
include("specification")
include("pdfImplementation")
include("excelImplementation")
include("csvImplementation")
include("textImplementation")
include("testApp")
include("calculation")
