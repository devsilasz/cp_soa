# Auto Escola 3ESS – API REST

API REST em Spring Boot para agendamento de instruções de uma auto-escola.
Checkpoint 5 – SOA e WebServices (Prof. Carlos Eduardo Machado de Oliveira).

## Integrantes

| Nome | RM |
|------|----|
| Guilherme Oliveira Da Silva | 558797 |
| Rafael Panhoca | 555014 |
| Matheus Dantas | 558804 |
| Victor Rodriguez | 559094 |
| Silas Alves | 555020 |

## Tecnologias

- Java 25 / Spring Boot 4
- Spring Data JPA + Flyway + MySQL
- Spring Security + JWT
- springdoc-openapi (Swagger)
- JUnit 5, MockMvc e Mockito

## Funcionalidades

- **CRUD** de Instrutores (`/instrutores`) e Alunos (`/alunos`), com exclusão lógica
- **Login** com geração de token JWT (`/login`)
- **Agendamento** (`POST /instrucoes`) e **cancelamento** (`DELETE /instrucoes`) de instruções, com as regras de negócio:
  - funcionamento de segunda a sábado, das 06:00 às 21:00, em horas inteiras;
  - antecedência mínima de 30 minutos para agendar;
  - aluno e instrutor precisam estar ativos;
  - instrutor não pode ter duas instruções no mesmo horário;
  - aluno pode ter no máximo uma instrução por dia;
  - se o instrutor não for informado, um instrutor disponível da especialidade é escolhido;
  - cancelamento exige motivo (`ALUNO_DESISTIU`, `INSTRUTOR_CANCELOU`, `OUTROS`) e antecedência mínima de 24 horas.

## Itens do Checkpoint 5

### Consumo de API externa
Integração com a API pública [ViaCEP](https://viacep.com.br) usando `RestClient`
(`adapter/out/client/ViaCepClient.java`). O endpoint `GET /enderecos/{cep}` consulta o
endereço pelo CEP e o devolve no mesmo formato usado no cadastro de alunos e instrutores.

### Documentação com Swagger
Configurada em `config/documentation/SwaggerConfig.java`, com autenticação Bearer (JWT).
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI (JSON/YAML): http://localhost:8080/v3/api-docs e http://localhost:8080/v3/api-docs.yaml
- Cópia offline: `SwaggerOffLine.yaml`

### CORS
Configurado em `config/security/SecurityConfig.java`, liberando origens específicas
(ex.: `http://localhost:3000`, `http://localhost:5500`) e os métodos GET, POST, PUT, DELETE, OPTIONS e HEAD.

### Testes automatizados
Em `src/test/java`:

| Entidade | Classe de teste |
|----------|-----------------|
| Aluno | `controller/AlunoControllerTest` |
| Instrutor | `controller/InstrutorControllerTest`, `domain/instrutor/InstrutorRepositoryTest` |
| Instrução | `controller/InstrucaoControllerTest` |
| Usuário | `domain/usuario/UsuarioRepositoryTest` |

## Como executar

Pré-requisitos: Java 25 e MySQL rodando em `localhost:3306` (usuário `root`, senha `fiap`).

```sql
create database autoescola3ess;
create database autoescola3ess_test; -- usado pelos testes
```

```bash
./mvnw spring-boot:run   # sobe a API em http://localhost:8080
./mvnw test              # executa os testes
```

As tabelas são criadas automaticamente pelo Flyway. Para acessar os endpoints, cadastre um usuário
na tabela `usuarios` com a senha criptografada em BCrypt (veja `shared/util/GerarSenhaHash.java`),
faça `POST /login` e envie o token no header `Authorization: Bearer <token>`.
