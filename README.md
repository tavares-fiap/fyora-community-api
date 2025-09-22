# Fyora Community API

API REST desenvolvida em **Java Spring Boot** para a funcionalidade de **Comunidade** da plataforma Fyora.  
Permite que usuários anônimos criem posts, classifiquem com tags, apoiem (curtidas) e comentem em publicações, simulando a dinâmica de uma rede social interna e anônima.

---

## 📝 Descrição do Projeto
A Fyora Community API implementa:
- Criação de **usuários anônimos** com nome gerado automaticamente.
- **Posts** de texto, com limite de caracteres e categorização por **tags** (vitória, desabafo, gatilhos, motivação, dúvida).
- Possibilidade de **apoiar posts** (curtidas únicas por usuário).
- **Comentários** em posts, também anônimos.
- Feed de posts paginado, retornando os mais recentes primeiro.
- Tratamento centralizado de erros e validações de entrada.

---

## ⚙️ Passos de Configuração e Execução

### Pré-requisitos
- **Java 17+**
- **Maven 3.9+**
- **Docker** e **Docker Compose**

### 1) Clonar o repositório
```bash
git clone https://github.com/tavares-fiap/fyora-community-api.git
cd fyora-community-api
````

### 2) Subir banco de dados

```bash
docker compose up -d
```

### 3) Rodar a aplicação

```bash
./mvnw spring-boot:run
```

ou via IDE rodando o método main

A API ficará disponível em:
`http://localhost:8080/api/community`

### 4) Banco de Dados

* **URL**: `jdbc:postgresql://localhost:5432/fyora_community_db`
* **Usuário**: `fyora_user`
* **Senha**: `fyora_password`

O schema é gerenciado automaticamente pelo **Flyway**.

---

## 📌 Exemplos de Requisições e Respostas

### Criar Usuário Anônimo

**POST** `/api/community/users`
Resposta:

```json
{
  "id": 1,
  "communityName": "Fênix Serena #4821",
  "createdAt": "2025-09-22T00:00:00Z"
}
```

---

### Criar Post

**POST** `/api/community/posts`

```json
{
  "communityUserId": 1,
  "content": "Hoje consegui não apostar. Me sentindo bem!",
  "tags": ["VITORIA", "MOTIVACAO"]
}
```

Resposta:

```json
{
  "id": 10,
  "content": "Hoje consegui não apostar. Me sentindo bem!",
  "createdAt": "2025-09-22T00:10:00Z",
  "authorName": "Fênix Serena #4821",
  "tags": ["VITORIA", "MOTIVACAO"],
  "supportsCount": 0
}
```

---

### Listar Feed (paginado)

**GET** `/api/community/posts?page=0&size=5`
Resposta:

```json
{
  "content": [
    {
      "id": 10,
      "content": "Hoje consegui não apostar. Me sentindo bem!",
      "createdAt": "2025-09-22T00:10:00Z",
      "authorName": "Fênix Serena #4821",
      "tags": ["VITORIA","MOTIVACAO"],
      "supportsCount": 0
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "size": 5,
  "number": 0
}
```

---

### Apoiar um Post

**POST** `/api/community/posts/{id}/support?userId=1`
Erro se usuário já apoiou: `422 BusinessRule`.

---

### Desfazer apoio

**DELETE** `/api/community/posts/{POST_ID}/support?userId={USER_ID}`

---

### Comentar em um Post

**POST** `/api/community/posts/{id}/comments`

```json
{
  "communityUserId": 1,
  "content": "Tamo junto!"
}
```

Resposta:

```json
{
  "id": 55,
  "content": "Tamo junto!",
  "createdAt": "2025-09-22T00:15:00Z",
  "authorName": "Fênix Serena #4821"
}
```

---

## 🛠️ Tecnologias Utilizadas

* **Java 17**
* **Spring Boot 3.5.6**

  * Spring Web
  * Spring Data JPA
  * Bean Validation
* **PostgreSQL** (Docker)
* **Flyway** (migrações de banco de dados)
* **Lombok**
* **Maven**
* **Postman** (testes manuais de endpoints)
* **Docker Compose**

---

## 👥 Integrantes

* **Guilherme Rocha Bianchini** – RM97974
* **Nikolas Rodrigues Moura dos Santos** – RM551566
* **Pedro Henrique Pedrosa Tavares** – RM97877
* **Rodrigo Brasileiro** – RM98952
* **Thiago Jardim de Oliveira** – RM551624


## Diagrama de arquitetura
<img width="1136" height="762" alt="FyoraCommunity-Architecture" src="https://github.com/user-attachments/assets/97ca8034-68c7-49d1-bc0b-3b08ffef68f2" />


## Diagrama de entidades
<img width="990" height="523" alt="FyoraCommunity-ER" src="https://github.com/user-attachments/assets/36925b3a-47bd-412f-8e66-0a73c4f9d964" />

