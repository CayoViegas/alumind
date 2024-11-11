# AluMind - Plataforma de Bem-Estar e Feedback Inteligente

## Contexto

A **AluMind** é uma startup que oferece uma aplicação focada em bem-estar e saúde mental, proporcionando aos usuários acesso a meditações guiadas, sessões de terapia e conteúdos educativos. Com a alta demanda, a AluMind deseja entender melhor a experiência dos usuários. Para isso, desenvolvemos um sistema que captura e analisa feedbacks de múltiplas plataformas (como redes sociais e atendimento ao cliente), classificando sentimentos, identificando sugestões de funcionalidades e gerando respostas personalizadas para engajamento e melhoria contínua.

## Modelagem da Aplicação

A modelagem foi projetada para facilitar o fluxo de dados, desde o recebimento dos feedbacks até o processamento e armazenamento das respostas personalizadas. 

### Componentes Principais

1. **Feedback**: Representa o feedback enviado pelo usuário, armazenando também uma referência à resposta gerada (FeedbackResponse) para rastrear cada feedback individualmente.
   
2. **FeedbackResponse**: Contém a análise de sentimento (positiva, negativa, inconclusiva ou spam), uma lista de sugestões de funcionalidades (RequestedFeature) extraídas do feedback e uma mensagem de resposta personalizada para engajamento.

3. **RequestedFeature**: Representa funcionalidades sugeridas pelos usuários, com um código único e uma razão que descreve sua importância, essencial para priorização de melhorias.

4. **GroqService**: Serviço que realiza chamadas à API Groq para processar feedbacks, extraindo informações de sentimento, funcionalidades sugeridas e mensagens de resposta. O **Groq** foi escolhido por ser gratuito e fácil de usar, o que o torna uma boa solução prática para o momento, mesmo não sendo a opção mais sofisticada do mercado.

5. **Utils e GroqHelper**: Classes utilitárias para o suporte geral da aplicação, como o carregamento de prompts do sistema e o parsing das respostas JSON da API Groq.

### Escolha da Modelagem para Geração de Respostas Personalizadas

A escolha do uso de uma **LLM** (Modelo de Linguagem de Grande Escala) foi motivada pela sua capacidade de compreender o contexto emocional dos feedbacks e gerar respostas engajantes e contextualizadas. Esse tipo de modelagem traz diversas vantagens:

- **Automatização Inteligente**: Respostas automáticas, porém personalizadas, que aumentam a satisfação dos usuários.
- **Identificação de Sentimento e Funcionalidades**: A LLM identifica o tom emocional e propostas de melhoria, ajudando a equipe a priorizar atualizações de forma inteligente.
- **Escalabilidade**: Com a capacidade de responder automaticamente a múltiplos feedbacks, o sistema é escalável e preparado para um crescimento contínuo da base de usuários.

## Como Executar a Aplicação

Este projeto foi desenvolvido com **Java (Spring Boot)** e **PostgreSQL** como banco de dados. Abaixo estão as instruções para execução em diferentes sistemas operacionais.

### Pré-requisitos

- **Java 23**
- **Maven**
- **PostgreSQL** instalado e em execução
- Configuração de variáveis de ambiente para `SPRING_AI_OPENAI_BASE_URL` e `SPRING_AI_OPENAI_API_KEY`

### Configuração do Banco de Dados (PostgreSQL)

1. Crie um banco de dados PostgreSQL.
2. Configure o acesso ao banco no arquivo `application.properties` ou como variáveis de ambiente:
```properties
spring.application.name=AluMind
spring.datasource.url=jdbc:postgresql://localhost:5432/alumind
spring.datasource.username=<seu_usuario>
spring.datasource.password=<sua_senha>

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update

spring.ai.openai.api-key=<sua_api_key>
spring.ai.openai.base-url=https://api.groq.com/openai
spring.ai.openai.chat.model=llama-3.1-70b-versatile
```
### Executando
1. Clone o repositório:
```bash
git clone https://github.com/CayoViegas/alumind.git
cd alumind
```
2. Compile e Instale Dependências:
```bash
mvn clean install
```
3. Execute a Aplicação:
```bash
mvn spring-boot:run
```
4. A aplicação estará disponível em `http://localhost:8080`
### Endpoints Principais
- **POST /feedbacks**: Recebe um feedback em formato JSON e retorna uma resposta com sentimento, funcionalidades sugeridas e uma mensagem personalizada.

Exemplo de corpo da requisição:
```json
{
    "feedback": "A aplicação é excelente, mas poderia ter um tema escuro."
}
```

Exemplo de resposta:
```json
{
    "id": 1,
    "sentiment": "POSITIVO",
    "requestedFeatures": [
        {
            "code": "TEMA_ESCURO",
            "reason": "O usuário gostaria de um tema escuro para o aplicativo"
        }
    ],
    "responseMessage": "Obrigado pelo seu feedback! Ficamos felizes que esteja gostando e vamos considerar a sugestão do tema escuro."
}
```

## Funcionalidade Proposta: Insights Emocionais e Recomendação Personalizada
### Contexto e Objetivo
Para melhorar a experiência dos usuários e sua retenção, propõe-se a funcionalidade **Insights Emocionais e Recomendação Personalizada**. Essa funcionalidade utilizaria o poder das LLMs para analisar tendências emocionais nos feedbacks, tanto individualmente quanto em massa, fornecendo insights para usuários e a equipe de produto.
### Proposta de Funcionalidade
A funcionalidade possui dois componentes principais:
1. **Análise Emocional Longitudinal para o Usuário Individual:**
    - Cada usuário teria um "histórico emocional" baseado nos feedbacks enviados, permitindo à aplicação recomendar conteúdos e funcionalidades que otimizem seu bem-estar.
    - Os usuários teriam acesso a uma visualização das mudanças em seus estados emocionais ao longo do tempo, além de recomendações para aprimorar o uso da plataforma.
2. **Dashboard de Tendências para a Equipe de Produto:**
    - A LLM analisaria clusters de feedbacks para identificar padrões de sentimento e demandas recorrentes.
    - A equipe de produto teria um painel que destacaria sugestões e tendências, ajudando a priorizar melhorias de acordo com as necessidades dos usuários.
### Implementação e Benefícios
Para implementar essa funcionalidade, o sistema poderia usar uma LLM configurada para detectar padrões de sentimento e agrupamentos de feedback. Com isso, a AluMind consegue:
- **Para o Usuário**: Fornecer insights sobre interações e recomendações personalizadas, aumentando a retenção e a satisfação.
- **Para a Equipe de Produto**: Ganhar um entendimento mais profundo e automatizado das tendências emocionais e necessidades dos usuários, facilitando a priorização de funcionalidades e correções.

Essa funcionalidade posicionaria a AluMind como uma plataforma de bem-estar que evolui junto com os usuários e responde proativamente às suas necessidades.

## Reflexões e Dúvidas Durante o Desenvolvimento

Houve algumas dúvidas em relação à modelagem e persistência dos dados no banco de dados, especialmente sobre quais informações deveriam ou não ser armazenadas. Optou-se por uma abordagem que mantém todos os detalhes relevantes sobre cada feedback no banco, o que facilita o rastreamento e permite responder a qualquer dúvida relacionada a um feedback específico de forma rápida e precisa, acessando diretamente os dados armazenados.

Em relação à geração de respostas personalizadas para os feedbacks, decidiu-se implementar essa funcionalidade dentro de um único endpoint. Essa escolha visa simplificar o fluxo de respostas e unificar o processamento de cada feedback, tornando a estrutura do código mais enxuta e a resposta mais direta. Essa modelagem trouxe simplicidade, permitindo que o feedback fosse tratado e respondido em uma única chamada, o que facilita tanto o desenvolvimento quanto a manutenção do projeto.
