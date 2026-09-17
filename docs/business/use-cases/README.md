# 📊 Diagramas de Casos de Uso (Use Cases)

Este diretório contém a modelagem em **PlantUML** dos Casos de Uso do sistema **GWJ (Tgo's Barbearia)**, abrangendo o painel administrativo/gerencial e o storefront público/atendimento ao cliente.

---

## 📂 Diagramas Disponíveis

| Módulo / Escopo | Arquivo PlantUML | Diagrama Vetorial (SVG) | Imagem (PNG) | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| **Painel Administrativo & Dashboard** | [`general_dashboard.puml`](./general_dashboard.puml) | [`general_dashboard.svg`](./general_dashboard.svg) | [`general_dashboard.png`](./general_dashboard.png) | Casos de uso do Dashboard central (`/MRYnZpAsC9sp`), 6 KPIs em tempo real, navegação para módulos operacionais e de gestão estratégica, com proteção RBAC (`AdminInterceptor`). |
| **Storefront & Loja Virtual** | [`storefront.puml`](./storefront.puml) | [`storefront.svg`](./storefront.svg) | [`storefront.png`](./storefront.png) | Casos de uso da vitrine pública: e-commerce de cosméticos e kits, manipulação do carrinho em sessão HTTP, checkout atômico com baixa de estoque, agendamento de serviços e área do cliente. |

---

## 🛠️ Como Regenerar os Diagramas

Caso realize alterações nos arquivos `.puml`, execute o comando abaixo no terminal da raiz do projeto:

```bash
plantuml -tsvg docs/business/use-cases/*.puml
plantuml -tpng docs/business/use-cases/*.puml
```
