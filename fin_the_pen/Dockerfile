FROM adoptopenjdk/openjdk11

WORKDIR /app

COPY build/libs/fin_the_pen-0.0.1-SNAPSHOT.jar app.jar

# 기본 프로파일을 dev로 설정
ARG SPRING_PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILE}

VOLUME /tmp

ENTRYPOINT ["java","-jar","app.jar"]