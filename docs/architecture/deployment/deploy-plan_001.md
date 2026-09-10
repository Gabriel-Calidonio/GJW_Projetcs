# DP-1: Impedir Agendamento no Passado

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-06-16 16:48:00
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Impedir Agendamento no Passado
Este plano de implementação descreve as alterações necessárias para impedir que os clientes visualizem como disponíveis ou confirmem agendamentos em horários/datas que já passaram, garantindo a consistência das regras de negócio tanto no frontend quanto no backend.

### Avaliação do usuário necessária

**NOTE**
As alterações serão aplicadas em ambas as classes de serviço de agendamento existentes no sistema (AgendamentoService e AgendaService) para garantir consistência em toda a base de código. Além disso, adicionaremos validação na rota do checkout (/checkout) e na finalização (/checkout/confirmar).
### **Alterações propostas**
[Backend Services]
[MODIFY] AgendamentoService.java

- Alterar o método getHorariosDisponiveis para verificar se a data consultada é a data de hoje. Caso seja, verificar se o horário de início de cada slot é anterior ao horário atual (LocalTime.now()). Se for anterior, marcar o slot como indisponível (disponivel = false).
- Se a data consultada for anterior ao dia de hoje, marcar todos os slots do dia como indisponíveis.
- Alterar o método confirmarReserva para rejeitar (lançando uma RuntimeException) tentativas de confirmar reservas em datas/horários passados.

**[MODIFY] AgendaService.java**
Aplicar as mesmas regras de verificação de horários no passado aos métodos getHorariosDisponiveis e confirmarReserva na classe AgendaService para manter a paridade lógica.
[Backend Controller / Router]
**[MODIFY] Router.java**
No método @GetMapping("/checkout"), validar se o parâmetro dataHora recebido representa um momento no passado. Caso sim, adicionar um atributo de erro à model informando que o horário está indisponível/passado.
[Frontend templates]
**[MODIFY] checkout.html**
Modificar o botão de submissão do formulário de checkout para desabilitar a confirmação (th:disabled="${erro != null}") caso a página seja carregada com um erro de horário passado.
## Verification Plan
Automated Tests

- Executar os testes unitários do Maven:

```
bash
mvn test
```

- Adicionar novos cenários de testes unitários em AgendamentoServiceTest.java para validar a indisponibilidade de slots passados e a rejeição ao tentar confirmar agendamentos passados.

### Manual Verification

- Iniciar a aplicação Spring Boot localmente.
- Acessar a página de agendamento e verificar que os horários do dia atual que já passaram estão indisponíveis (desabilitados e cinzas) na grade de horários.
- Tentar forçar o acesso à rota /checkout com uma data e hora no passado e verificar o surgimento do alerta vermelho e o botão "Confirmar Reserva" desabilitado.

# Tarefas para validação de agendamentos no passado

-  Implementar validação de horário passado no AgendamentoService
-  Ajustar getHorariosDisponiveis para desabilitar horários passados do dia atual
-  Ajustar confirmarReserva para rejeitar reservas em horários passados
-  Implementar validação de horário passado no AgendaService
-  Ajustar getHorariosDisponiveis para desabilitar horários passados do dia atual
-  Ajustar confirmarReserva para rejeitar reservas em horários passados
-  Implementar validação de horário passado na rota GET /checkout do Router
-  Atualizar checkout.html para desabilitar o botão de finalização caso haja erro
-  Escrever testes unitários em AgendamentoServiceTest para cobrir os cenários novos
-  Executar mvn test para garantir o funcionamento correto de toda a suíte de testes
