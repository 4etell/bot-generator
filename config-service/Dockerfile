FROM azul/zulu-openjdk:17.0.4
RUN set -x \
    mkdir -p /app
COPY ./build/libs/config-service-0.1-all.jar /app/
WORKDIR /app
ARG profile
ENV env_profile $profile
CMD java -Dmicronaut.environments=${env_profile} -jar config-service-0.1-all.jar