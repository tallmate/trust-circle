dependencies {
    implementation(project(":core"))
    implementation(project(":repository"))
    implementation(project(":api"))

    implementation(libs.spring.boot.starter)
    implementation(libs.spring.tx)
}