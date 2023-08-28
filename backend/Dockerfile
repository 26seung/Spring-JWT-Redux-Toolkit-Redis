# APP (Build & Run)
FROM openjdk:11.0-slim

WORKDIR /app
#   현재 경로의 파일들을 "WORKDIR" 위치로 복사
COPY . .
#   권한설정 & jar build
RUN chmod +x ./gradlew
RUN ./gradlew clean build

#  (jar/war) 파일 위치 설정   (ARG는 호스트 파일위치를 설정할때, ENV는 컨테이너 내부)
ENV JAR_FILE=/app/build/libs/*-SNAPSHOT.jar
#   jar파일 이동
RUN mv ${JAR_FILE} /app/app.jar
ENV TZ=Asia/Seoul

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]


 # docker-compose 사용시 X
 #  수행 명령어 정리
 # 1. jar 파일 생성 : ./gradlew clean build
 # 2. docker 이미지 빌드 : docker build -t myapp .