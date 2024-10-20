plugins {
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.5"
	kotlin("plugin.jpa") version "1.9.24"
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	kotlin("kapt") version "1.9.22"
}

noArg {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}


group = "com"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}



dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	//my-sql
	runtimeOnly("com.mysql:mysql-connector-j")

	//jwt
	implementation("io.jsonwebtoken:jjwt-api:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

	//swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	//security
	implementation("org.springframework.boot:spring-boot-starter-security")
	testImplementation("org.springframework.security:spring-security-test")

	//dotenv
	implementation("io.github.cdimascio:dotenv-kotlin:6.2.2")

	// mail
	implementation("org.springframework.boot:spring-boot-starter-mail")

	//validation
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// aws-s3
	implementation ("org.springframework.cloud:spring-cloud-starter-aws:2.2.6.RELEASE")
	implementation ("javax.xml.bind:jaxb-api:2.3.0")

	//queryDSL
	implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
	kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

	implementation("org.springdoc:springdoc-openapi-ui:1.6.14")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<JavaCompile> {
	sourceSets {
		main {
			java.srcDirs("src/main/kotlin")
			resources {
				srcDirs("src/main/resources")
				exclude("**/.eslintrc.js", "**/.gitignore", "**/.prettierrc", "**/Dockerfile.nest", "**/nest-cli.json", "**/package-lock.json", "**/package.json", "**/tsconfig.build.json", "**/tsconfig.json")
			}
		}
		test {
			java.srcDirs("src/test/kotlin")
		}
	}
}
tasks.withType<ProcessResources> {
	duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Jar>("jar") {
	enabled = false
}

//tasks.withType<Test> {
//	useJUnitPlatform()
//}
