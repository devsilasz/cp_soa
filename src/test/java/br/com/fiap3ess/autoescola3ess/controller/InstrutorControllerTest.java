package br.com.fiap3ess.autoescola3ess.controller;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.fiap3ess.autoescola3ess.application.service.InstrutorService;
import br.com.fiap3ess.autoescola3ess.shared.vo.endereco.dto.DadosEndereco;
import br.com.fiap3ess.autoescola3ess.shared.vo.enumeration.Especialidade;
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
public class InstrutorControllerTest {
    private static final String JSON_INSTRUTOR = """
            {
                "nome": "Instrutor Teste",
                "email": "instrutorteste@email.com.br",
                "telefone": "(11) 91234-5678",
                "cnh": "01234567890",
                "especialidade": "MOTOS",
                "endereco": {
                    "logradouro": "Rua Teste",
                    "numero": "000",
                    "bairro": "Vila Teste",
                    "cidade": "Test City",
                    "uf": "SP",
                    "cep": "01234-567"
                }
            }
            """;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    InstrutorService service;

    @Test
    @DisplayName("Expectativa: Retornar código 201 para dados válidos.")
    @WithMockUser(roles = "ADMIN")
    void cadastrarInstrutorCenario1() throws Exception {
        //arrange
        DadosEndereco endereco = new DadosEndereco(
                "Rua Teste", "000", null, "Vila Teste", "Test City", "SP", "01234-567");
        when(service.cadastrarInstrutor(any(DadosCadastroInstrutor.class))).thenReturn(new DadosDetalhamentoInstrutor(
                1L, "Instrutor Teste", "instrutorteste@email.com.br", "(11) 91234-5678",
                "01234567890", Especialidade.MOTOS, endereco, true));

        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_INSTRUTOR))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).contains("Instrutor Teste");
    }

    @Test
    @DisplayName("Expectativa: Retornar código 403 quando o usuário não for ADMIN.")
    @WithMockUser(roles = "USER")
    void cadastrarInstrutorCenario2() throws Exception {
        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_INSTRUTOR))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
