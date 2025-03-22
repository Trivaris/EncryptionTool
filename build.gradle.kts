plugins {
    kotlin("jvm") version "2.1.0"
    application
}

group = "com.trivaris"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.trivaris.encryptiontool.app.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.trivaris.encryptiontool.app.MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.compileClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}