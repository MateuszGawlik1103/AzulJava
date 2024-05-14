plugins {
    id("java")
    id("jacoco")
    application
    id("org.barfuin.gradle.jacocolog").version("3.1.0")
    id("org.openjfx.javafxplugin").version("0.0.14")
}

group = "pl.edu.pw"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "20.0.1"
    modules("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.3")
    testImplementation("org.mockito:mockito-core:3.+")
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
}

application {
    mainClass.set("pl.edu.pw.gui.Main")
    mainModule.set("pl.edu.pw.gui")
}

tasks.withType<JavaExec>() {
    standardInput = System.`in`
}
val jacocoExclude = listOf("pl/edu/pw/Main.class", "pl/edu/pw/Exceptions.class")
tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    extensions.configure(JacocoTaskExtension::class) {
        excludes = jacocoExclude
    }
}


tasks.jacocoTestReport {
    mustRunAfter(tasks.getByName<Test>("test"))
    reports {
        xml.required.set(true)
        xml.outputLocation.set(layout.buildDirectory.file("jacoco/jacoco.xml"))
        csv.required.set(false)
    }
    classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(jacocoExclude)
                }
            })
    )
}

tasks.jacocoTestCoverageVerification {
    classDirectories.setFrom(
            files(classDirectories.files.map {
                fileTree(it) {
                    exclude(jacocoExclude)
                }
            })
    )
}

