dependencies {
    implementation(project(":core"))
    implementation(project(":service"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
}