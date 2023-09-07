plugins {
    id("java-library")
    id("maven-publish")
}

group = "ru.brominemc.nbnt"
java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    compileOnly("com.google.errorprone:error_prone_annotations:2.21.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(17)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "BromineMcReleases"
            url = uri("https://api.brominemc.ru/maven/releases")
            credentials {
                username = System.getenv("MAVEN_NAME")
                password = System.getenv("MAVEN_PASS")
            }
        }
    }
}