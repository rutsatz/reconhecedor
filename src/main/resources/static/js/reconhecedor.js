// Função ready do jQuery
$(function() {

	// Tem regras de produção informadas.
	var temRegras;
	
	// Ao carregar a página, já adiciona uma linha.
	if(rowCount() == 0){
		temRegras = false;
		addTableRow();
	}else{
		temRegras = true;
	}
	
	// Botão (+) Adicionar Produção.
	$('#btnAddProducao').on('click', function(event) {
		event.preventDefault();
		addTableRow();
	});

	// Adiciona uma nova linha nas regras de produção.
	function addTableRow(event) {	
		console.log(temRegras);
		if(!temRegras){
			removeEmptyRow();
		}
		
		var trComponent = $('<tr>' + '<td><input type="text" class="form-control" id="regrasProducao'+rowCount()+'.LE" name="regrasProducao['+rowCount()+'].LE"/></td>'
			+ '<td><label>=></label></td>'
			+ '<td><input type="text" class="form-control" id="regrasProducao'+rowCount()+'.LD" name="regrasProducao['+rowCount()+'].LD"/></td>'
			+ '<td class="text-center">'
			+ '<a class="btn btn-link btn-xs js-remover-row" title="Excluir" rel="tooltip" data-placement="top">'
				+ '<span class="glyphicon glyphicon-remove"></span>'
			+ '</a>'
			+ '</td>'
			+ '</tr>');
		$('#tabelaRegrasProducao tbody').append(trComponent.hide().show('slow'));
		
		temRegras = true;
		
		$('.js-remover-row').click(removeTableRow);
		
		$('[rel="tooltip"]').tooltip();
	}

	// Retorna quantidade de linhas da tabela.
	function rowCount(){
		return $("#tabelaRegrasProducao tbody tr").length;
	}
	
	// Remover uma row.
	function removeTableRow(){
		$(this).parent('td').parent('tr').off('click').hide('slow', function(){
			$(this).remove();
			
			if(rowCount() == 0 && temRegras){
				$("#tabelaRegrasProducao tbody").append(
					$('<tr><td colspan="4" class="text-center">Nenhuma Regra de Produção.</td></tr>')
						.hide().show('slow'));
				temRegras = false;
			}else{
				refreshIndices();
			}			
		});
	}
	
	// Ajustar indices.
	function refreshIndices(){
		var linhas = $("#tabelaRegrasProducao tbody tr");
		$(linhas).each(function(pos, linha){
		      var leId = 'regrasProducao'+pos+'.LE';
		      var leName = 'regrasProducao['+pos+'].LE';
		      var ldId = 'regrasProducao'+pos+'.LD';
		      var ldName = 'regrasProducao['+pos+'].LD';
		      $(linha).find('td input:first').prop('id', leId).prop('name',leName);
		      $(linha).find('td input:last').prop('id', ldId).prop('name', ldName);
		});
	}
	
	function removeEmptyRow(){
		$("#tabelaRegrasProducao tbody tr").hide('slow').remove();
	}
	
	$('.js-remover-row').click(removeTableRow);
	
	$('[rel="tooltip"]').tooltip();
	
	
	
	// ### Gerar sentença ###
	
	var btnGerarSentenca = $('.js-gerar-sentenca');
	var divTextoSentenca = $('.js-texto-sentenca');
	var textSentenca;
	
	
	// Gerar a sentença via ajax.
	btnGerarSentenca.on('click', function(event){
		event.preventDefault();
		
		btnGerarSentenca.addClass('disabled').text('Aguarde...');
		
		var response = $.ajax({
			url : '/sentenca',
			type : 'PUT',
			contentType : "application/json;charset=utf-8",
			timeout : 60000
//			timeout : 0.0000001
		});
		
		response.done(function(sentenca){
//			console.log(result.texto);
//			console.log(result);
//			$.each(result, function(index, sentenca){
//				console.log(sentenca.sentenca);
//			});
			
			textSentenca = "Sentença gerada: " + sentenca.sentenca;
			
			divTextoSentenca.html(textSentenca).show('slow');	
				
			btnGerarSentenca.removeClass('disabled').text('Gerar Sentença');
			
//			console.log("request done");
		});
		
		response.fail(function(jqXHR, textStatus, errorThrown){
//			console.log(jqXHR);
			$('.js-sentenca-error').html('Ops! Ocorreu um erro ao gerar a sentença. '
					+ 'Tente revisar a gramática.').show();
			
			btnGerarSentenca.text('Não permitido').removeClass('btn-primary')
				.addClass('btn-danger');
//			console.log(textStatus);
//			console.log(errorThrown);
//			console.log("request fail");
		});
		
	});
	

	
});