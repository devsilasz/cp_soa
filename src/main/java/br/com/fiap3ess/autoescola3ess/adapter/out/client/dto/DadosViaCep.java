package br.com.fiap3ess.autoescola3ess.adapter.out.client.dto;

public record DadosViaCep(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String erro) {
}
