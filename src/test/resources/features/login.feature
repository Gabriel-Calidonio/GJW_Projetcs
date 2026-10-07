# language: pt

@auth @login @exaustivo
Funcionalidade: Autenticação de Usuários e Gestão de Sessão de Login
  Como um usuário cadastrado (Cliente ou Administrador) ou visitante
  Quero autenticar minhas credenciais na interface de login
  Para acessar o sistema com segurança e gerenciar minha sessão

  Contexto:
    Dado que o usuário está na página de login ("/login")

  @sucesso @login_cliente
  Cenário: Login realizado com sucesso por Cliente
    Quando o usuário preenche o e-mail com "cliente@teste.com"
    E o usuário preenche a senha com "Senha@123"
    E clica no botão de entrar
    Então o sistema deve autenticar o usuário
    E deve redirecionar para a página principal ("/")
    E a sessão deve conter o usuário autenticado

  @sucesso @login_admin
  Cenário: Login realizado com sucesso por Administrador
    Quando o usuário preenche o e-mail com "admin@teste.com"
    E o usuário preenche a senha com "Senha@123"
    E clica no botão de entrar
    Então o sistema deve autenticar o usuário
    E deve redirecionar para o painel administrativo ("/MRYnZpAsC9sp")

  @falha @seguranca @anti_enumeracao
  Cenário: Tentativa de login com senha incorreta
    Quando o usuário preenche o e-mail com "cliente@teste.com"
    E o usuário preenche a senha com "SenhaIncorreta"
    E clica no botão de entrar
    Então o sistema deve exibir a mensagem de erro "E-mail ou senha inválidos."
    E a sessão não deve ser criada

  @falha @seguranca @anti_enumeracao
  Cenário: Tentativa de login com e-mail não cadastrado
    Quando o usuário preenche o e-mail com "inexistente@teste.com"
    E o usuário preenche a senha com "Senha@123"
    E clica no botão de entrar
    Então o sistema deve exibir a mensagem de erro "E-mail ou senha inválidos."
    E a sessão não deve ser criada

  @seguranca @restricao_perfil
  Cenário: Cliente tentando autenticar na área restrita administrativa
    Dado que o cliente acessa a rota administrativa restrita ("/MRYnZpAsC9sp/login")
    Quando o cliente preenche as credenciais válidas de sua conta de cliente
    E clica no botão de entrar
    Então o sistema deve exibir a mensagem "Acesso negado: esta área é restrita a administradores."
    E a sessão administrativa não deve ser concedida

  @sessao @redirecionamento_ativo
  Cenário: Usuário já autenticado tentando acessar a página de login
    Dado que o usuário já possui sessão ativa
    Quando o usuário acessa a página de login ("/login")
    Então o sistema deve redirecionar diretamente para seu respectivo painel
    E não deve reapresentar o formulário de login

  @logout @sessao
  Cenário: Encerramento de sessão (Logout) com sucesso
    Dado que o usuário possui sessão ativa
    Quando o usuário clica no botão de sair ou acessa "/logout"
    Então a sessão do usuário deve ser invalidada
    E o sistema deve redirecionar para "/login"
