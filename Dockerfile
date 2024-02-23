FROM registry.access.redhat.com/ubi8/openjdk-8:1.10-1 AS builder

ARG SIGA_VERSAO=develop

USER root

WORKDIR /pbdoc

COPY siga ./siga
COPY siga-assinador ./siga-assinador
COPY siga-autenticidade ./siga-autenticidade
COPY siga-base ./siga-base
COPY siga-cp ./siga-cp
COPY siga-cp-sinc ./siga-cp-sinc
COPY siga-documentacao ./siga-documentacao
COPY siga-dump ./siga-dump
COPY siga-ex ./siga-ex
COPY sigaex ./sigaex
COPY siga-ex-gsa ./siga-ex-gsa
COPY siga-ex-sinc ./siga-ex-sinc
COPY siga-ext ./siga-ext
COPY siga-ext-cd-bluc ./siga-ext-cd-bluc
COPY sigagc ./sigagc
COPY siga-gc-gsa ./siga-gc-gsa
COPY siga-integration-test ./siga-integration-test
COPY siga-jwt ./siga-jwt
COPY siga-ldap ./siga-ldap
COPY siga-ldap-cli ./siga-ldap-cli
COPY siga-le ./siga-le
COPY siga-oidc ./siga-oidc
COPY siga-play-proxy ./siga-play-proxy
COPY sigapp ./sigapp
COPY siga-rel ./siga-rel
COPY siga-relarmaz ./siga-relarmaz
COPY sigari ./sigari
COPY siga-sinc-lib ./siga-sinc-lib
COPY sigasr ./sigasr
COPY sigatp ./sigatp
COPY siga-vraptor-module ./siga-vraptor-module
COPY siga-vraptor-module-old ./siga-vraptor-module-old
COPY siga-wf ./siga-wf
COPY sigawf ./sigawf
COPY siga-ws ./siga-ws

COPY pom.xml settings.xml ./

RUN mvn clean package -Dmaven.test.skip -Dsiga.versao=${SIGA_VERSAO} -s settings.xml

FROM docker.io/daggerok/jboss-eap-7.2:7.2.5-alpine
LABEL org.opencontainers.image.authors="michelrisucci@codata.pb.gov.br"

USER root

# Bibliotecas adicionais necessárias no container
RUN sudo apk --update --no-cache add busybox-extras graphviz fontconfig ttf-freefont ttf-dejavu
RUN fc-cache -f
RUN ln -s /usr/lib/libfontconfig.so.1 /usr/lib/libfontconfig.so && \
    ln -s /lib/libuuid.so.1 /usr/lib/libuuid.so.1 && \
    ln -s /lib/libc.musl-x86_64.so.1 /usr/lib/libc.musl-x86_64.so.1
ENV LD_LIBRARY_PATH /usr/lib
RUN chmod -R 777 ${JBOSS_HOME}

USER ${JBOSS_USER}

# Diretório-base de armazenamento de arquivos da aplicação
ENV PBDOC_HOME /home/jboss/pbdoc
RUN mkdir -p ${PBDOC_HOME}
RUN chmod -R 777 ${PBDOC_HOME}

# Outros serviços que executam no JBoss
ENV DEPLOYMENTS_HOME ${JBOSS_HOME}/standalone/deployments
# https://api.github.com/repos/projeto-siga/siga-docker/releases/latest
RUN wget https://github.com/projeto-siga/siga-docker/releases/download/v1.1/ckeditor.war -O ${DEPLOYMENTS_HOME}/ckeditor.war 
# https://api.github.com/repos/assijus/blucservice/releases/latest
RUN wget https://github.com/assijus/blucservice/releases/download/v2.3.6/blucservice.war -O ${DEPLOYMENTS_HOME}/blucservice.war
# https://api.github.com/repos/projeto-siga/vizservice/releases/latest
RUN wget https://github.com/projeto-siga/vizservice/releases/download/v1.0.0.0/vizservice.war -O ${DEPLOYMENTS_HOME}/vizservice.war
# https://api.github.com/repos/assijus/assijus/releases/latest
RUN wget https://github.com/assijus/assijus/releases/download/v4.1.4/assijus.war -O ${DEPLOYMENTS_HOME}/assijus.war

# Driver JDBC do PostgreSQL
ENV JDBC_DRIVER_FILENAME 'postgresql-42.2.23.jar'
RUN mkdir -p ${JBOSS_HOME}/modules/org/postgresql/main
RUN wget https://jdbc.postgresql.org/download/${JDBC_DRIVER_FILENAME} -O ${JBOSS_HOME}/modules/org/postgresql/main/${JDBC_DRIVER_FILENAME}
COPY docker/postgresql-module.xml ${JBOSS_HOME}/modules/org/postgresql/main/module.xml
RUN sed -i "s/\${JDBC_DRIVER_FILENAME}/${JDBC_DRIVER_FILENAME}/" ${JBOSS_HOME}/modules/org/postgresql/main/module.xml

# Diretório imagens
COPY imagens /opt/pbdoc/imagens

# config glowroot APM
RUN curl -L https://github.com/glowroot/glowroot/releases/download/v0.14.1/glowroot-0.14.1-dist.zip -o /tmp/glowroot.zip && \
unzip /tmp/glowroot.zip -d ${JBOSS_HOME}/standalone/lib/ext && \
rm /tmp/glowroot.zip

COPY --from=builder /pbdoc/target/*.war $DEPLOYMENTS_HOME/
COPY --chown=jboss:nogroup /docker/standalone.xml ${JBOSS_HOME}/standalone/configuration/standalone.xml

RUN touch ${JBOSS_HOME}/standalone/lib/ext/glowroot/glowroot.properties && \
    chown jboss:nogroup $JBOSS_HOME/standalone/lib/ext/glowroot/glowroot.properties && \
    chmod 777 $JBOSS_HOME/standalone/lib/ext/glowroot/glowroot.properties

RUN mkdir ${JBOSS_HOME}/standalone/lib/ext/glowroot/tmp && \
    chown jboss:nogroup $JBOSS_HOME/standalone/lib/ext/glowroot/tmp && \
    chmod 777 $JBOSS_HOME/standalone/lib/ext/glowroot/tmp

COPY --chmod=755 docker/entrypoint.sh /entrypoint.sh

CMD [ "/entrypoint.sh" ]