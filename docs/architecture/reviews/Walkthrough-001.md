# Walkthrough-001: Validação de Agendamentos no Passado
Implementamos a regra de negócio para impedir que clientes agendem serviços em horários e datas que já passaram, tanto no frontend quanto no backend.

Modificações Realizadas
### 1. Backend: Serviços de Agendamento
**AgendamentoService.java:**

        - Modificamos o método getHorariosDisponiveis para desabilitar slots passados quando a data requisitada é o dia atual (ou datas passadas).
        - Modificamos o método confirmarReserva para lançar uma RuntimeException caso a data/hora selecionada esteja no passado, impedindo requisições forçadas.
**AgendaService.java:**

      - Aplicamos a mesma lógica de validação nos métodos getHorariosDisponiveis e confirmarReserva para assegurar integridade no restante do sistema.

### 2. Backend: Controlador / Roteamento
Router.java:

- Adicionamos validação na rota GET /checkout. Se a data/hora passada no parâmetro da URL estiver no passado, uma mensagem de erro é injetada na model e repassada ao template.

### 3. Frontend: Página de Checkout
checkout.html:

- Atualizamos o botão "Confirmar Reserva" para ficar desabilitado caso exista o atributo de erro na tela (th:disabled="${erro != null}"), impedindo o envio de dados inválidos.
## Verificação e Testes

1. Testes Automatizados
Escrevemos e executamos testes unitários específicos em 
AgendamentoServiceTest.java:

- testHorariosNoPassadoComoIndisponiveis: Garante que, ao buscar horários de uma data no passado (ex: terça-feira de uma semana anterior), todos os slots retornam marcados como indisponíveis (disponivel = false).
- testConfirmarReservaNoPassadoLancaExcecao: Garante que a tentativa de confirmação de reserva de datas passadas lança uma exceção de runtime.

Rodamos o comando:

bash

`mvn test`
Resultado:

text

```
[INFO] Running com.gwj.service.AgendamentoServiceTest
✅ Todas as classes em 'com.gwj.model.domain.entities' implementam IEntity e seguem os padrões.
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 0.275 s - - in com.gwj.service.AgendamentoServiceTest
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```
## Todos os 10 testes da aplicação passaram com sucesso.

# Finalizei a implementação da regra de negócio para impedir agendamentos no passado.

Todas as alterações propostas no plano de implementação foram realizadas com sucesso e a suíte de testes unitários passou em sua totalidade (incluindo novos testes desenvolvidos para cobrir as regras de data/hora no passado).

Para ver os detalhes das modificações realizadas, os testes e os resultados da validação, consulte o artefato

