package br.com.fiap3ess.autoescola3ess.controller;

import br.com.fiap3ess.autoescola3ess.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.fiap3ess.autoescola3ess.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3ess.autoescola3ess.application.service.AgendaDeInstrucoes;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class InstrucaoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AgendaDeInstrucoes agenda;

    @Test
    @DisplayName("Expectativa: Retornar código 400 para dados inválidos.")
    @WithMockUser
    void agendarInstrucaoCenario1() throws Exception {
        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrucoes"))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Expectativa: Retornar código 200 para dados válidos.")
    @WithMockUser
    void agendarInstrucaoCenario2() throws Exception {
        //arrange
        LocalDateTime dataHora = LocalDateTime.now().plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
        when(agenda.agendar(any(DadosAgendamento.class))).thenReturn(new DadosDetalhamentoAgendamento(
                1L, "Aluno Teste", "Instrutor Teste", Especialidade.MOTOS, dataHora));

        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrucoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id_aluno": 1,
                                    "especialidade": "MOTOS",
                                    "data_hora": "%s"
                                }
                                """.formatted(dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm")))))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("Instrutor Teste");
    }

    @Test
    @DisplayName("Expectativa: Retornar código 204 ao cancelar instrução com dados válidos.")
    @WithMockUser
    void cancelarInstrucaoCenario1() throws Exception {
        //act
        MockHttpServletResponse response = mockMvc
                .perform(delete("/instrucoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id_instrucao": 1,
                                    "motivo": "ALUNO_DESISTIU"
                                }
                                """))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
