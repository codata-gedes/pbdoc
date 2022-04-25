<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/customtag" prefix="tags"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://localhost/functiontag" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Protocolo de Arquivamento" popup="true">
	<style>
	   @media print { 
	       #btn-form { display:none; } 
	   }
	</style>
	<!-- main content bootstrap -->
	<div class="container-fluid">
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Protocolo de Arquivamento</h5>
			</div>

			<div class="card-body">

				<table class="table table-bordered table-light">
					<tr>
						<td>Data</td>
						<td colspan="2">${mov.dtRegMovDDMMYYHHMMSS}</td>
					</tr>
				</table>
			</div>
		</div>
		<div class="card bg-light mb-3">
			<div class="card-header">
				<h5>Documento(s)</h5>
			</div>

			<div class="card-body">
				<table class="table table-striped" id="tbl-docs">
					<col width="22%" />
					<col width="5%" />
					<col width="4%" />
					<col width="4%" />
					<col width="5%" />
					<col width="4%" />
					<col width="4%" />
					<col width="4%" />
					<col width="4%" />
					<col width="44%" />
					<thead class="${thead_color} align-middle text-center">
						<tr>
							<th rowspan="2" class="text-right">Número</th>
							<th colspan="3">Documento</th>
							<th colspan="3">Última Movimentação</th>
							<th colspan="2">Atendente</th>
							<th rowspan="2" class="text-left">Descrição</th>
						</tr>
						<tr>
							<th>Data</th>
							<th><fmt:message key="usuario.lotacao"/></th>
							<th><fmt:message key="usuario.pessoa2"/></th>
							<th>Data</th>
							<th><fmt:message key="usuario.lotacao"/></th>
							<th><fmt:message key="usuario.pessoa2"/></th>
							<th><fmt:message key="usuario.lotacao"/></th>
							<th><fmt:message key="usuario.pessoa2"/></th>
						</tr>

					<c:forEach var="documento" items="${itens}">
						<tr>
							<td>
								<a href="${pageContext.request.contextPath}/app/expediente/doc/exibir?sigla=${documento[1].exMobil.sigla}">${documento[1].exMobil.codigo}</a>
								<c:if test="${not documento[1].exMobil.geral}">
									<td>${documento[0].dtDocDDMMYY}</td>
									<td><siga:selecionado
												sigla="${documento[0].lotaSubscritor.sigla}"
												descricao="${documento[0].lotaSubscritor.descricao}" /></td>
										<td><siga:selecionado
												sigla="${documento[0].subscritor.iniciais}"
												descricao="${documento[0].subscritor.descricao}" /></td>
										<td>${documento[1].dtMovDDMMYY}</td>
										<td><siga:selecionado
												sigla="${documento[1].lotaSubscritor.sigla}"
												descricao="${documento[1].lotaSubscritor.descricao}" /></td>
										<td><siga:selecionado
												sigla="${documento[1].subscritor.iniciais}"
												descricao="${documento[1].subscritor.descricao}" /></td>
										<td><siga:selecionado
												sigla="${documento[1].lotaResp.sigla}"
												descricao="${documento[1].lotaResp.descricao}" /></td>
										<td><siga:selecionado
												sigla="${documento[1].resp.iniciais}"
												descricao="${documento[1].resp.descricao}" /></td>
								</c:if> 
								<c:if test="${documento[1].exMobil.geral}">
									<td>${documento[0].dtDocDDMMYY}</td>
									<td><siga:selecionado
											sigla="${documento[0].subscritor.iniciais}"
											descricao="${documento[0].subscritor.descricao}" /></td>
									<td><siga:selecionado
											sigla="${documento[0].lotaSubscritor.sigla}"
											descricao="${documento[0].lotaSubscritor.descricao}" /></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</c:if>
								<td class="text-left">
									<c:choose>
										<c:when test="${siga_cliente!='GOVSP'}">
											${f:descricaoConfidencial(documento[0], lotaTitular)}
										</c:when>
										<c:otherwise>
											${documento[0].descrDocumento}
										</c:otherwise>
									</c:choose>
								</td>
						</tr>
					</c:forEach>


				</table>
			</div>
		</div>

		<br />
		<div id="btn-form">
			<form name="frm" action="principal" namespace="/" method="get"
				theme="simple">
				<button type="button" class="btn btn-primary" onclick="javascript: document.body.offsetHeight; window.print();" >Imprimir</button>
				<c:if test="${popup != true}">
					<button type="button" class="btn btn-primary" onclick="javascript:history.back();" >Voltar</button>
				</c:if>
			</form>
		</div>
		<br /> <br />
		<div>
			<br /> <br />
			<p align="center">Recebido em: _____/_____/_____ às _____:_____</p>
			<br /> <br /> <br />
			<p align="center">________________________________________________</p>
			<p align="center">Assinatura do Servidor</p>
		</div>
	</div>
</siga:pagina>