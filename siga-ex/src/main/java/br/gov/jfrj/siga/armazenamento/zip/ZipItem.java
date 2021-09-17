package br.gov.jfrj.siga.armazenamento.zip;

import java.util.Objects;

import org.apache.commons.io.FilenameUtils;

public interface ZipItem {

	static final String DOC_NOME_PADRAO_ITEM = "doc";

	String getNome();

	public enum Tipo implements ZipItem {

		/*
		 * Tipos de ZipItem suportados pelo PBdoc
		 */

		RTF,
		DOCX,
		DOC,
		XLSX,
		XLS,
		PPT,
		PPTX,
		PDF,
		FORM,
		XML,
		HTM,
		HTML,
		RESUMO,
		JPG,
		JPEG,
		PNG,
		BMP,
		FTL,
		;

		@Override
		public String getNome() {
			return this.getNomeZipItem(DOC_NOME_PADRAO_ITEM);
		}

		String getNomeZipItem(String nomeSemExtensao) {
			return nomeSemExtensao + "." + this.name().toLowerCase();
		}

		public ZipItem comNome(String nomeSemExtensao) {
			return new TipoPersonalizado(this, nomeSemExtensao);
		}

		public static final ZipItem porNomeItem(String nomeItemComExtensao) {
			String nome = FilenameUtils.getBaseName(nomeItemComExtensao);
			ZipItem.Tipo tipo = porNomeItemPadrao(nomeItemComExtensao);

			// Tipo com arquivos de nomes personalizados
			if (!DOC_NOME_PADRAO_ITEM.equals(nome)) {
				return tipo.comNome(nome);
			}
			return tipo;
		}

		public static final ZipItem.Tipo porNomeItemPadrao(String nomeItemComExtensao) {
			String extensao = FilenameUtils.getExtension(nomeItemComExtensao);
			return valueOf(extensao.toUpperCase());
		}

	}

	final class TipoPersonalizado implements ZipItem {

		private String nomeSemExtensao;
		private Tipo tipo;

		TipoPersonalizado(Tipo tipo, String nomeSemExtensao) {
			this.tipo = tipo;
			this.nomeSemExtensao = nomeSemExtensao;
		}

		@Override
		public String getNome() {
			return this.tipo.getNomeZipItem(this.nomeSemExtensao);
		}

		@Override
		public int hashCode() {
			return Objects.hash(nomeSemExtensao, tipo);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			TipoPersonalizado other = (TipoPersonalizado) obj;
			return Objects.equals(nomeSemExtensao, other.nomeSemExtensao) && tipo == other.tipo;
		}

	}

}
