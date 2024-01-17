package br.gov.jfrj.siga.vraptor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.dp.dao.CpDao;

@Controller
public class ExImagemController extends ExController {
	
	@SuppressWarnings("deprecation")
	public ExImagemController() {}
	
	@Inject
	public ExImagemController(
			HttpServletRequest request,
			HttpServletResponse response,
			ServletContext context,
			Result result,
			SigaObjects so,
			EntityManager em) {

		super(request, response, context, result, CpDao.getInstance(), so, em);
	}
	
	@Get("/public/app/imagem/brasao")
	public Download getImagemBrasao() {		
		return new ByteArrayDownload(Utils.getImagemBrasao(), "image/png", "brasao");
	}

}
