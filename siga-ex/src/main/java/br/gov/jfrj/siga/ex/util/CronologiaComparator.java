package br.gov.jfrj.siga.ex.util;

import java.util.Comparator;

import br.gov.jfrj.siga.ex.ExMovimentacao;

public class CronologiaComparator implements Comparator<ExMovimentacao> {

	public static final CronologiaComparator INSTANCE = new CronologiaComparator();

	public int compare(ExMovimentacao o1, ExMovimentacao o2) {
		try {
			int i = 0;

			// Pela data
			if (o2.getDtIniMov() != null && o1.getDtIniMov() != null) {
				i = o2.getDtIniMov().compareTo(o1.getDtIniMov());
			}

			if (i != 0) {
				return i;
			}

			// Por uma estar cancelada e a outra não
			if (o1.isCancelada() && !o2.isCancelada()) {
				return Integer.MAX_VALUE;
			}
			if (!o1.isCancelada() && o2.isCancelada()) {
				return Integer.MIN_VALUE;
			}

			// Pelo tipo da movimentação
			if (o1.getExTipoMovimentacao() != null && o2.getExTipoMovimentacao() != null) {
				i = ExMovimentacao.tpMovDesempatePosicao(
						o1.getExTipoMovimentacao().getId(),
						o2.getExTipoMovimentacao().getId()
				);
				if (i != 0) {
					return i;
				}
			}

			// Pela ID
			i = o2.getIdMov().compareTo(o1.getIdMov());
			return i;
		} catch (final Exception ex) {
			return 0;
		}
	}
}