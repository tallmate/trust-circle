dependencies {
    implementation(project(":core"))

    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.validation)
    implementation(libs.jackson.datatype.jsr310)
}