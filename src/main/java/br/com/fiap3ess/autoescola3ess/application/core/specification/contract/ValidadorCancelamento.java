package br.com.fiap3ess.autoescola3ess.application.core.specification.contract;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.instrucao.DadosCancelamento;

public interface ValidadorCancelamento {
    void validar(DadosCancelamento dados);
}
