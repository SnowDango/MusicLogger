buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
    }
}

allprojects {
    repositories{
        google()
        mavenCentral()
    }
}

task("clean" ,Delete::class){
    delete = setOf(rootProject.buildDir)
}