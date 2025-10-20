# Fyora Community API

API REST para funcionalidade de comunidade da plataforma Fyora, desenvolvida com Spring Boot seguindo princípios de arquitetura orientada a serviços (SOA) e boas práticas de desenvolvimento.

## 📋 Descrição do Projeto

A Fyora Community API é uma aplicação backend que fornece funcionalidades de comunidade online, permitindo que usuários:

- **Autentiquem-se de forma segura** com JWT (JSON Web Token)
- **Criem posts** com conteúdo e tags categorizadas
- **Apoiem posts** de outros usuários
- **Comentem** em posts
- **Mantenham anonimato** através de nomes comunitários gerados automaticamente
- **Interajam** em um ambiente seguro e validado

A API foi projetada com foco em segurança, escalabilidade e manutenibilidade, implementando autenticação stateless, validação robusta de dados e tratamento centralizado de exceções.

## 🚀 Tecnologias Utilizadas

### Framework e Linguagem
- **Java 17** - Linguagem de programação
- **Spring Boot 3.5.6** - Framework principal
- **Spring Data JPA** - Persistência e ORM
- **Spring Security** - Autenticação e autorização
- **Spring Validation** - Validação de dados

### Banco de Dados
- **PostgreSQL 16.2** - Banco de dados relacional
- **Flyway** - Versionamento e migração de banco de dados

### Segurança
- **JWT (JSON Web Token)** - Autenticação stateless
- **BCrypt** - Criptografia de senhas
- **Auth0 Java JWT 4.5.0** - Biblioteca para manipulação de tokens

### Documentação
- **SpringDoc OpenAPI 2.8.13** - Documentação automática da API
- **Swagger UI** - Interface interativa para testes

### Ferramentas de Desenvolvimento
- **Lombok** - Redução de boilerplate code
- **Maven** - Gerenciamento de dependências
- **Docker** - Containerização do banco de dados

### Testes
- **JUnit 5** - Framework de testes unitários
- **Mockito** - Framework para mocks e testes unitários
- **Spring Test** - Testes de integração
- **MockMvc** - Testes de controllers

## 📦 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- **Java 17** ou superior ([Download](https://www.oracle.com/java/technologies/downloads/))
- **Docker** e **Docker Compose** ([Download](https://www.docker.com/products/docker-desktop/))
- **Maven** (opcional, o projeto inclui Maven Wrapper)
- **Git** ([Download](https://git-scm.com/downloads))

## 🔧 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/tavares-fiap/fyora-community-api
cd fyora-community-api
```

### 2. Configure o banco de dados

O projeto utiliza Docker Compose para facilitar a configuração do PostgreSQL:

```bash
docker-compose up -d
```

Isso iniciará um container PostgreSQL com as seguintes configurações:
- **Host**: localhost
- **Porta**: 5432
- **Database**: fyora_community_db
- **Usuário**: fyora_user
- **Senha**: fyora_password

### 3. Configure as variáveis de ambiente (opcional)

As configurações padrão estão em `src/main/resources/application.properties`. Para customizar, você pode:

**Opção A**: Modificar o arquivo `application.properties`

**Opção B**: Definir variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/fyora_community_db
export SPRING_DATASOURCE_USERNAME=fyora_user
export SPRING_DATASOURCE_PASSWORD=fyora_password
export JWT_SECRET=sua_chave_secreta_aqui
export JWT_EXPIRATION_SECONDS=3600
```

### 4. Execute as migrações do banco de dados

As migrações Flyway são executadas automaticamente ao iniciar a aplicação. Os scripts estão em `src/main/resources/db/migration/`.

## ▶️ Executando a Aplicação

### Pela própria IDE

Basta rodar o arquivo [CommunityApiApplication.java](src/main/java/com/fyora/community/CommunityApiApplication.java)

### Usando Maven Wrapper (recomendado)

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

### Usando Maven instalado

```bash
mvn spring-boot:run
```

### Compilando e executando o JAR

```bash
mvn clean package
java -jar target/community-api-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: **http://localhost:8080**

## 📚 Documentação da API

### Swagger UI (Interativo)

Acesse a documentação interativa da API através do Swagger UI:

**URL**: http://localhost:8080/swagger-ui.html

A interface Swagger permite:
- Visualizar todos os endpoints disponíveis
- Testar requisições diretamente no navegador
- Ver exemplos de request/response
- Autenticar com JWT para testar endpoints protegidos

### OpenAPI JSON

Especificação OpenAPI em formato JSON:

**URL**: http://localhost:8080/v3/api-docs

## 🧪 Executando os Testes

O projeto possui testes unitários e de integração abrangentes.

### Executar todos os testes

**Pela IDE:**

Basta executar [CommunityApiApplicationTests](src/test/java/com/fyora/community/CommunityApiApplicationTests.java)

**Windows:**
```bash
.\mvnw.cmd test
```

**Linux/Mac:**
```bash
./mvnw test
```

### Executar testes de uma classe específica

```bash
mvn test -Dtest=AuthServiceTest
```

### Executar com cobertura de código

```bash
mvn clean test jacoco:report
```

### Estrutura dos Testes

- **Testes Unitários**: Testam classes individuais com mocks
  - `AuthServiceTest` - Testes do serviço de autenticação
  - `PostServiceTest` - Testes do serviço de posts
  - `CommunityUserServiceTest` - Testes do serviço de usuários

- **Testes de Integração**: Testam endpoints completos
  - `PostSecurityTest` - Testes de segurança e autorização
  - `CommunityApiApplicationTests` - Teste de inicialização do contexto

## 🏗️ Estrutura do Projeto

```
community-api/
├── src/
│   ├── main/
│   │   ├── java/com/fyora/community/
│   │   │   ├── auth/                    # Módulo de autenticação
│   │   │   │   ├── exception/           # Exceções customizadas de auth
│   │   │   │   ├── dto/                 # DTOs de auth
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── MeController.java
│   │   │   │   ├── UserAccount.java
│   │   │   │   └── UserAccountRepository.java
│   │   │   ├── comment/                 # Módulo de comentários
│   │   │   │   ├── dto/
│   │   │   │   ├── Comment.java
│   │   │   │   └── CommentRepository.java
│   │   │   ├── common/                  # Componentes compartilhados
│   │   │   │   ├── exception/           # Tratamento global de exceções
│   │   │   │   └── NomeComunitarioGenerator.java
│   │   │   ├── communityuser/           # Módulo de usuários da comunidade
│   │   │   │   ├── dto/
│   │   │   │   ├── CommunityUser.java
│   │   │   │   ├── CommunityUserController.java
│   │   │   │   ├── CommunityUserService.java
│   │   │   │   └── CommunityUserRepository.java
│   │   │   ├── infra/                   # Infraestrutura
│   │   │   │   ├── config/              # Configurações (OpenAPI)
│   │   │   │   └── security/            # Segurança (JWT, Filters)
│   │   │   ├── post/                    # Módulo de posts
│   │   │   │   ├── dto/
│   │   │   │   ├── Post.java
│   │   │   │   ├── PostController.java
│   │   │   │   ├── PostService.java
│   │   │   │   ├── PostServiceInterface.java
│   │   │   │   ├── PostRepository.java
│   │   │   │   ├── Support.java
│   │   │   │   └── SupportRepository.java
│   │   │   ├── tag/                     # Módulo de tags
│   │   │   │   ├── Tag.java
│   │   │   │   ├── TagType.java
│   │   │   │   └── TagRepository.java
│   │   │   └── CommunityApiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/migration/            # Scripts Flyway
│   └── test/
│       └── java/com/fyora/community/
│           ├── auth/
│           │   └── AuthServiceTest.java
│           ├── communityuser/
│           │   └── CommunityUserServiceTest.java
│           ├── post/
│           │   ├── PostSecurityTest.java
│           │   └── PostServiceTest.java
│           └── CommunityApiApplicationTests.java
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🔐 Autenticação e Autorização

A API utiliza autenticação baseada em JWT (JSON Web Token) com o padrão Bearer.

### Registro de Usuário

```bash
POST /api/auth/register
Content-Type: application/json

{
  "username": "usuario",
  "password": "senha123"
}
```

**Resposta**: `204 No Content`

### Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "username": "usuario",
  "password": "senha123"
}
```

**Resposta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Usando o Token

Para acessar endpoints protegidos, inclua o token no header:

```bash
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

Caso esteja acessando pela UI do Swagger, basta acessar "authorize" no canto superior esquerdo e inserir seu token


### Informações do Usuário Autenticado

```bash
GET /api/auth/me
Authorization: Bearer {token}
```

**Resposta**:
```json
{
  "username": "usuario",
  "role": "USER",
  "communityUserId": 1,
  "communityName": "Fênix Valente #1234"
}
```

## 📝 Exemplos de Uso

### 1. Criar um Post

```bash
POST /api/community/posts
Authorization: Bearer {token}
Content-Type: application/json

{
  "content": "Meu primeiro post na comunidade!",
  "tags": ["MOTIVACAO", "REFLEXAO"]
}
```

**Resposta**:
```json
{
  "id": 1,
  "content": "Meu primeiro post na comunidade!",
  "createdAt": "2025-10-18T14:30:00",
  "authorName": "Fênix Valente #1234",
  "tags": ["MOTIVACAO", "REFLEXAO"],
  "supportsCount": 0
}
```

### 2. Listar Feed de Posts (público)

```bash
GET /api/community/posts?page=0&size=10&sort=createdAt,desc
```

**Resposta**:
```json
{
  "content": [
    {
      "id": 1,
      "content": "Meu primeiro post na comunidade!",
      "createdAt": "2025-10-18T14:30:00",
      "authorName": "Fênix Valente #1234",
      "tags": ["MOTIVACAO"],
      "supportsCount": 0
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

### 3. Apoiar um Post

```bash
POST /api/community/posts/1/support
Authorization: Bearer {token}
```

**Resposta**: `200 OK`

### 4. Comentar em um Post

```bash
POST /api/community/posts/1/comments
Authorization: Bearer {token}
Content-Type: application/json

{
  "content": "Ótimo post! Concordo totalmente."
}
```

**Resposta**:
```json
{
  "id": 1,
  "content": "Ótimo post! Concordo totalmente.",
  "createdAt": "2025-10-18T14:35:00",
  "authorName": "Aurora Serena #5678"
}
```

### 5. Ver Comentários de um Post (público)

```bash
GET /api/community/posts/1/comments?page=0&size=10
```

## 🌐 Endpoints Disponíveis

### Autenticação (Público)
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Fazer login
- `GET /api/auth/me` - Informações do usuário autenticado (requer token)

### Posts
- `POST /api/community/posts` - Criar post (requer token)
- `GET /api/community/posts` - Listar posts (público)
- `POST /api/community/posts/{id}/support` - Apoiar post (requer token)
- `DELETE /api/community/posts/{id}/support` - Remover apoio (requer token)
- `POST /api/community/posts/{id}/comments` - Comentar (requer token)
- `GET /api/community/posts/{id}/comments` - Ver comentários (público)

## 🐛 Tratamento de Erros

A API retorna respostas de erro padronizadas:

```json
{
  "timestamp": "2025-10-18T14:30:00Z",
  "status": 400,
  "error": "ValidationError",
  "details": {
    "content": "não deve estar em branco"
  }
}
```

### Códigos de Status HTTP

- `200 OK` - Requisição bem-sucedida
- `201 Created` - Recurso criado com sucesso
- `204 No Content` - Operação bem-sucedida sem retorno
- `400 Bad Request` - Dados de entrada inválidos
- `401 Unauthorized` - Token ausente ou inválido
- `404 Not Found` - Recurso não encontrado
- `409 Conflict` - Conflito (ex: username já existe)
- `422 Unprocessable Entity` - Regra de negócio violada
- `500 Internal Server Error` - Erro interno do servidor (normalmente fata de autorizacao(Token ausente ou inválido))

## 📊 Migrações do Banco de Dados

As migrações Flyway estão organizadas em:

1. **V1__create-base-tables.sql** - Cria tabelas principais (posts, comments, users, tags)
2. **V2__seed-tags.sql** - Insere tags padrão do sistema
3. **V3__auth_accounts.sql** - Cria tabela de contas de usuário
4. **V4__link-useraccount-communityuser.sql** - Relaciona contas com usuários da comunidade

## 👥 Autores

Guilherme Rocha Bianchini – RM97974

Nikolas Rodrigues Moura dos Santos – RM551566

Pedro Henrique Pedrosa Tavares – RM97877

Rodrigo Brasileiro – RM98952

Thiago Jardim de Oliveira – RM551624

```markdown
## 🏗️ Diagramas de Arquitetura

### Arquitetura Geral
```plantuml
@startuml
!theme plain
title Fyora Community API - Arquitetura Geral

package "Cliente" {
  [Frontend/Postman] as Client
}

package "API Gateway/Load Balancer" {
  [Nginx/API Gateway] as Gateway
}

package "Aplicação Spring Boot" {
  package "Controllers" {
    [AuthController] as AuthCtrl
    [PostController] as PostCtrl
    [MeController] as MeCtrl
  }
  
  package "Services" {
    [AuthService] as AuthSvc
    [PostService] as PostSvc
    [CommunityUserService] as UserSvc
  }
  
  package "Security" {
    [JwtTokenProvider] as JWT
    [JwtAuthenticationFilter] as Filter
    [SecurityConfig] as SecConfig
  }
  
  package "Repositories" {
    [UserAccountRepository] as UserRepo
    [PostRepository] as PostRepo
    [CommentRepository] as CommentRepo
  }
}

package "Banco de Dados" {
  database "PostgreSQL" as DB {
    [user_accounts]
    [community_users]
    [posts]
    [comments]
    [tags]
    [supports]
  }
}

package "Documentação" {
  [Swagger UI] as Swagger
  [OpenAPI Spec] as OpenAPI
}

Client --> Gateway
Gateway --> AuthCtrl
Gateway --> PostCtrl
Gateway --> MeCtrl

AuthCtrl --> AuthSvc
PostCtrl --> PostSvc
MeCtrl --> AuthSvc

AuthSvc --> UserRepo
PostSvc --> PostRepo
PostSvc --> CommentRepo
UserSvc --> UserRepo

Filter --> JWT
SecConfig --> Filter

UserRepo --> DB
PostRepo --> DB
CommentRepo --> DB

Swagger --> OpenAPI
OpenAPI --> PostCtrl
OpenAPI --> AuthCtrl

@enduml
```

### Fluxo de Autenticação
```plantuml
@startuml
!theme plain
title Fluxo de Autenticação JWT

actor "Cliente" as Client
participant "AuthController" as Auth
participant "AuthService" as Service
participant "UserAccountRepository" as Repo
participant "JwtTokenProvider" as JWT
participant "BCryptPasswordEncoder" as BCrypt
database "PostgreSQL" as DB

== Registro de Usuário ==
Client -> Auth: POST /api/auth/register
Auth -> Service: register(RegisterRequest)
Service -> Repo: existsByUsername()
Repo -> DB: SELECT username
DB --> Repo: resultado
Repo --> Service: boolean
alt Username já existe
  Service --> Auth: UsernameAlreadyExistsException
  Auth --> Client: 409 Conflict
else Username disponível
  Service -> BCrypt: encode(password)
  BCrypt --> Service: passwordHash
  Service -> Repo: save(UserAccount)
  Repo -> DB: INSERT user_accounts
  DB --> Repo: UserAccount salvo
  Repo --> Service: UserAccount
  Service --> Auth: void
  Auth --> Client: 204 No Content
end

== Login ==
Client -> Auth: POST /api/auth/login
Auth -> Service: login(LoginRequest)
Service -> Repo: findByUsername()
Repo -> DB: SELECT * FROM user_accounts
DB --> Repo: UserAccount
Repo --> Service: UserAccount
Service -> BCrypt: matches(password, hash)
BCrypt --> Service: boolean
alt Credenciais inválidas
  Service --> Auth: InvalidCredentialsException
  Auth --> Client: 401 Unauthorized
else Credenciais válidas
  Service -> JWT: generate(username, role)
  JWT --> Service: JWT Token
  Service --> Auth: JWT Token
  Auth --> Client: 200 OK + Token
end

== Acesso a Endpoint Protegido ==
Client -> Auth: GET /api/community/posts (Authorization: Bearer token)
Auth -> JWT: validate(token)
JWT --> Auth: DecodedJWT
Auth -> Auth: setAuthentication()
Auth --> Client: 200 OK + Dados

@enduml
```

### Modelo de Dados
```plantuml
@startuml
!theme plain
title Modelo de Dados - Entidades e Relacionamentos

entity "UserAccount" {
  * id : BIGINT <<PK>>
  --
  * username : VARCHAR(255) <<UK>>
  * password_hash : VARCHAR(255)
  * role : VARCHAR(50)
  * created_at : TIMESTAMP
}

entity "CommunityUser" {
  * id : BIGINT <<PK>>
  --
  * community_name : VARCHAR(255) <<UK>>
  * created_at : TIMESTAMP
  * user_account_id : BIGINT <<FK>>
}

entity "Post" {
  * id : BIGINT <<PK>>
  --
  * content : VARCHAR(1000)
  * created_at : TIMESTAMP
  * supports_count : INTEGER
  * community_user_id : BIGINT <<FK>>
}

entity "Comment" {
  * id : BIGINT <<PK>>
  --
  * content : VARCHAR(500)
  * created_at : TIMESTAMP
  * post_id : BIGINT <<FK>>
  * community_user_id : BIGINT <<FK>>
}

entity "Tag" {
  * id : BIGINT <<PK>>
  --
  * type : VARCHAR(255) <<UK>>
}

entity "Support" {
  * id : BIGINT <<PK>>
  --
  * created_at : TIMESTAMP
  * post_id : BIGINT <<FK>>
  * community_user_id : BIGINT <<FK>>
}

entity "PostTags" {
  * post_id : BIGINT <<PK>>
  * tag_id : BIGINT <<PK>>
}

UserAccount ||--o| CommunityUser : "tem"
CommunityUser ||--o{ Post : "cria"
CommunityUser ||--o{ Comment : "comenta"
CommunityUser ||--o{ Support : "apoia"
Post ||--o{ Comment : "possui"
Post ||--o{ Support : "recebe"
Post ||--o{ PostTags : "tem"
Tag ||--o{ PostTags : "aplicada em"

@enduml
```

### Fluxo de Criação de Post
```plantuml
@startuml
!theme plain
title Fluxo de Criação de Post

actor "Cliente" as Client
participant "PostController" as Controller
participant "PostService" as Service
participant "CurrentUserService" as CurrentUser
participant "CommunityUserService" as UserService
participant "TagRepository" as TagRepo
participant "PostRepository" as PostRepo
database "PostgreSQL" as DB

Client -> Controller: POST /api/community/posts
note right: Authorization: Bearer token

Controller -> Service: criar(DadosCadastroPost)

== Validação de Conteúdo ==
Service -> Service: validatePostContent()
note right: Verifica se conteúdo tem 3-1000 caracteres

== Obter Usuário Atual ==
Service -> CurrentUser: requireUserAccount()
CurrentUser --> Service: UserAccount

Service -> UserService: getOrCreateByAccount()
UserService -> DB: SELECT community_user WHERE user_account_id
alt Usuário não existe
  UserService -> UserService: gerarNomeComunitario()
  UserService -> DB: INSERT community_users
  DB --> UserService: CommunityUser criado
else Usuário existe
  DB --> UserService: CommunityUser existente
end
UserService --> Service: CommunityUser

== Validação e Associação de Tags ==
alt Tags fornecidas
  loop Para cada tag
    Service -> Service: TagType.valueOf(tag.toUpperCase())
    Service -> TagRepo: findByType(tagType)
    TagRepo -> DB: SELECT * FROM tags WHERE type
    DB --> TagRepo: Tag
    TagRepo --> Service: Tag
  end
  Service -> Service: validateAndSetTags()
  note right: Verifica limite de 5 tags
end

== Criação do Post ==
Service -> Service: new Post()
Service -> Service: setContent()
Service -> Service: setCommunityUser()
Service -> Service: addTag() para cada tag

Service -> PostRepo: save(Post)
PostRepo -> DB: INSERT posts
DB --> PostRepo: Post salvo
PostRepo --> Service: Post

== Retorno ==
Service -> Service: new DadosDetalhePost()
Service --> Controller: DadosDetalhePost
Controller --> Client: 201 Created + Dados

@enduml
```