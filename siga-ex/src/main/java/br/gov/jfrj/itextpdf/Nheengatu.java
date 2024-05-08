/*******************************************************************************
 * Copyright (c) 2006 - 2011 SJRJ.
 * 
 *     This file is part of SIGA.
 * 
 *     SIGA is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     SIGA is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with SIGA.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package br.gov.jfrj.itextpdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.aryjr.nheengatu.pdf.HTML2PDFParser;
import com.aryjr.nheengatu.pdf.PDFDocument;

import org.jboss.logging.Logger;

import br.gov.jfrj.siga.base.util.Texto;

public class Nheengatu implements ConversorHtml {

	private final HTML2PDFParser parser;
	private static final Logger log = Logger.getLogger(Nheengatu.class);
	private final int TIMEOUT_SECONDS = 32;

	public Nheengatu() {
		parser = new HTML2PDFParser();
	}

	public byte[] converter(String sHtml, byte output) throws Exception {
		try (ByteArrayOutputStream bo = new ByteArrayOutputStream()) {
		parser.parse(new ByteArrayInputStream(sHtml.getBytes("utf-8")),
				extract(sHtml, "<!-- INICIO PRIMEIRO CABECALHO",
						"FIM PRIMEIRO CABECALHO -->"), extract(sHtml,
						"<!-- INICIO PRIMEIRO RODAPE",
						"FIM PRIMEIRO RODAPE -->"), extract(sHtml,
						"<!-- INICIO CABECALHO", "FIM CABECALHO -->"), extract(
						sHtml, "<!-- INICIO RODAPE", "FIM RODAPE -->"));
		
		final PDFDocument pdf = parser.getPdf();
		
		final ExecutorService executor = Executors.newSingleThreadExecutor();
		final Future<?> future = executor.submit(() -> {
			try {
				pdf.generateFile(bo);
			} catch (Exception e) {
				log.debug("Erro na geração do PDF: " + e.getMessage());
				throw new RuntimeException("Erro na geração do PDF. " + e.getMessage(), e);
			}
		});

        try {
            future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            log.debug("Erro na geração do PDF: timeout exception. " + e.getMessage());
			throw new Exception("Timeout na geração do PDF. " + e.getMessage(), e);
        } catch (Exception e) {
            future.cancel(true);
			throw new Exception("Erro na geração do PDF. " + e.getMessage(), e);
        } finally {
        	shutdownAndAwaitTermination(executor);
        }

		return bo.toByteArray();
		} catch(Throwable t){
			throw new Exception(t);
		}
	}

	private static InputStream extract(final String sSource,
			final String sBegin, final String sEnd)
			throws UnsupportedEncodingException {
		String sResult = Texto.extrai(sSource, sBegin, sEnd);
		if (sResult == null)
			return null;
		return new ByteArrayInputStream(sResult.getBytes("utf-8"));
	}
	
	
	void shutdownAndAwaitTermination(ExecutorService executor) {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
				executor.shutdownNow();
				if (!executor.awaitTermination(1, TimeUnit.SECONDS))
					log.debug("Erro na geração do PDF: timeout exception.");
				}
			} catch (InterruptedException ie) {
				executor.shutdownNow();
				Thread.currentThread().interrupt();
			}
		 }
}
