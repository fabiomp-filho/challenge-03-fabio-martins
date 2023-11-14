
# MS User API

Este projeto é uma API Spring Boot para gerenciamento de usuários. Ele utiliza Spring Security para autenticação e autorização, e oferece documentação interativa da API com Swagger (Springdoc-OpenAPI).

## Características

- CRUD de usuários.
- Autenticação e autorização com Spring Security.
- Documentação da API com Swagger (Springdoc-OpenAPI).

## Tecnologias Utilizadas

- Spring Boot
- Spring Security
- Spring Data JPA
- Springdoc-OpenAPI (Swagger)
- Banco de dados H2 (em memória) para testes

## Pré-requisitos

- Java 11 ou superior
- Maven (para construção e gestão do projeto)

## Como Executar

Para executar a aplicação, siga os seguintes passos:

1. Clone o repositório:

   ```bash
   git clone [URL do Repositório]
   ```

2. Navegue até a pasta do projeto:

   ```bash
   cd ms-user
   ```

3. Execute a aplicação utilizando o Maven:

   ```bash
   mvn spring-boot:run
   ```

4. A aplicação estará disponível em `http://localhost:8080`.

## Acessando a Documentação da API

Após iniciar a aplicação, você pode acessar a documentação da API Swagger através do seguinte URL:

```
http://localhost:8080/swagger-ui/index.html
```

## Endpoints da API

A API oferece os seguintes endpoints:

- `POST /v1/login`: Endpoint para autenticação de usuários.
- `GET /v1/users/{id}`: Endpoint para obter detalhes de um usuário específico.
- `POST /v1/users`: Endpoint para criação de um novo usuário.
- `PUT /v1/users/{id}`: Endpoint para atualização de um usuário existente.
- `PUT /v1/users/{id}/password`: Endpoint para atualização da senha de um usuário.

## Segurança

A API utiliza Spring Security para autenticação e autorização de usuários. As credenciais são passadas via HTTP Basic Auth.

## Contribuições

Contribuições são sempre bem-vindas. Sinta-se à vontade para clonar, fazer fork ou enviar pull requests.

## Licença

[Inserir informações da licença aqui]

## Contato

[Seu Nome] - [Seu E-mail]
