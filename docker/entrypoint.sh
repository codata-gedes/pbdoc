#!/bin/sh

set -e

standalone_xml=$JBOSS_HOME/standalone/configuration/standalone.xml

sed_replace() {
    property=$1
    replacement=$2
    shift 2

    if [ -n "$replacement" ]; then
        # escapa contra barra
        replacement="${replacement//\\/\\\\}"
        # escapa E comercial
        replacement="${replacement//&/\\&}"

        spec=$(printf 's|<property name="(%s)" value=".*?"\s*/>|<property name="\\1" value="%s"/>|' "$property" "${replacement//&/\&}")

        sed --in-place --regexp-extended "$spec" $standalone_xml
    fi
}

configure_database() {
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
}

configure_pbdoc() {
    sed_replace 'siga.base.url' "$PBDOC_BASE_URL"
    sed_replace 'siga.pagina.inicial.url' "$PBDOC_BASE_URL/sigaex/app/mesa"
    sed_replace 'siga.ambiente' "$PBDOC_AMBIENTE"
    sed_replace 'siga.prefeitura' "$PBDOC_PREFEITURA"
    sed_replace 'siga.consulta.processos' "$PBDOC_CONSULTA_PROCESSOS"
    sed_replace 'siga.consulta.processos.link' "$PBDOC_CONSULTA_PROCESSOS_LINK"
    sed_replace 'sigaex.modelos.cabecalho.titulo' "$PBDOC_CABECALHO_TITULO"
    sed_replace 'sigaex.modelos.cabecalho.subtitulo' "$PBDOC_CABECALHO_SUBTITULO"
    sed_replace 'sigaex.carimbo.texto.superior' "$PBDOC_CARIMBO_TEXTO_SUPERIOR"
    sed_replace 'siga.prefeitura.cabecalho' "$PBDOC_PREFEITURA_CABECALHO"
    sed_replace 'siga.brasao.width' "$PBDOC_BRASAO_WIDTH"
    sed_replace 'siga.brasao.height' "$PBDOC_BRASAO_HEIGHT"
    sed_replace 'siga.relat.titulo' "$PBDOC_RELATORIO_TITULO"
    sed_replace 'siga.relat.subtitulo' "$PBDOC_RELATORIO_SUBTITULO"
    sed_replace 'siga.jwt.secret' "$PBDOC_JWT_SECRET"
    sed_replace 'siga.sinc.password' "$PBDOC_SINC_PASSWORD"
    sed_replace 'sigaex.webdav.senha' "$PBDOC_WEBDAV_SENHA"
    sed_replace 'siga.recaptcha.key' "$PBDOC_RECAPTCHA_KEY"
    sed_replace 'siga.recaptcha.pwd' "$PBDOC_RECAPTCHA_PASSWORD"
    sed_replace 'siga.flyway.migrate' "$PBDOC_FLYWAY_MIGRATE"
    sed_replace 'vizservice.url' "$PBDOC_VIZSERVICE_URL"
    sed_replace 'blucservice.url' "$PBDOC_BLUCSERVICE_URL"
    sed_replace 'sigaex.diretorio.armazenamento.arquivos' "$PBDOC_ARMAZENAMENTO_ARQUIVOS"
    sed_replace 'siga.smtp' "$PBDOC_SMTP"
    sed_replace 'siga.smtp.porta' "$PBDOC_SMTP_PORTA"
    sed_replace 'siga.smtp.auth' "$PBDOC_SMTP_AUTH"
    sed_replace 'siga.smtp.auth.usuario' "$PBDOC_SMTP_AUTH_USUARIO"
    sed_replace 'siga.smtp.auth.senha' "$PBDOC_SMTP_AUTH_SENHA"
    sed_replace 'siga.smtp.debug' "$PBDOC_SMTP_DEBUG"
    sed_replace 'siga.smtp.usuario.remetente' "$PBDOC_SMTP_USUARIO_REMETENTE"
}

configure_database
configure_pbdoc

exec $JBOSS_HOME/bin/standalone.sh -b 0.0.0.0
