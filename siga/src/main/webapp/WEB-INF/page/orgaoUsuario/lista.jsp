<%@ page language="java" contentType="text/html; charset=UTF-8"
	buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<siga:pagina titulo="Lista Orgão Usuário">
<script type="text/javascript" language="Javascript1.1">
function sbmt(offset) {
	if (offset == null) {
		offset = 0;
	}
	frm.elements["paramoffset"].value = offset;
	frm.elements["p.offset"].value = offset;
	frm.submit();
}
</script>
<form name="frm" action="listar" class="form" method="GET">
	<input type="hidden" name="paramoffset" value="0" />
	<input type="hidden" name="p.offset" value="0" />
	<div class="container-fluid">
		<div class="card bg-light mb-3" >		
			<div class="card-header"><h5>Cadastro de &Oacute;rg&atilde;o</h5></div>
				<div class="card-body">
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<label>Nome</label>
								<input type="text" id="nome" name="nome" value="${nome}" maxlength="100" size="30" class="form-control"/>
							</div>						
						</div>
					</div>
				
					<div class="row">
						<div class="col-sm-6">
							<input type="submit" value="Pesquisar" class="btn btn-primary"/>
						</div>
					</div>
				</div>
			</div>
		<c:if test="${not empty mensagem}">
			<div style="align-items: center">
				<div>
					<p id="mensagem" style="text-align: center" class="alert alert-success">${mensagem}</p>
					<script>
						setTimeout(function() {
							$('#mensagem').fadeTo(1000, 0, function() {
								$('#mensagem').slideUp(1000);
							});
						}, 5000);
					</script>
				</div>
			</div>
		</c:if>
			<!-- main content -->
			<h5>&Oacute;rg&atilde;os cadastrados</h5>
				<table border="0" class="table table-sm table-striped">
					<thead class="${thead_color}">
						<tr>
							<th class="text-left w-10">ID</th>
							<th class="text-left w-30">Nome</th>
							<th class="text-center w-10">Sigla</th>
							<th class="text-center w-20">Sigla Completa</th>
							<th class="text-center w-10" hidden="true">Externo</th>
							<th class="text-left w-10">Data Contrato</th>
							<th colspan="2" class="text-left w-10">Op&ccedil;&otilde;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<siga:paginador maxItens="15" maxIndices="10" totalItens="${tamanho}"
							itens="${itens}" var="orgaoUsuarioTupla">

							<c:set var="orgaoUsuario" value="${orgaoUsuarioTupla.get(0, Object.class)}" />
							<c:set var="dtContratoOrgaoUsuario" value="${orgaoUsuarioTupla.get(1, Object.class)}" />

							<tr>
								<td class="text-left w-10">${orgaoUsuario.id}</td>
								<td class="text-left w-30">${orgaoUsuario.descricao}</td>
								<td class="text-center w-10">${orgaoUsuario.sigla}</td>
								<td class="text-center w-20">${orgaoUsuario.siglaOrgaoUsuarioCompleta}</td>
								<td class="text-center w-10" hidden="true">${orgaoUsuario.isExternoOrgaoUsu  == 1 ? 'SIM' : 'NÃO'}</td>
								<td class="text-left w-10"><fmt:formatDate value="${dtContratoOrgaoUsuario}" pattern="dd/MM/yyyy" /></td>
								<td class="text-left w-10">
									<c:url var="url" value="/app/orgaoUsuario/editar">
										<c:param name="id" value="${orgaoUsuario.id}"></c:param>
									</c:url>
									<c:if test="${usuarioPodeAlterar && orgaoUsuarioSiglaLogado eq orgaoUsuario.sigla}">
									<input type="button" value="Alterar"
										onclick="javascript:window.location.href='${url}'"
										class="btn btn-primary">
									</c:if>					
								</td>
							<%--	<td align="left">									
	 			 					<a href="javascript:if (confirm('Deseja excluir o orgão?')) location.href='/siga/app/orgao/excluir?id=${orgao.idOrgao}';">
										<img style="display: inline;"
										src="/siga/css/famfamfam/icons/cancel_gray.png" title="Excluir orgão"							
										onmouseover="this.src='/siga/css/famfamfam/icons/cancel.png';" 
										onmouseout="this.src='/siga/css/famfamfam/icons/cancel_gray.png';"/>
									</a>															
								</td>
							 --%>							
							</tr>
						</siga:paginador>
					</tbody>
				</table>				
			
			<c:if test="${usuarioPodeAlterar}">
			<div class="gt-table-buttons">
					<c:url var="url" value="/app/orgaoUsuario/editar"></c:url>
					<input type="button" value="Incluir"
						onclick="javascript:window.location.href='${url}'"
						class="btn btn-primary">
				</div>
			</c:if>			
		</div>

</form>
</siga:pagina>