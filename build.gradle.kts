plugins {
    id("com.gradleup.shadow") version "9.0.2"
    java
    // id("maven-publish") // Only if you need to publish artifacts
}

group = "com.complexible.stardog.docs.corenlp"
version = "1.3.0"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val stardogVersion by extra("8.1.1")
val corenlpVersion by extra("4.0.0")
val junitVersion by extra("4.12")

repositories {
    maven {
        url = uri("https://maven.stardog.com")
    }
    mavenCentral()
    mavenLocal()
}

configurations {
    create("localDeps")
}

dependencies {
    compileOnly("com.complexible.stardog:server:$stardogVersion")
    compileOnly("com.complexible.stardog:client-http:$stardogVersion")
    implementation("edu.stanford.nlp:stanford-corenlp:$corenlpVersion")
    implementation("edu.stanford.nlp:stanford-corenlp:$corenlpVersion") {
        artifact {
            classifier = "models"
        }
    }
    implementation("edu.stanford.nlp:stanford-corenlp:$corenlpVersion") {
        artifact {
            classifier = "models-english"
        }
    }
    implementation("edu.stanford.nlp:stanford-corenlp:$corenlpVersion") {
        artifact {
            classifier = "models-english-kbp"
        }
    }

    testImplementation("junit:junit:$junitVersion")
    testImplementation("com.complexible.stardog:client-embedded:$stardogVersion")
    testImplementation("com.complexible.stardog.bites.http:stardog-bites-protocols-http-client:$stardogVersion")
}

tasks.shadowJar {
    relocate("org.apache.lucene", "shadow.org.apache.lucene")
    mergeServiceFiles()
}

tasks.test {
    systemProperty("stardog.home", System.getProperty("stardog.home"))
    systemProperty("java.library.path", System.getProperty("stardog.home") + "/lib")
    maxHeapSize = "8g"
}