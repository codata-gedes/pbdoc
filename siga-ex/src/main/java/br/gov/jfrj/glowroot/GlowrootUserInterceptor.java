package br.gov.jfrj.glowroot;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.gov.jfrj.siga.dp.DpPessoa;
import br.gov.jfrj.siga.vraptor.SigaObjects;
import org.glowroot.agent.api.Glowroot;

import javax.inject.Inject;

@Intercepts
public class GlowrootUserInterceptor {

    @Inject
    private SigaObjects sigaObjects;

    @AroundCall
    public void intercept(SimpleInterceptorStack stack) {
        try {
            // Recupera o usuário logado na app
            DpPessoa pessoa = sigaObjects.getCadastrante();

            // Insere em "Slow Traces" do glowroot as informações do usuário que executou a rota do módulo SIGA-EX
            if (pessoa != null && pessoa.getSigla() != null) {
                Glowroot.setTransactionUser(pessoa.getSigla());
                Glowroot.addTransactionAttribute("[PBDOC-SIGA-EX] Usuario", pessoa.getNomePessoa());
                Glowroot.addTransactionAttribute("[PBDOC-SIGA-EX] Sigla", pessoa.getSigla());
                Glowroot.addTransactionAttribute("[PBDOC-SIGA-EX] CPF", pessoa.getCpfFormatado());
                if(pessoa.getOrgaoUsuario() != null) {
                    Glowroot.addTransactionAttribute("[PBDOC-SIGA-EX] Orgao", pessoa.getOrgaoUsuario().getNmOrgaoUsu());
                }
                if(pessoa.getLotacao() != null) {
                    Glowroot.addTransactionAttribute("[PBDOC-SIGA-EX] Lotacao", pessoa.getLotacao().getNomeLotacao());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        stack.next();
    }
}
