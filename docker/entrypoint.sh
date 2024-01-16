#!/bin/sh

set -e

standalone_xml=$JBOSS_HOME/standalone/configuration/standalone.xml

if [ -z "$DATABASE_URL" ]; then
    echo 'Faltando $DATABASE_URL'
    exit 1
fi

if [ -z "$DATABASE_USERNAME" ]; then
    echo 'Faltando $DATABASE_USERNAME'
    exit 1
fi

if [ -z "$DATABASE_PASSWORD" ]; then
    echo 'Faltando $DATABASE_PASSWORD'
    exit 1
fi

sed -i -E \
    -e "s|<connection-url>.*?</connection-url>|<connection-url>$DATABASE_URL</connection-url>|" \
    -e "s|<user-name>.*?</user-name>|<user-name>$DATABASE_USERNAME</user-name>|" \
    -e "s|<password>.*?</password>|<password>$DATABASE_PASSWORD</password>|" \
    $standalone_xml

if [ -n "$DATABASE_MIN_POOL_SIZE" ]; then
    sed -i -E "s|<min-pool-size>.*?</min-pool-size>|<min-pool-size>$DATABASE_MIN_POOL_SIZE</min-pool-size>|" $standalone_xml
fi

if [ -n "$DATABASE_MAX_POOL_SIZE" ]; then
    sed -i -E "s|<max-pool-size>.*?</max-pool-size>|<max-pool-size>$DATABASE_MAX_POOL_SIZE</max-pool-size>|" $standalone_xml
fi

if [ -n "$PBDOC_CABECALHO_TITULO" ]; then
    sed -i -E "s|<property name=\"sigaex.modelos.cabecalho.titulo\" value=\".*?\"/>|<property name=\"sigaex.modelos.cabecalho.titulo\" value=\"$PBDOC_CABECALHO_TITULO\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_CABECALHO_SUBTITULO" ]; then
    sed -i -E "s|<property name=\"sigaex.modelos.cabecalho.subtitulo\" value=\".*?\"/>|<property name=\"sigaex.modelos.cabecalho.titulo\" value=\"$PBDOC_CABECALHO_SUBTITULO\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_PREFEITURA" ]; then
    sed -i -E "s|<property name=\"siga.prefeitura\" value=\".*?\"/>|<property name=\"siga.prefeitura\" value=\"$PBDOC_PREFEITURA\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_PREFEITURA_CABECALHO" ]; then
    sed -i -E "s|<property name=\"siga.prefeitura.cabecalho\" value=\".*?\"/>|<property name=\"siga.prefeitura.cabecalho\" value=\"$PBDOC_PREFEITURA_CABECALHO\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_PREFEITURA_TEMA_COR" ]; then
    sed -i -E "s|<property name=\"siga.prefeitura.tema.cor\" value=\".*?\"/>|<property name=\"siga.prefeitura.tema.cor\" value=\"$PBDOC_PREFEITURA_TEMA_COR\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_RELATORIO_TITULO" ]; then
    sed -i -E "s|<property name=\"siga.relat.titulo\" value=\".*?\"/>|<property name=\"siga.relat.titulo\" value=\"$PBDOC_RELATORIO_TITULO\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_RELATORIO_SUBTITULO" ]; then
    sed -i -E "s|<property name=\"siga.relat.subtitulo\" value=\".*?\"/>|<property name=\"siga.relat.subtitulo\" value=\"$PBDOC_RELATORIO_SUBTITULO\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_CARIMBO_TEXTO_SUPERIOR" ]; then
    sed -i -E "s|<property name=\"sigaex.carimbo.texto.superior\" value=\".*?\"/>|<property name=\"sigaex.carimbo.texto.superior\" value=\"$PBDOC_CARIMBO_TEXTO_SUPERIOR\"/>|" $standalone_xml
fi

if [ -n "$PBDOC_FLYWAY_MIGRATE" ]; then
    sed -i -E "s|<property name=\"siga.flyway.migrate\" value=\".*?\"/>|<property name=\"siga.flyway.migrate\" value=\"$PBDOC_FLYWAY_MIGRATE\"/>|" $standalone_xml
fi

exec $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0
