package br.com.fiap3ess.autoescola3ess.application.service;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.aluno.DadosAtualizacaoAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3ess.autoescola3ess.application.core.domain.Aluno;
import br.com.fiap3ess.autoescola3ess.application.port.out.AlunoRepository;
import br.com.fiap3ess.autoescola3ess.exception.type.AlunoNotFoundException;
import br.com.fiap3ess.autoescola3ess.shared.vo.endereco.mapper.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;
    private final EnderecoMapper enderecoMapper;

    @Transactional
    public DadosDetalhamentoAluno cadastrarAluno(DadosCadastroAluno dados) {
        Aluno aluno = new Aluno(
                null,
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.cpf(),
                enderecoMapper.toVo(dados.endereco()),
                true
        );
        Aluno salvo = repository.save(aluno);
        return toDetails(salvo);
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemAluno> listarAlunos(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(aluno -> new DadosListagemAluno(
                        aluno.getId(),
                        aluno.getNome(),
                        aluno.getEmail(),
                        aluno.getCpf()
                ));
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoAluno detalharAluno(Long id) {
        return toDetails(buscarAluno(id));
    }

    @Transactional
    public DadosDetalhamentoAluno atualizarAluno(DadosAtualizacaoAluno dados) {
        Aluno aluno = buscarAluno(dados.id());
        aluno.atualizarInformacoes(
                dados.nome(),
                dados.email(),
                dados.telefone(),
                enderecoMapper.toVo(dados.endereco())
        );
        return toDetails(aluno);
    }

    @Transactional
    public void excluirAluno(Long id) {
        buscarAluno(id).excluir();
    }

    private Aluno buscarAluno(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() ->
                        new AlunoNotFoundException("ID do aluno informado não existe!"));
    }

    private DadosDetalhamentoAluno toDetails(Aluno aluno) {
        return new DadosDetalhamentoAluno(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getCpf(),
                enderecoMapper.toDto(aluno.getEndereco()),
                aluno.isAtivo()
        );
    }
}
