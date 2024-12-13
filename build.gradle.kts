plugins {
    java
    kotlin("jvm") version "1.8.20"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation ("org.apache.logging.log4j:log4j-api:2.24.2")
    implementation ("org.apache.logging.log4j:log4j-core:2.24.2")

    testImplementation ("org.apache.logging.log4j:log4j-api:2.24.2")
    testImplementation ("org.apache.logging.log4j:log4j-core:2.24.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

application {
    mainClass.set("org.example.Main")
}
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    // Можно добавить дополнительные параметры форматирования кода или lint
}

tasks.withType<Javadoc> {
    // Генерация Javadoc
    (options as StandardJavadocDocletOptions).addStringOption("encoding", "UTF-8")
    (options as StandardJavadocDocletOptions).addStringOption("charSet", "UTF-8")
    // Сгенерированный HTML будет в build/docs/javadoc
}
tasks.jar {
    // Настройки для JAR-файла, например, указание манифеста
    manifest {
        attributes(
            "Main-Class" to "com.example.MainKt"  // Замените на вашу главную точку входа
        )
    }
}
tasks.test {
    useJUnitPlatform()
}