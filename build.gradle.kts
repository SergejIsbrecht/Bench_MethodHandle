plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.graalvm.buildtools.native") version "0.9.21"
}

group = "io.sergejisbrecht"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("io.sergejisbrecht.HelloWorldOpenJdk")
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        // vendor.set(JvmVendorSpec.matching("GraalVM Community"))
    }
}

dependencies {
    implementation("org.openjdk.jmh:jmh-core:1.36")
    annotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.36")
}

tasks.withType<Zip> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.withType<Tar> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

graalvmNative {
    binaries {
        agent {
            defaultMode.set("standard") // Default agent mode if one isn't specified using `-Pagent=mode_name`
            enabled.set(false) // Enables the agent
        }

        this["main"].run {
            this.useFatJar.set(true)

            println("BUILD main")

            imageName.set("bench_amd64")
            verbose.set(true)

            javaLauncher.set(javaToolchains.launcherFor {
                languageVersion.set(JavaLanguageVersion.of(17))
                // vendor.set(JvmVendorSpec.matching("GraalVM Community"))
            })

            mainClass.set("io.sergejisbrecht.HelloWorldNativeImage")

            buildArgs.add("--verbose")
            buildArgs.add("--no-fallback")
            buildArgs.add("-H:IncludeResources=.*/BenchmarkList")
            buildArgs.add("-H:Log=registerResource:verbose")
            buildArgs.add("--initialize-at-build-time=org.openjdk.jmh.infra,org.openjdk.jmh.util.Utils,org.openjdk.jmh.runner.InfraControl,org.openjdk.jmh.runner.InfraControlL0,org.openjdk.jmh.runner.InfraControlL1,org.openjdk.jmh.runner.InfraControlL2,org.openjdk.jmh.runner.InfraControlL3,org.openjdk.jmh.runner.InfraControlL4")
            buildArgs.add("-H:-SpawnIsolates")
            buildArgs.add("-H:+UseSerialGC")
            buildArgs.add("-H:InitialCollectionPolicy=BySpaceAndTime")
            // buildArgs.add("-H:+SourceLevelDebug")
            // buildArgs.add("-H:AlignedHeapChunkSize=2097152")
            // buildArgs.add("--native-compiler-path=/usr/bin/gcc-10")
            buildArgs.add("-H:AlignedHeapChunkSize=524288")
            buildArgs.add("-H:+ReportExceptionStackTraces")
            buildArgs.add("--enable-monitoring=all")
            buildArgs.add("-J-Xmx20g")
            buildArgs.add("-march=native")

            // buildArgs.add("-g")
            // buildArgs.add("-H:-DeleteLocalSymbols")
        }
    }
}
