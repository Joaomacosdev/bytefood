# 🍔 ByteFood

Sistema backend completo de pedidos de comida (Food Ordering System), desenvolvido em **Java 21 + Spring Boot**, aplicando **Arquitetura de Domínio**, **boas práticas de engenharia de software**, **segurança**, **pagamentos online** e **integrações externas**.

O ByteFood simula o funcionamento de plataformas como **iFood / Uber Eats**, permitindo autenticação de usuários, gerenciamento de cardápio, carrinho, pedidos, pagamentos, avaliações e notificações.

---

## 📌 O que o Projeto Faz

O **ByteFood** é responsável por:

- Cadastro e autenticação de usuários
- Autorização baseada em roles (perfis)
- Gerenciamento de categorias e cardápio
- Carrinho de compras
- Criação e controle de pedidos
- Pagamento online via Stripe
- Upload de arquivos usando AWS S3
- Envio de notificações por e-mail
- Avaliações de pedidos
- Tratamento global de exceções

---

## 🧠 Arquitetura de Domínio

O projeto foi desenvolvido seguindo **Arquitetura de Domínio**, onde cada módulo representa um **contexto de negócio independente**, evitando acoplamento excessivo e facilitando manutenção, testes e escalabilidade.

### 🔹 Princípios Aplicados
- **Separação de responsabilidades**
- **Baixo acoplamento**
- **Alta coesão**
- **Domain-Driven Design (DDD)**
- **SOLID**
- **Clean Code**

Cada domínio possui suas próprias camadas, como:
- `controller` → Entrada da aplicação (HTTP)
- `service` → Regras de negócio
- `repository` → Persistência
- `entity` → Modelo de domínio
- `dto` → Transporte de dados

---

## 🏗️ Estrutura de Domínios

```text
br.com.bytefood
├── auth_users         # Domínio de autenticação e usuários
├── cart               # Domínio do carrinho de compras
├── category           # Domínio de categorias
├── menu               # Domínio do cardápio
├── order              # Domínio de pedidos
├── payment            # Domínio de pagamentos
├── review             # Domínio de avaliações
├── email_notification # Domínio de notificações
├── aws                # Integrações AWS (S3)
├── security           # Configurações de segurança e JWT
├── config             # Configurações gerais
├── enums              # Enumerações globais
├── exception          # Tratamento global de erros
└── BytefoodApplication
````

📌 Essa organização permite que cada domínio evolua de forma independente, facilitando a adição de novas funcionalidades sem impacto nos outros módulos.

---

## 🗺️ Diagrama de Arquitetura

![Arquitetura do Sistema](https://github.com/Joaomacosdev/bytefood/blob/main/food_app_diagram.png?raw=true)

---

## 🚀 Tecnologias Utilizadas

### 🧠 Backend
![Java](https://img.shields.io/badge/Java-21-red?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-brightgreen?logo=springboot)
![Spring Security](https://img.shields.io/badge/Spring_Security-6-green?logo=springsecurity)
![Spring Data JPA](https://img.shields.io/badge/JPA-Hibernate-blue)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-Template-darkgreen)

---

### 🗄️ Banco de Dados
![MySQL](https://img.shields.io/badge/MySQL-Database-orange?logo=mysql)

---

### ☁️ Infraestrutura e Integrações
![AWS S3](https://img.shields.io/badge/AWS-S3-orange?logo=amazonaws)
![Stripe](https://img.shields.io/badge/Stripe-Payments-blueviolet?logo=stripe)
![JWT](https://img.shields.io/badge/JWT-Authentication-black?logo=jsonwebtokens)

---

### 🛠️ Ferramentas
![Maven](https://img.shields.io/badge/Maven-Build-red?logo=apachemaven)
![Lombok](https://img.shields.io/badge/Lombok-Productivity-pink)

---

## ⚙️ Como Instalar e Rodar Localmente

### 📋 Pré-requisitos
- Java 21
- Maven 3.9+
- MySQL 8+
- Conta AWS (S3)
- Conta Stripe

---

### 1️⃣ Clonar o repositório
```bash
git clone https://github.com/Joaomacosdev/bytefood.git
cd bytefood
```

## ⚙️ Como Instalar e Rodar Localmente

### 2️⃣ Configurar o Banco de Dados (MySQL)

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/db_bytefood_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo&createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=admin
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
```

## 🖼️ Screenshots

### Estrutura do Projeto
![Estrutura do Projeto](https://github.com/Joaomacosdev/bytefood/blob/main/1769036396593.jpeg?raw=true)

---

## 💳 Funcionalidades Implementadas

- ✅ Cadastro e login de usuários
- ✅ Autenticação com JWT
- ✅ Autorização por roles
- ✅ Carrinho de compras
- ✅ Criação de pedidos
- ✅ Pagamento via Stripe
- ✅ Upload de arquivos no AWS S3
- ✅ Envio de e-mails
- ✅ Avaliação de pedidos
- ✅ Tratamento global de exceções

---

## 🧩 Desafios Enfrentados

### 🔐 Segurança
Implementação do Spring Security com JWT para garantir autenticação e autorização seguras.

### 💳 Pagamentos
Integração com Stripe garantindo consistência entre pedido e pagamento.

### ☁️ Upload de Arquivos
Upload seguro e escalável utilizando AWS S3.

### 🧱 Arquitetura
Separação por domínios para garantir escalabilidade, organização e manutenibilidade do código.

---

## 🌐 Deploy

🚧 Em andamento  
*(Planejado para AWS, Railway ou Render)*

---

## 👨‍💻 Autor

**João Marcos**  
Java Backend Developer

Spring Boot • Arquitetura de Domínio • Segurança • AWS • Pagamentos
