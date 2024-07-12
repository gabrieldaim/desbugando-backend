# API Backend em Java com Spring Boot para Plataforma de Estudos

Este é um projeto de backend desenvolvido em Java utilizando Spring Boot. O projeto é uma API para uma plataforma de estudos, fornecendo funcionalidades como gerenciamento de turmas, postagens, usuários e autenticação.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Flyway
- PostgreSQL

## Configuração do Projeto

Antes de iniciar o projeto, certifique-se de ter o PostgreSQL instalado e rodando na sua máquina. Você pode alterar as configurações de conexão com o banco de dados no arquivo `application.properties` localizado em `src/main/resources`.

Fora isso, certifique-se de ter todas as dependencias do Maven baixadas.

### application.properties

```properties
spring.application.name=api
spring.datasource.url=jdbc:postgresql://localhost:5432/desbugando
spring.datasource.username=postgres
spring.datasource.password=password

api.security.token.secret=${JWT_SECRET:12345678}
api.desbugando.url.front=${URL_FRONT:http://localhost:5173}
