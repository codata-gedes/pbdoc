package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.net.HttpHeaders;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.ex.ExDocumento;
import br.gov.jfrj.siga.ex.ExMovimentacao;

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
	public void exibeDocumentoPorId(final Long id) throws Exception {
		final ExDocumento documento = dao().consultar(id, ExDocumento.class, false);
		result.redirectTo(ExDocumentoController.class)
				.exibe(false, documento.getSigla(), null, null, null, false);
	}

	@Get({ "/app/anexo/{id}" })
	public void exibeMovimentacaoPorId(final Long id) throws Exception {
		final ExMovimentacao movimentacao = dao().consultar(id, ExMovimentacao.class, false);

		// Previne controle de etag causando HTTP 304
		request.removeAttribute(HttpHeaders.IF_NONE_MATCH);
		result.redirectTo(ExArquivoController.class)
				.aExibir(null, false, movimentacao.getReferenciaPDF(), null, null, null, null, false, false, false, null, false);
	}

}
