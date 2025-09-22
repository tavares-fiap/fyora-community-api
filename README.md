# Fyora Community API

API REST para a funcionalidade de **Comunidade** do Fyora App, uma solução tecnológica de impacto social criada para apoiar pessoas que enfrentam o **jogo problemático** e promover **bem-estar, autocontrole e apoio coletivo**.

---

## 🌍 Sobre o Fyora App

O **Fyora** é uma plataforma móvel gratuita, anônima e empática, concebida como uma resposta à epidemia silenciosa do jogo problemático no Brasil. Desenvolvida com apoio da **XP Inc.**, busca oferecer ferramentas de autocontrole, suporte emocional, educação financeira e conexão com redes de apoio.

A **aba de Comunidade**, representada por esta API, é parte essencial do ecossistema, permitindo que os usuários compartilhem experiências, encontrem apoio mútuo e fortaleçam vínculos sociais durante sua jornada de recuperação.

---

## 🚀 Funcionalidades da API

- 📌 **Criação e gestão de posts** na comunidade  
- 💬 **Comentários** e interações em posts  
- 👍 **Curtidas/Reações** em conteúdos da comunidade  
- 👤 **Gestão de usuários** (criação, autenticação e associação a posts/comentários)  
- 🔍 **Busca e listagem** de publicações com filtros  

---

## 🛠️ Tecnologias Utilizadas

- **Java 17**  
- **Spring Boot** (REST API)  
- **Maven** (gestão de dependências)  
- **Docker / Docker Compose** (infraestrutura de containers)  
- **Banco de Dados Relacional** (via configuração em `docker-compose.yml`)  

---

## 📂 Estrutura do Projeto

```bash
fyora-community-api/
│── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── fyora/
│   │   │   │   │   ├── community/
│   │   │   │   │   │   ├── controller/
│   │   │   │   │   │   ├── service/
│   │   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── model/
│   │   │   │   │   │   ├── dto/
│   │   │   │   │   │   └── config/
│   │   │   └── (outras packages utilitárias, autenticação etc.)
│   │   └── resources/
│   │       ├── application.yml (ou application.properties)
│   │       └── outras configurações / arquivos estáticos (templates, etc.)
│   └── test/
│       ├── java/
│       │   └── com/
│       │       └── fyora/
│       │           └── community/
│       │               └── (testes unitários e de integração)
│       └── resources/
│           └── (recursos de teste)
│── pom.xml
│── docker-compose.yml
│── mvnw / mvnw.cmd
```

## ⚙️ Como Executar Localmente

### Pré-requisitos
- [Java 17+](https://adoptium.net/)  
- [Maven](https://maven.apache.org/)  
- [Docker](https://www.docker.com/)  

### Passos
```bash
# Clonar o repositório
git clone https://github.com/tavares-fiap/fyora-community-api.git
cd fyora-community-api

# Subir infraestrutura via Docker
docker-compose up -d

# Rodar a aplicação
./mvnw spring-boot:run
````
A API estará disponível em:
👉 http://localhost:8080/api/community

---

## 📖 Endpoints Principais (exemplo)

| Método | Endpoint                   | Descrição                     |
|--------|----------------------------|--------------------------------|
| GET    | `/posts`                   | Lista todos os posts           |
| POST   | `/posts`                   | Cria um novo post              |
| GET    | `/posts/{id}`              | Detalhes de um post específico |
| POST   | `/posts/{id}/comments`     | Adiciona comentário            |
| POST   | `/posts/{id}/like`         | Curte um post                  |

---

## 🎯 Objetivo da Comunidade Fyora

- Criar um **ambiente seguro** e acolhedor para troca de experiências.  
- Reduzir o **isolamento social** associado ao transtorno de jogo.  
- Incentivar **apoio mútuo e resiliência coletiva**.  

---

## Integrantes

GUILHERME ROCHA BIANCHINI - RM97974  
NIKOLAS RODRIGUES MOURA DOS SANTOS - RM551566  
PEDRO HENRIQUE PEDROSA TAVARES - RM97877  
RODRIGO BRASILEIRO - RM98952  
THIAGO JARDIM DE OLIVEIRA - RM551624  

---

## 📜 Licença

Este projeto é open-source sob a licença MIT.  <br>
Projeto desenvolvido pela equipe TechPulse Global Network para o Challenge de Arquitetura orientada a Servicos e Web Services da FIAP.
