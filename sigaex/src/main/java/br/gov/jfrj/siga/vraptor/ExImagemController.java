package br.gov.jfrj.siga.vraptor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.observer.download.ByteArrayDownload;
import br.com.caelum.vraptor.observer.download.Download;
import br.gov.jfrj.siga.base.AplicacaoException;
import br.gov.jfrj.siga.base.Prop;
import br.gov.jfrj.siga.base.util.Utils;
import br.gov.jfrj.siga.ex.ExTipoMovimentacao;


@Controller
public class ExImagemController extends SigaController {
	
	@SuppressWarnings("deprecation")
	public ExImagemController() {}
	
	@Get({ "/app/imagem/brasao" })
	public Download getBrasaoPath() {
		return new ByteArrayDownload(Utils.getImagemBrasao(), "image/png", "brasao");
	}
	
}