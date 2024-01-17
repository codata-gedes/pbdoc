#!/bin/sh

set -e

standalone_xml=$JBOSS_HOME/standalone/configuration/standalone.xml

is_true() {
    if [ -n "$1" ] && [ "$1" != "0" ] && [ $(echo "$1" | tr '[:upper:]' '[:lower:]') != "false" ]
    then
        return 0 # true
    fi

    return 1 # false
}

configure() {
    if [ -n "$DATABASE_URL" ]; then
        if [ -z "$DATABASE_USERNAME" ]; then
            echo 'Faltando $DATABASE_USERNAME'
            exit 1
        fi
        
        if [ -z "$DATABASE_PASSWORD" ]; then
            echo 'Faltando $DATABASE_PASSWORD'
            exit 1
        fi

        sed --in-place --regexp-extended \
            --expression "s|<connection-url>.*?</connection-url>|<connection-url>$DATABASE_URL</connection-url>|" \
            --expression "s|<user-name>.*?</user-name>|<user-name>$DATABASE_USERNAME</user-name>|" \
            --expression "s|<password>.*?</password>|<password>$DATABASE_PASSWORD</password>|" \
            $standalone_xml
    fi

    if [ -n "$DATABASE_MIN_POOL_SIZE" ]; then
        sed --in-place --regexp-extended "s|<min-pool-size>.*?</min-pool-size>|<min-pool-size>$DATABASE_MIN_POOL_SIZE</min-pool-size>|" $standalone_xml
    fi

    if [ -n "$DATABASE_MAX_POOL_SIZE" ]; then
        sed --in-place --regexp-extended "s|<max-pool-size>.*?</max-pool-size>|<max-pool-size>$DATABASE_MAX_POOL_SIZE</max-pool-size>|" $standalone_xml
    fi

    if [ -n "$PBDOC_CABECALHO_TITULO" ]; then
        sed --in-place --regexp-extended "s|<property name=\"sigaex.modelos.cabecalho.titulo\" value=\".*?\"/>|<property name=\"sigaex.modelos.cabecalho.titulo\" value=\"$PBDOC_CABECALHO_TITULO\"/>|" $standalone_xml
    fi

    if [ -n "$PBDOC_CABECALHO_SUBTITULO" ]; then
        sed --in-place --regexp-extended "s|<property name=\"sigaex.modelos.cabecalho.subtitulo\" value=\".*?\"/>|<property name=\"sigaex.modelos.cabecalho.titulo\" value=\"$PBDOC_CABECALHO_SUBTITULO\"/>|" $standalone_xml
    fi

    if is_true "$PBDOC_PREFEITURA"; then
        sed --in-place --regexp-extended 's|<property name="siga.prefeitura" value=".*?"/>|<property name="siga.prefeitura" value="true"/>|' $standalone_xml
    fi

    if [ -n "$PBDOC_PREFEITURA_CABECALHO" ]; then
        sed --in-place --regexp-extended "s|<property name=\"siga.prefeitura.cabecalho\" value=\".*?\"/>|<property name=\"siga.prefeitura.cabecalho\" value=\"$PBDOC_PREFEITURA_CABECALHO\"/>|" $standalone_xml
    fi

    if [ -n "$PBDOC_PREFEITURA_TEMA_COR" ]; then
        sed --in-place --regexp-extended "s|<property name=\"siga.prefeitura.tema.cor\" value=\".*?\"/>|<property name=\"siga.prefeitura.tema.cor\" value=\"$PBDOC_PREFEITURA_TEMA_COR\"/>|" $standalone_xml
    fi

    if [ -n "$PBDOC_RELATORIO_TITULO" ]; then
        sed --in-place --regexp-extended "s|<property name=\"siga.relat.titulo\" value=\".*?\"/>|<property name=\"siga.relat.titulo\" value=\"$PBDOC_RELATORIO_TITULO\"/>|" $standalone_xml
    fi

    if [ -n "$PBDOC_RELATORIO_SUBTITULO" ]; then
        sed --in-place --regexp-extended "s|<property name=\"siga.relat.subtitulo\" value=\".*?\"/>|<property name=\"siga.relat.subtitulo\" value=\"$PBDOC_RELATORIO_SUBTITULO\"/>|" $standalone_xml
    fi

    if [ -n "$PBDOC_CARIMBO_TEXTO_SUPERIOR" ]; then
        sed --in-place --regexp-extended "s|<property name=\"sigaex.carimbo.texto.superior\" value=\".*?\"/>|<property name=\"sigaex.carimbo.texto.superior\" value=\"$PBDOC_CARIMBO_TEXTO_SUPERIOR\"/>|" $standalone_xml
    fi

    if is_true "$PBDOC_FLYWAY_MIGRATE"; then
        sed --in-place --regexp-extended 's|<property name="siga.flyway.migrate" value=".*?"/>|<property name="siga.flyway.migrate" value="true"/>|' $standalone_xml
    fi
}

configure

exec $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0
