# API Sob Alta Demanda

## Sobre o Projeto

Este projeto foi desenvolvido para resolver o problema de alocação de recursos escassos sob alta concorrência.

O sistema gerencia um pool global de 100 vagas, permitindo que usuários autenticados disputem recursos de forma segura, consistente e justa.

A solução foi implementada utilizando:

* Java 17
* Spring Boot
* Spring Security
* JWT Authentication
* Spring Data JPA
* PostgreSQL
* Docker
* Docker Compose

---

# Regras de Negócio

## Pool Global

O sistema possui um total de:

```text
100 vagas disponíveis
```

Nenhum usuário pode ultrapassar o limite global.

Quando o pool atingir sua capacidade máxima:

```json
{
  "error": "POOL_CHEIO"
}
```

é retornado com status:

```http
409 Conflict
```

---

## Cota Pessoal

Cada usuário pode possuir no máximo:

```text
2 tickets simultaneamente em PROCESSANDO
```

Ao tentar submeter o terceiro ticket:

```json
{
  "error": "COTA_PESSOAL"
}
```

é retornado com status:

```http
409 Conflict
```

---

## Blindagem de Propriedade

Um usuário não pode acessar tickets pertencentes a outros usuários.

Caso isso ocorra:

```http
404 Not Found
```

é retornado.

---

# Máquina de Estados

```text
RASCUNHO
    |
    | SUBMETER
    v
PROCESSANDO
    |
    | FINALIZAR
    v
CONCLUIDO
```

Fluxo:

```text
RASCUNHO -> PROCESSANDO -> CONCLUIDO
```

---

# Controle de Concorrência

Para evitar race conditions durante a disputa das vagas, foi utilizado:

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

sobre a entidade responsável pelo controle do pool.

Dessa forma:

* Nunca existem mais de 100 vagas alocadas
* Não ocorre duplicação de vagas
* Não ocorre inconsistência de dados
* A concorrência é tratada pelo banco de dados

---

# Estrutura do Projeto

```text
src
 └── main
      ├── controller
      ├── services
      ├── repositories
      ├── entities
      ├── dtos
      ├── config
      ├── security
      └── enums
```

---

# Autenticação

A API utiliza JWT.

Todas as rotas de tickets exigem:

```http
Authorization: Bearer TOKEN
```

---

# Endpoints

## Registrar Usuário

```http
POST /auth/registrar
```

Body:

```json
{
  "email": "usuario@email.com",
  "password": "123456"
}
```

Resposta:

```http
201 Created
```

---

## Login

```http
POST /auth/login
```

Body:

```json
{
  "email": "usuario@email.com",
  "password": "123456"
}
```

Resposta:

```json
{
  "token": "jwt-token"
}
```

---

## Criar Ticket

```http
POST /entidades
```

Resposta:

```json
{
  "id": 1,
  "status": "RASCUNHO"
}
```

---

## Submeter Ticket

```http
POST /entidades/{id}/submeter
```

Sucesso:

```json
{
  "id": 1,
  "status": "PROCESSANDO"
}
```

Erro de Pool:

```json
{
  "error": "POOL_CHEIO"
}
```

Erro de Cota:

```json
{
  "error": "COTA_PESSOAL"
}
```

---

## Finalizar Ticket

```http
POST /entidades/{id}/finalizar
```

Resposta:

```json
{
  "id": 1,
  "status": "CONCLUIDO"
}
```

---

# Configuração Local

## Banco de Dados

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/highdemand
spring.datasource.username=postgres
spring.datasource.password=123456
```

---

# Executando com Docker

## Construir containers

```bash
docker compose up --build -d
```

## Verificar containers

```bash
docker ps
```

## Derrubar containers

```bash
docker compose down -v
```

---

# Docker Compose

Serviços:

* PostgreSQL 15
* Backend Spring Boot

Portas:

| Serviço    | Porta |
| ---------- | ----- |
| Backend    | 8080  |
| PostgreSQL | 5432  |

---

# Tecnologias Utilizadas

* Java 17
* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Docker
* Docker Compose
* Maven

---

# Autor

Projeto desenvolvido como solução para o desafio de Sistemas Sob Alta Demanda, demonstrando controle de concorrência, autenticação JWT, persistência de dados e containerização utilizando Docker.
