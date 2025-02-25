FROM amd64/amazoncorretto:17
WORKDIR /app
COPY ./build/libs/BBANGZIP-0.0.1-SNAPSHOT.jar /app/BBANGZIP.jar
CMD ["java", "-Duser.timezone=Asia/Seoul", "-jar", "-Dspring.profiles.active=dev", "BBANGZIP.jar"]
