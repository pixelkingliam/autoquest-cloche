plugins {
    kotlin("jvm") version "2.1.0"
    id("earth.terrarium.cloche") version "0.8.25"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "pixel"
version = "1.0-SNAPSHOT"
var architecturyApiVersion = "13.0.8"



repositories {
//    maven("https://repo.maven.apache.org/maven2/")
//    maven("https://maven.msrandom.net/repository/cloche")
//    maven("https://maven.msrandom.net/repository/root")
//    maven(url = "https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.architectury.dev/")
    mavenCentral()
    maven("https://thedarkcolour.github.io/KotlinForForge/") { name = "Kotlin for Forge" }
    cloche {
        main()
        librariesMinecraft()
        mavenFabric()
    }

}


kotlin {
    jvmToolchain(21)
}
cloche {
    metadata {
        // Automatically generate mod metadata file
        modId = "autoquest"
        name = "Autoquest"
        description = "Autoquest Modern"
        license = "MIT"

        author("Pixel")
        dependency {
            modId = "minecraft"
            version {
                start = "1.21.1"
            }
        }
    }
    common {
        dependencies {
            implementation("com.google.code.gson:gson:2.8.9")
            implementation("com.squareup.okhttp:okhttp:2.5.0")
            implementation("com.squareup.okio:okio:3.9.1")
            implementation("com.squareup.okio:okio-jvm:3.9.1")
        }


    }
    minecraftVersion = "1.21.1"

    fabric {
        loaderVersion = "0.16.10"
        data()
         client {
            data()
        }

        runs {
            server()
            client()

            data()
            clientData()
        }
        dependencies {
            fabricApi("0.115.1+1.21.1")
            implementation("dev.architectury:architectury:$architecturyApiVersion")

//            modApi("net.fabricmc:fabric-language-kotlin:1.13.1+kotlin.2.1.10") {
//                isTransitive = false
//            }
        //modImplementation("net.fabricmc:fabric-language-kotlin:1.13.1+kotlin.2.1.10")
        }
        metadata {
            entrypoint("main", "pixel.autoquestmodern.fabric.AutoquestFabric")
            //entrypoint("client", "earth.terrarium.chipped.client.fabric.ChippedClientFabric")
        }
    }

//    neoforge {
//        loaderVersion = "21.4.110-beta"
//
//        data()
//
//        runs {
//            client()
//            server()
//
//            data()
//            clientData()
//        }
//
//    }
}
