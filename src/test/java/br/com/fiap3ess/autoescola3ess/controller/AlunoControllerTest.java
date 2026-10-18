package br.com.fiap3ess.autoescola3ess.controller;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3ess.autoescola3ess.application.service.AlunoService;
import br.com.fiap3ess.autoescola3ess.shared.vo.endereco.dto.DadosEndereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AlunoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AlunoService service;

    @Test
    @DisplayName("Expectativa: Retornar código 400 para dados inválidos.")
    @WithMockUser(roles = "ADMIN")
    void cadastrarAlunoCenario1() throws Exception {
        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Expectativa: Retornar código 201 para dados válidos.")
    @WithMockUser(roles = "ADMIN")
    void cadastrarAlunoCenario2() throws Exception {
        //arrange
        DadosEndereco endereco = new DadosEndereco(
                "Rua Teste", "000", null, "Vila Teste", "Test City", "SP", "01234-567");
        when(service.cadastrarAluno(any(DadosCadastroAluno.class))).thenReturn(new DadosDetalhamentoAluno(
                1L, "Aluno Teste", "alunoteste@email.com.br", "(11) 98765-4321", "12345678901", endereco, true));

        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nome": "Aluno Teste",
                                    "email": "alunoteste@email.com.br",
                                    "telefone": "(11) 98765-4321",
                                    "cpf": "12345678901",
                                    "endereco": {
                                        "logradouro": "Rua Teste",
                                        "numero": "000",
                                        "bairro": "Vila Teste",
                                        "cidade": "Test City",
                                        "uf": "SP",
                                        "cep": "01234-567"
                                    }
                                }
                                """))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/alunos/1");
    }
}
