#FROM registry.cn-shenzhen.aliyuncs.com/heytea-ops/heytea-openjdk8-20210924:2.0.0
FROM gxstax/centos-jdk1.8:v1

MAINTAINER ant (gxstaxant@gmail.com)

# install dtc
COPY target/data-transfer-center-*.tar.gz /data/dtc/

RUN \
    mkdir -p /data/dtc/ && \
    mkdir -p /data/dtc/data-transfer-center && \
    tar -xzvf /data/dtc/data-transfer-center-*.tar.gz -C /data/dtc/data-transfer-center && \
    /bin/rm -f /data/dtc/data-transfer-center-*.tar.gz && \

    mkdir -p /data/dtc/data-transfer-center/logs  && \
    chmod +x /data/dtc/data-transfer-center/bin/*.sh  && \
    yum clean all && \
    true

WORKDIR /data/dtc

CMD [ "/data/dtc/data-transfer-center/bin/startup.sh" ]
