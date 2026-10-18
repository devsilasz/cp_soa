package br.com.fiap3ess.autoescola3ess.adapter.out.client;

import br.com.fiap3ess.autoescola3ess.adapter.out.client.dto.DadosViaCep;
import br.com.fiap3ess.autoescola3ess.exception.type.ValidacaoException;
import br.com.fiap3ess.autoescola3ess.shared.vo.endereco.dto.DadosEndereco;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

//Consumo da API externa ViaCEP (https://viacep.com.br) para consulta de endereço pelo CEP
@Component
public class ViaCepClient {
    private final RestClient restClient = RestClient.create("https://viacep.com.br/ws");

    public DadosEndereco buscarEndereco(String cep) {
        String cepNumerico = cep.replaceAll("\\D", "");
        if (cepNumerico.length() != 8) {
            throw new ValidacaoException("CEP inválido! Informe 8 dígitos (ex: 01310-100).");
        }

        DadosViaCep resposta = restClient
                .get()
                .uri("/{cep}/json/", cepNumerico)
                .retrieve()
                .body(DadosViaCep.class);

        if (resposta == null || "true".equals(resposta.erro())) {
            throw new ValidacaoException("CEP não encontrado!");
        }

        return new DadosEndereco(
                resposta.logradouro(),
                null,
                resposta.complemento(),
                resposta.bairro(),
                resposta.localidade(),
                resposta.uf(),
                resposta.cep()
        );
    }
}
