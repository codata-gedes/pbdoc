<%@ tag body-content="empty" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="assinante"%>
<%@ attribute name="idmov"%>
<c:url var="url" value="/app/expediente/mov/assinar_verificar">
	<c:param name="id">${idmov}</c:param>
</c:url>
<span id="verificar_assinatura_${idmov}">
	${assinante}
<!-- VERIFICAÇÃO DE ASSINATURA: DESABILITADO ATÉ QUE POSSAMOS ENTENDER COMO VALIDAR COM O BLUCSERVICE -->
<!--
	<a href="javascript: SetInnerHTMLFromAjaxResponse('${url}', 'verificar_assinatura_${idmov}');">Verificar</a>
-->
</span>
<span id="verificar_assinatura2_${idmov}"> </span>
