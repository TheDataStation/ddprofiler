# ddprofiler
FROM debian:bullseye

RUN apt-get update && apt-get upgrade -y

RUN apt-get update && apt-get install -y openjdk-11-jdk

COPY . /ddprofiler

RUN cd /ddprofiler && \
    bash build.sh && \
    rm -f /ddprofiler/build/distributions/*

WORKDIR /ddprofiler

ENTRYPOINT ["/bin/bash", "/ddprofiler/run.sh", "${*}"]
