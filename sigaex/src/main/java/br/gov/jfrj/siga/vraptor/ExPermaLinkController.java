package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;
import br.gov.jfrj.siga.util.RateLimiter;

import com.google.common.net.HttpHeaders;

import static br.gov.jfrj.siga.util.RateLimiter.isWithinRateLimiterTimeout;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class ExPermaLinkController extends ExController {

	@SuppressWarnings("deprecation")
	public ExPermaLinkController() {}

	@Inject
	public ExPermaLinkController(
			HttpServletRequest request,
			HttpServletResponse response,
			ServletContext context,
			Result result,
			SigaObjects so,
			EntityManager em) {

		super(request, response, context, result, CpDao.getInstance(), so, em);
	}

	@Get({ "/app/documento/{id}" })
	public void exibeDocumentoPorId(final Long id) {
		final ExDocumento documento = dao().consultar(id, ExDocumento.class, false);
		result.redirectTo("/app/expediente/doc/exibir?sigla=" + documento.getSigla());
	}

	@Get({ "/app/anexo/{id}" })
	public void exibeMovimentacaoPorId(final Long id) {

		if (!isWithinRateLimiterTimeout("ExPermaLinkController#exibeMovimentacaoPorId", 2_000)) {
            throw new AplicacaoException(RateLimiter.DEFAULT_RATE_LIMITER_MESSAGE);
        }

		final ExMovimentacao movimentacao = dao().consultar(id, ExMovimentacao.class, false);

		// Previne controle de etag causando HTTP 304
		request.removeAttribute(HttpHeaders.IF_NONE_MATCH);

		result.redirectTo("/app/arquivo/exibir?arquivo=" + movimentacao.getReferenciaPDF());
	}

}
