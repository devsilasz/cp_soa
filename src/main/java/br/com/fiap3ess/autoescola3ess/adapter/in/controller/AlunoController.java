package br.com.fiap3ess.autoescola3ess.adapter.in.controller;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.aluno.DadosAtualizacaoAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3ess.autoescola3ess.application.port.in.StdFeaturesPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController implements StdFeaturesPort<
        DadosCadastroAluno,
        DadosListagemAluno,
        DadosAtualizacaoAluno,
        Void,
        DadosDetalhamentoAluno,
        Long> {
    @Override
    public ResponseEntity<DadosDetalhamentoAluno> cadastrar(DadosCadastroAluno dados, UriComponentsBuilder uriBuilder) {
        return null;
    }

    @Override
    public ResponseEntity<Page<DadosListagemAluno>> listar(Pageable paginacao) {
        return null;
    }

    @Override
    public ResponseEntity<DadosDetalhamentoAluno> detalhar(Long aLong) {
        return null;
    }

    @Override
    public ResponseEntity<DadosDetalhamentoAluno> atualizar(DadosAtualizacaoAluno dados) {
        return null;
    }

    @Override
    public ResponseEntity<Void> excluir(Long aLong) {
        return null;
    }
}
