FROM adoptopenjdk/openjdk11

WORKDIR /app

# Gradle wrapper 및 프로젝트 파일 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# gradlew에 실행 권한 추가
RUN chmod +x gradlew

# Gradle 빌드 실행 (clean 및 build 포함)
RUN ./gradlew clean build -x test

# Spring 프로파일 설정
ARG SPRING_PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE}

# 빌드된 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "build/libs/fin_the_pen-0.0.1-SNAPSHOT.jar"]
