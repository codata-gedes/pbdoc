package br.gov.jfrj.siga.armazenamento.zip;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

public interface ZipItem {

	static final String DOC_NOME_PADRAO_ITEM = "doc";

	default Set<String> parseTiposMime(String[] tiposMime) {
		return ArrayUtils.isNotEmpty(tiposMime)
				? new LinkedHashSet<>(asList(tiposMime))
				: emptySet();
	}

	default String getTipoMimePadrao() {
		final Set<String> tiposMime = getTiposMime();
		return isNotEmpty(tiposMime) ? tiposMime.iterator().next() : null;
	}

	String getNome();

	Set<String> getTiposMime();

	public enum Tipo implements ZipItem {

		/*
		 * Tipos de ZipItem suportados pelo PBdoc
		 */

		RTF("application/rtf"),
		DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
		DOC("application/msword"),
		XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
		XLS("application/vnd.ms-excel"),
		PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation"),
		PPT("application/vnd.ms-powerpoint"),
		PDF("application/pdf"),
		FORM("text/plain"),
		XML("application/xml", "text/xml"),
		HTM("text/html"),
		HTML("text/html"),
		RESUMO("text/plain"),
		JPG("image/jpeg"),
		JPEG("image/jpeg"),
		PNG("image/png"),
		BMP("image/bmp"),
		FTL("text/ftl"),
		P7S("application/pkcs7-signature"),
		;

		private final Set<String> tiposMime;

		private Tipo(String... tiposMime) {
			this.tiposMime = parseTiposMime(tiposMime);
		}

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

		@Override
		public Set<String> getTiposMime() {
			return this.tiposMime;
		}

	}

	final class TipoPersonalizado implements ZipItem {

		private String nomeSemExtensao;
		private Tipo tipo;

		private TipoPersonalizado(Tipo tipo, String nomeSemExtensao) {
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

		@Override
		public Set<String> getTiposMime() {
			return this.tipo.getTiposMime();
		}

	}

}
