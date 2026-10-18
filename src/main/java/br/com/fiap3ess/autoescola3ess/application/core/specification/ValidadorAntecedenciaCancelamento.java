package br.com.fiap3ess.autoescola3ess.application.core.specification;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.instrucao.DadosCancelamento;
import br.com.fiap3ess.autoescola3ess.application.core.domain.Instrucao;
import br.com.fiap3ess.autoescola3ess.application.core.specification.contract.ValidadorCancelamento;
import br.com.fiap3ess.autoescola3ess.application.port.out.InstrucaoRepository;
import br.com.fiap3ess.autoescola3ess.exception.type.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaCancelamento implements ValidadorCancelamento {
    @Autowired
    private InstrucaoRepository repository;

    @Override
    public void validar(DadosCancelamento dados) {
        Instrucao instrucao = repository.getReferenceById(dados.idInstrucao());
        long antecedencia = Duration.between(LocalDateTime.now(), instrucao.getDataHora()).toHours();

        if (antecedencia < 24) {
            throw new ValidacaoException("Instrução somente pode ser cancelada com antecedência mínima de 24 horas!");
        }
    }
}
