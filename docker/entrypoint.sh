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

sed_replace() {
    property=$1
    replacement=$2
    shift 2
    sed --in-place --regexp-extended "s|<property name=\"($property)\" value=\".*?\"\s*/>|<property name=\"\\1\" value=\"$replacement\"/>|" $standalone_xml
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

    if [ -n "$PBDOC_BASE_URL" ]; then
        sed_replace 'sigaex.modelos.cabecalho.titulo' "$PBDOC_CABECALHO_TITULO"
    fi

    if [ -n "$PBDOC_CABECALHO_TITULO" ]; then
        sed_replace 'sigaex.modelos.cabecalho.titulo' "$PBDOC_CABECALHO_TITULO"
    fi

    if [ -n "$PBDOC_CABECALHO_SUBTITULO" ]; then
        sed_replace 'sigaex.modelos.cabecalho.subtitulo' "$PBDOC_CABECALHO_SUBTITULO"
    fi

    if is_true "$PBDOC_PREFEITURA"; then
        sed_replace 'siga.prefeitura' "true"
    fi

    if [ -n "$PBDOC_PREFEITURA_CABECALHO" ]; then
        sed_replace 'siga.prefeitura.cabecalho' "$PBDOC_PREFEITURA_CABECALHO"
    fi

    if [ -n "$PBDOC_PREFEITURA_TEMA_COR" ]; then
        sed_replace 'siga.prefeitura.tema.cor' "$PBDOC_PREFEITURA_TEMA_COR"
    fi

    if [ -n "$PBDOC_RELATORIO_TITULO" ]; then
        sed_replace 'siga.relat.titulo' "$PBDOC_RELATORIO_TITULO"
    fi

    if [ -n "$PBDOC_RELATORIO_SUBTITULO" ]; then
        sed_replace 'siga.relat.subtitulo' "$PBDOC_RELATORIO_SUBTITULO"
    fi

    if [ -n "$PBDOC_CARIMBO_TEXTO_SUPERIOR" ]; then
        sed_replace 'sigaex.carimbo.texto.superior' "$PBDOC_CARIMBO_TEXTO_SUPERIOR"
    fi

    if [ -n "$PBDOC_JWT_SECRET" ]; then
        sed_replace 'siga.jwt.secret' "$PBDOC_JWT_SECRET"
    fi

    if [ -n "$PBDOC_SINC_PASSWORD" ]; then
        sed_replace 'siga.sinc.password' "$PBDOC_SINC_PASSWORD"
    fi

    if [ -n "$PBDOC_WEBDAV_SENHA" ]; then
        sed_replace 'sigaex.webdav.senha' "$PBDOC_WEBDAV_SENHA"
    fi

    if [ -n "$PBDOC_RECAPTCHA_KEY" ]; then
        sed_replace 'siga.recaptcha.key' "$PBDOC_RECAPTCHA_KEY"
    fi

    if [ -n "$PBDOC_RECAPTCHA_PASSWORD" ]; then
        sed_replace 'siga.recaptcha.pwd' "$PBDOC_RECAPTCHA_PASSWORD"
    fi

    if is_true "$PBDOC_FLYWAY_MIGRATE"; then
        sed_replace 'siga.flyway.migrate' "true"
    fi
}

configure

exec $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0
