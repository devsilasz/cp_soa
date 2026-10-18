package br.com.fiap3ess.autoescola3ess.adapter.in.controller;

import br.com.fiap3ess.autoescola3ess.adapter.out.client.ViaCepClient;
import br.com.fiap3ess.autoescola3ess.shared.vo.endereco.dto.DadosEndereco;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class EnderecoController {
    private final ViaCepClient viaCepClient;

    @GetMapping("/{cep}")
    public ResponseEntity<DadosEndereco> buscarPorCep(@PathVariable String cep) {
        return ResponseEntity.ok(viaCepClient.buscarEndereco(cep));
    }
}
