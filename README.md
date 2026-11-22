# Backend — Rede Raízes do Nordeste

Backend do estudo de caso "Rede Raízes do Nordeste" (Projeto Multidisciplinar — trilha Back-end). Implementa os fluxos centrais descritos no estudo de caso: cardápio por unidade (com diferenças regionais e sazonalidade), pedidos multicanal, pagamento desacoplado de um gateway externo, controle de estoque, fidelidade com conformidade LGPD e auditoria de operações sensíveis.

## Stack

- Java 17, Spring Boot 3 (Web, Data JPA, Validation)
- PostgreSQL + Flyway (migrations versionadas)
- Lombok

## Estrutura

```
src/main/java/com/raizesdonordeste/backend/
  domain/        entidades JPA e enums
  repository/     interfaces Spring Data JPA
  service/        regras de negócio
  web/            controllers REST
  dto/            request/response
  gateway/        porta de integração com o gateway de pagamento externo
  exception/      exceções de domínio e handler global
src/main/resources/db/migration/   scripts Flyway (schema + dados de exemplo)
docs/diagramas/   casos de uso, classes, DER, sequência de pagamento, componentes
docs/api.md       documentação dos endpoints
```

## Como rodar localmente

Pré-requisitos: JDK 17, Maven, PostgreSQL rodando localmente (ou ajuste `application.yml`).

```bash
createdb raizes_nordeste
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`. Os dados de exemplo (seed) já populam 2 unidades, 5 produtos e estoque inicial — ver `V2__seed_data.sql`.

## Decisões de arquitetura

Ver `docs/diagramas/componentes.md` para a justificativa da arquitetura (monólito modular, pagamento desacoplado via porta/adaptador, estratégia de alta disponibilidade).

## Limitações conhecidas / não implementado

- Autenticação/autorização (JWT por perfil de usuário) foi projetada mas não implementada neste recorte — endpoints estão abertos para fins de demonstração acadêmica.
- Job de conciliação para pagamentos que nunca recebem webhook não foi implementado (mencionado como ponto de atenção em `docs/diagramas/sequencia-pagamento.md`).
- Multi-região/réplicas de leitura ficaram apenas como decisão arquitetural documentada, não implementada.
