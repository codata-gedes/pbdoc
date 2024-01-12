package br.gov.jfrj.siga.vraptor;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.gov.jfrj.siga.base.util.Utils;

@Controller
public class ExImagemController extends SigaController {
	
	@SuppressWarnings("deprecation")
	public ExImagemController() {}
	
	@Get({ "/app/imagem/brasao" })
	public Download getImagemBrasao() {
		return new ByteArrayDownload(Utils.getImagemBrasao(), "image/png", "brasao");
	}
	
}
