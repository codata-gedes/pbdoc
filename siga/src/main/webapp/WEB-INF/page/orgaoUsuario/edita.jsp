<%@ page language="java" contentType="text/html; charset=UTF-8" buffer="64kb"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://localhost/jeetags" prefix="siga"%>


<siga:pagina titulo="Cadastro de Orgãos">

<script type="text/javascript">
	function validar() {

		const codOrgUsuInput = document.getElementById('codOrgUsu');
		const codOrgUsu = codOrgUsuInput.value;
		if (!codOrgUsu) {
			sigaModal.alerta("Preencha o código interno do Órgão.");
			codOrgUsuInput.focus();
			return;
		}

		const siglaOrgaoUsuarioInput = document.getElementById('siglaOrgaoUsuario');	
		const siglaOrgaoUsuario = siglaOrgaoUsuarioInput.value;	
		if (!siglaOrgaoUsuario || siglaOrgaoUsuario.length != 3) {
			sigaModal.alerta("Preencha o Acrônimo do Órgão com exatamente 3 letras. Ex.: \"ABC\"");
			siglaOrgaoUsuarioInput.focus();
			return;
		}

		const siglaOrgaoUsuarioCompletaInput = document.getElementById('siglaOrgaoUsuarioCompleta');
		const siglaOrgaoUsuarioCompleta = siglaOrgaoUsuarioCompletaInput.value;
		if (!siglaOrgaoUsuarioCompleta) {
			sigaModal.alerta("Preencha a sigla Completa do Órgão.");
			siglaOrgaoUsuarioCompletaInput.focus();
			return;
		}

		const nmOrgaoUsuarioInput = document.getElementById('nmOrgaoUsuario');
		const nmOrgaoUsuario = nmOrgaoUsuarioInput.value;
		if (!nmOrgaoUsuario) {
			sigaModal.alerta("Preencha o nome do Órgão.");
			nmOrgaoUsuarioInput.focus();
			return;
		}

		frm.submit();
	}

	function apenasLetras(event) {
		return /^[a-z]$/i.test(event.key);
	}

	function apenasNumeros(event) {
		return /^[0-9]$/i.test(event.key);
	}
</script>

<body>

<div class="container-fluid">
	<div class="card bg-light mb-3" >		
		<form name="frm" action="${request.contextPath}/app/orgaoUsuario/gravar" method="POST">
			<input type="hidden" name="postback" value="1" /> 
			<div class="card-header"><h5>Cadastro de Órgão Usuário</h5></div>
				<div class="card-body">
					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Código</label>
						        <input type="text" id="codOrgUsu" name="codOrgUsu" value="${codOrgUsu}" maxlength="6" onKeypress="return apenasNumeros(event);" class="form-control"/>
						    	<input type="hidden" id="id" name="id" value="${id}"/>
						        <input type="hidden" name="acao" value="${empty id ? 'i' : 'a'}"/>
							</div>
						</div>
						<div class="col-md-2">
							<div class="form-group">
								<label>Acrônimo</label>
								<input type="text" name="siglaOrgaoUsuario" id="siglaOrgaoUsuario" value="${siglaOrgaoUsuario}" minlength="3" maxlength="3" style="text-transform: uppercase" onKeypress="return apenasLetras(event);" onkeyup="this.value = this.value.trim()" class="form-control" ${empty siglaOrgaoUsuario || podeAlterarSigla ? '' : 'title="Não é possível alterar o Acrônimo de um Órgão" readonly'} />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="col-md-2">
							<div class="form-group">
								<label>Sigla Oficial</label>
								<input type="text" id="siglaOrgaoUsuarioCompleta" name="siglaOrgaoUsuarioCompleta" value="${siglaOrgaoUsuarioCompleta}" maxlength="10" class="form-control" />
							</div>
						</div>
						<div class="col-md-6">
							<div class="form-group">
								<label>Nome</label>
								<input type="text" id="nmOrgaoUsuario" name="nmOrgaoUsuario" value="${nmOrgaoUsuario}" maxlength="256" class="form-control"/>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="col-sm-6">
							<div class="form-group">
								<input type="button" value="Salvar" onclick="javascript: validar();" class="btn btn-primary" />
								<input type="button" value="Cancelar" onclick="javascript:history.back();" class="btn btn-secondary" />
							</div>
						</div>
					</div>
				</div>
			</div>
			<br />
		</form>		
	</div>
</div>

</body>

</siga:pagina>