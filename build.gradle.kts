import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	kotlin("plugin.jpa") version "1.6.10"
	kotlin("kapt") version "1.6.10" // 자바의 어노테이션 처리할 때 kotlin 파일 어노테이션 처리 포함
}

group = "com.personal-study"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
val querydslVersion = "5.0.0"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}
// for querydsl
sourceSets["main"].withConvention(org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet::class) {
	kotlin.srcDir("$buildDir/generated/source/kapt/main")
}

repositories {
	mavenCentral()
}

dependencies {


	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// db
	runtimeOnly("mysql:mysql-connector-java")
	implementation("com.h2database:h2")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// querydsl (추가 설정)
	implementation("com.querydsl:querydsl-jpa:$querydslVersion")
	kapt("com.querydsl:querydsl-apt:$querydslVersion:jpa")
	kapt("org.springframework.boot:spring-boot-configuration-processor")

	implementation("com.github.gavlyukovskiy:p6spy-spring-boot-starter:1.5.6")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// Qclass 생성: ./gradlew clean compileKotlin