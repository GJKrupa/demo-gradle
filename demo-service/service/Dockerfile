FROM java:8u40
ARG BUILD_VERSION
MAINTAINER Gerard Krupa "gerard.krupa@gmail.com"
EXPOSE 8080
ADD service-$BUILD_VERSION.tar /
RUN ln -s /service-$BUILD_VERSION /service
ENTRYPOINT ["/service/bin/service", "-Djava.security.egd=file:/dev/./urandom"]