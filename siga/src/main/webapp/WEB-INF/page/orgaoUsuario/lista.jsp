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

function gerenciarAtivacao(id, codigoDeIntegracao, sigla, acao) {
	const aceite = confirm('Deseja ' + acao + ' o órgão [' + codigoDeIntegracao + '] ' + sigla + '?');
	if (!aceite) {
		return;
	}

	const acoes = ['ativar', 'desativar'];
	if (acoes.indexOf(acao) < 0) {
		return;
	}

	$.ajax({
	    type: 'POST',
    	url: '/siga/app/orgaoUsuario/' + acao + '?codigoDeIntegracao=' + codigoDeIntegracao,
    	data: {
    		id: id,
    		codigoDeIntegracao: codigoDeIntegracao
    	}
	})
	.done(() => {
		alert('Sucesso na ' + acao + ' do Órgão ' + sigla + '!');
		window.location.reload();
	})
	.fail(e => {
		alert('Não é possível atualizar o órgão ' + sigla + ' porque já existe outro órgão com o código de integração ativo.');
	});
}
function cpOrgaoUsuarioAtivar(id, codigoDeIntegracao, sigla) {
	gerenciarAtivacao(id, codigoDeIntegracao, sigla, 'ativar');
}
function cpOrgaoUsuarioDesativar(id, codigoDeIntegracao, sigla) {
	gerenciarAtivacao(id, codigoDeIntegracao, sigla, 'desativar');
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
							<th class="text-center w-10" title="Código interno utilizado para integração com outros sistemas">C&oacute;digo</th>
							<th class="text-center w-10" title="Sigla utilizada na constituição de documentos">Acrônimo</th>
							<th class="text-center w-20">Sigla Oficial</th>
							<th class="text-left w-40">Nome</th>
							<th colspan="2" class="text-left w-20">A&ccedil;&otilde;es</th>					
						</tr>
					</thead>
					
					<tbody>
						<siga:paginador maxItens="30" maxIndices="10" totalItens="${tamanho}"
							itens="${itens}" var="orgaoUsuarioTupla">

							<c:set var="orgaoUsuario" value="${orgaoUsuarioTupla.get(0, Object.class)}" />
							<c:set var="dtContratoOrgaoUsuario" value="${orgaoUsuarioTupla.get(1, Object.class)}" />

							<tr>
								<td class="text-center w-10">
									<i title="${orgaoUsuario.hisAtivo == 1 ? 'Ativo' : 'Inativo'}"
									   class="fa ${orgaoUsuario.hisAtivo == 1 ? 'fa-check text-success' : 'fa-times text-danger'}"></i>
									${orgaoUsuario.codOrgaoUsuFormatado}
								</td>
								<td class="text-center w-10">${orgaoUsuario.sigla}</td>
								<td class="text-center w-20">${orgaoUsuario.siglaOrgaoUsuarioCompleta}</td>
								<td class="text-left w-40">
									${orgaoUsuario.descricao}
									<small class="text-muted" title="ID de banco de dados: #${orgaoUsuario.id}">#${orgaoUsuario.id}</small>
								</td>
								<td class="text-left w-20">
									<c:url var="url" value="/app/orgaoUsuario/editar">
										<c:param name="id" value="${orgaoUsuario.id}"></c:param>
									</c:url>
									<c:if test="${administrador || (usuarioPodeAlterar && orgaoUsuarioSiglaLogado eq orgaoUsuario.sigla)}">
										<button type="button" onclick="javascript:window.location.href='${url}'" class="btn btn-sm btn-primary" style="min-width: 8em;">
											<i class="fa fa-edit"></i>&nbsp;&nbsp;Alterar
										</button>
									</c:if>
									<c:choose>
									<c:when test="${orgaoUsuario.hisAtivo == 1}">
										<button type="button" onclick="cpOrgaoUsuarioDesativar(${orgaoUsuario.id}, ${orgaoUsuario.codOrgaoUsu}, '${orgaoUsuario.siglaOrgaoUsuarioCompleta}')" class="btn btn-sm btn-danger" style="min-width: 8em;">
											<i class="fa fa-times"></i>&nbsp;&nbsp;Desativar
										</button>
									</c:when>
									<c:otherwise>
										<button type="button" onclick="cpOrgaoUsuarioAtivar(${orgaoUsuario.id}, ${orgaoUsuario.codOrgaoUsu}, '${orgaoUsuario.siglaOrgaoUsuarioCompleta}')" class="btn btn-sm btn-success" style="min-width: 8em;">
											<i class="fa fa-check"></i>&nbsp;&nbsp;Ativar
										</button>
									</c:otherwise>
									</c:choose>
								</td>
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