package br.gov.jfrj.siga.vraptor;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.cp.bl.Cp;
import br.gov.jfrj.siga.cp.bl.CpConfiguracaoBL;

@RequestScoped
@Intercepts(after = ParameterOptionalLoaderInterceptor.class)
public class AccessAuthInterceptor {

	private static final String ACESSO_WEB_BROWSER = "SIGA:Sistema Integrado de Gestão Administrativa;WEB:Acesso via Web Browser;";
	private static final String ATRIBUTO_LOGIN_MENSAGEM = "loginMensagem";
	private static final String PREFIXO_PROTOCOLO_HTTP = "http:";
	private static final String PREFIXO_PROTOCOLO_HTTPS = "https:";

	@Inject
	private SigaObjects so;
    @Inject
    private HttpServletRequest request;
    @Inject
    private HttpServletResponse response;

    /**
	 * @deprecated CDI eyes only
	 */
	public AccessAuthInterceptor() {
	}

	@AroundCall
	public void intercept(SimpleInterceptorStack stack) throws InterceptionException, UnsupportedEncodingException, IOException {
		if (so.getCadastrante() != null) {
			if (!podeFazerLogin()) {
				final HttpSession session = request.getSession(false);
				if (session != null) {
					session.setAttribute(ATRIBUTO_LOGIN_MENSAGEM, "Acesso não permitido via Web Browser para o usuário " + so.getCadastrante());
				}

				final String base = Prop.get("/siga.base.url");
				final String queryParams = request.getQueryString() != null ? "?" + request.getQueryString() : EMPTY;

				String cont = request.getRequestURL().append(queryParams).toString();
				if (base != null && base.startsWith(PREFIXO_PROTOCOLO_HTTPS) && cont.startsWith(PREFIXO_PROTOCOLO_HTTP)) {
					cont = PREFIXO_PROTOCOLO_HTTPS + cont.substring(PREFIXO_PROTOCOLO_HTTP.length());
				}

				response.sendRedirect("/siga/public/app/login?cont=" + URLEncoder.encode(cont, UTF_8.name()));
				return;
			}
		}
		stack.next();
	}

	private boolean podeFazerLogin() {
		final CpConfiguracaoBL config = Cp.getInstance().getConf();
		return config.podeUtilizarServicoPorConfiguracao(
				this.so.getCadastrante(),
				this.so.getLotaTitular(),
				ACESSO_WEB_BROWSER
		);
	}

}
