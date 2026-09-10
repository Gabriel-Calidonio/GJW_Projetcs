-- ========================================================
-- Script de Criação e Inicialização de Dados para PostgreSQL
-- Compatível com PostgreSQL 12+ (incluindo PostgreSQL 16 LTS)
-- ========================================================

-- 1. Tabela de Perfis de Usuário
CREATE TABLE IF NOT EXISTS tab_perfil (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO tab_perfil (id, nome) VALUES
(1, 'Administrador'),
(2, 'Recepcionista'),
(3, 'Barbeiro'),
(4, 'Cliente')
ON CONFLICT (id) DO NOTHING;

-- 2. Tabela de Permissões
CREATE TABLE IF NOT EXISTS tab_permissao (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE,
  descricao VARCHAR(255)
);

INSERT INTO tab_permissao (id, nome, descricao) VALUES
(1, 'AGENDAR_HORARIO', 'Permite que o cliente ou recepcionista crie um agendamento'),
(2, 'CANCELAR_AGENDAMENTO', 'Permite cancelar um horário agendado'),
(3, 'VISUALIZAR_PROPRIA_AGENDA', 'Permite ao barbeiro ver apenas os seus atendimentos'),
(4, 'GERENCIAR_TODAS_AGENDAS', 'Permite à recepção/adm mover e organizar horários de todos'),
(5, 'GERENCIAR_CLIENTES', 'Permite cadastrar, editar ou bloquear perfis de clientes'),
(6, 'GERENCIAR_SERVICOS', 'Permite alterar preços e cadastrar novos serviços (Cabelo, Barba, etc)'),
(7, 'VISUALIZAR_FATURAMENTO', 'Permite ver relatórios financeiros e comissões da equipe'),
(8, 'GERENCIAR_ESTOQUE', 'Permite dar entrada e saída em produtos (Pomadas, Shampoos)')
ON CONFLICT (id) DO NOTHING;

-- Compatibilidade com tabela legada 'permissoes'
CREATE TABLE IF NOT EXISTS permissoes (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL UNIQUE,
  descricao VARCHAR(255)
);

INSERT INTO permissoes (id, nome, descricao) VALUES
(1, 'AGENDAR_HORARIO', 'Permite que o cliente ou recepcionista crie um agendamento'),
(2, 'CANCELAR_AGENDAMENTO', 'Permite cancelar um horário agendado'),
(3, 'VISUALIZAR_PROPRIA_AGENDA', 'Permite ao barbeiro ver apenas os seus atendimentos'),
(4, 'GERENCIAR_TODAS_AGENDAS', 'Permite à recepção/adm mover e organizar horários de todos'),
(5, 'GERENCIAR_CLIENTES', 'Permite cadastrar, editar ou bloquear perfis de clientes'),
(6, 'GERENCIAR_SERVICOS', 'Permite alterar preços e cadastrar novos serviços (Cabelo, Barba, etc)'),
(7, 'VISUALIZAR_FATURAMENTO', 'Permite ver relatórios financeiros e comissões da equipe'),
(8, 'GERENCIAR_ESTOQUE', 'Permite dar entrada e saída em produtos (Pomadas, Shampoos)')
ON CONFLICT (id) DO NOTHING;

-- 3. Vínculo Perfil x Permissão
CREATE TABLE IF NOT EXISTS tab_perfil_permissao (
  perfil_id INT NOT NULL REFERENCES tab_perfil(id) ON DELETE CASCADE,
  permissao_id INT NOT NULL REFERENCES tab_permissao(id) ON DELETE CASCADE,
  PRIMARY KEY (perfil_id, permissao_id)
);

INSERT INTO tab_perfil_permissao (perfil_id, permissao_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1),
(1, 2), (2, 2), (3, 2), (4, 2),
(1, 3), (3, 3),
(1, 4), (2, 4),
(1, 5), (2, 5),
(1, 6),
(1, 7),
(1, 8), (2, 8)
ON CONFLICT (perfil_id, permissao_id) DO NOTHING;

-- 4. Tabela de Usuários
CREATE TABLE IF NOT EXISTS tab_usuario (
  id BIGSERIAL PRIMARY KEY,
  perfil_id BIGINT REFERENCES tab_perfil(id) ON DELETE SET NULL,
  nome_usuario VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  senha VARCHAR(255) NOT NULL,
  status BOOLEAN DEFAULT TRUE,
  token VARCHAR(255),
  ip VARCHAR(45),
  data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO tab_usuario (id, perfil_id, nome_usuario, email, senha, status, token, ip, data_cadastro) VALUES
(2, 3, 'barbeiro1', 'carlos@email.com', '123', TRUE, NULL, NULL, '2026-05-07 19:28:21'),
(3, 4, 'cliente1', 'marcos@email.com', '{sha256}pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mY6G9/eieuM=', TRUE, 'byscKmvPh87emtl7fAjYKJHkOFfzPlYH83TDsZtX372c0lmEVkgfzKALwGjCBT9S', '192.168.0.1', '2026-05-07 19:28:21'),
(4, 1, 'Pedro123', 'pedro.cefas@yahoo.com.br', '4321', TRUE, '111', NULL, '2026-05-13 18:35:00'),
(6, 1, 'Teste', 'teste@teste.com', '123456', TRUE, NULL, NULL, '2026-05-25 18:45:20'),
(7, 1, 'Gabriel', 'gabriel@admin.com', '{sha256}CsTZVEv8fvog4F4xISxq3jbQHsrxfStz9x6Yd0vPWvo=', TRUE, NULL, NULL, '2026-06-06 16:57:50'),
(8, 1, 'Wallace', 'wallace@admin.com', '{sha256}UfLgCc5Yfrh7z7g4eCFkVwhlFSGPuNjMGB048x7OjTw=', TRUE, NULL, NULL, '2026-06-06 18:11:23'),
(9, 3, 'tiago', 'tiago.3554@gmail.com', '{sha256}jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=', TRUE, NULL, NULL, '2026-06-11 19:56:53'),
(10, 4, 'luiz.garcia@gmail.com', 'luiz.garcia@gmail.com', '{sha256}FeKw08M4keuw8e9gnsQZQgwg4yDOlMZfvIwzEkSOsiU=', TRUE, NULL, NULL, '2026-06-14 15:29:52')
ON CONFLICT (id) DO NOTHING;

-- 5. Tabela de Clientes (Herança de tab_usuario)
CREATE TABLE IF NOT EXISTS tab_cliente (
  id BIGINT PRIMARY KEY REFERENCES tab_usuario(id) ON DELETE CASCADE,
  nome VARCHAR(100) NOT NULL,
  sobrenome VARCHAR(100),
  telefone VARCHAR(20),
  cpf VARCHAR(14) UNIQUE,
  observacao TEXT
);

INSERT INTO tab_cliente (id, nome, sobrenome, telefone, cpf, observacao) VALUES
(3, 'Marcos', 'Oliveira de Souza', '(11) 98888-2222', '12345678901', 'Prefere às sextas-feiras'),
(6, 'Teste', 'Teste', '(11) 123456789', '12345612378955', 'teste'),
(10, 'Luiz', 'Garcia', '(11) 45678-9123', NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- 6. Tabela de Endereços
CREATE TABLE IF NOT EXISTS tab_endereco (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(100),
  logradouro VARCHAR(200),
  numero INT,
  complemento VARCHAR(100),
  bairro VARCHAR(100),
  cidade VARCHAR(100),
  estado CHAR(2),
  cep VARCHAR(10),
  data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  observacao TEXT
);

INSERT INTO tab_endereco (id, nome, logradouro, numero, complemento, bairro, cidade, estado, cep, data_cadastro, observacao) VALUES
(1, 'Casa', 'Rua das rosas', 123, 'Apto 12', 'Centro', 'São Paulo', 'SP', '01001-000', '2026-05-07 19:39:17', NULL),
(2, 'Trabalho', 'Avenida Paulista', 1000, 'Andar 15', 'Bela Vista', 'São Paulo', 'SP', '01310-100', '2026-05-07 19:39:17', NULL),
(3, 'Residencial', 'Rua da Praia', 50, NULL, 'Copacabana', 'Rio de Janeiro', 'RJ', '22020-001', '2026-05-07 19:39:17', NULL),
(4, 'Barbearia Matriz', 'Rua dos Barbeiros', 10, 'Térreo', 'Centro', 'São Paulo', 'SP', '01002-000', '2026-05-07 19:39:17', NULL),
(5, 'Paulo', 'Rua Antônio Massa', 999, NULL, 'Jardim do Papai', 'Ferraz de Vasconcelos', 'SP', '08505340', '2026-05-25 19:03:12', NULL)
ON CONFLICT (id) DO NOTHING;

-- 7. Vínculo Cliente x Endereço
CREATE TABLE IF NOT EXISTS tab_cliente_endereco (
  cliente_id BIGINT NOT NULL REFERENCES tab_cliente(id) ON DELETE CASCADE,
  endereco_id BIGINT NOT NULL REFERENCES tab_endereco(id) ON DELETE CASCADE,
  PRIMARY KEY (cliente_id, endereco_id)
);

INSERT INTO tab_cliente_endereco (cliente_id, endereco_id) VALUES
(3, 1),
(3, 2),
(6, 5)
ON CONFLICT (cliente_id, endereco_id) DO NOTHING;

-- 8. Tabela de Profissionais (Herança de tab_usuario)
CREATE TABLE IF NOT EXISTS tab_profissional (
  id BIGINT PRIMARY KEY REFERENCES tab_usuario(id) ON DELETE CASCADE,
  nome VARCHAR(100) NOT NULL,
  sobrenome VARCHAR(100),
  telefone VARCHAR(20),
  cpf VARCHAR(14) UNIQUE,
  observacao TEXT
);

INSERT INTO tab_profissional (id, nome, sobrenome, telefone, cpf, observacao) VALUES
(2, 'Carlos', 'Tesoura', '(11) 99999-1111', NULL, NULL),
(9, 'Tiago (Especialista em Fade)', NULL, NULL, NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- 9. Vínculo Profissional x Endereço
CREATE TABLE IF NOT EXISTS tab_profissional_endereco (
  profissional_id BIGINT NOT NULL REFERENCES tab_profissional(id) ON DELETE CASCADE,
  endereco_id BIGINT NOT NULL REFERENCES tab_endereco(id) ON DELETE CASCADE,
  PRIMARY KEY (profissional_id, endereco_id)
);

INSERT INTO tab_profissional_endereco (profissional_id, endereco_id) VALUES
(2, 3),
(2, 4)
ON CONFLICT (profissional_id, endereco_id) DO NOTHING;

-- 10. Dias de Funcionamento
CREATE TABLE IF NOT EXISTS tab_dias_funcionamento (
  id SERIAL PRIMARY KEY,
  dia_semana INT NOT NULL,
  nome VARCHAR(14) NOT NULL,
  aberto BOOLEAN DEFAULT TRUE,
  horario_inicio TIME,
  horario_fim TIME
);

INSERT INTO tab_dias_funcionamento (id, dia_semana, nome, aberto, horario_inicio, horario_fim) VALUES
(1, 1, 'Domingo', TRUE, '09:00:00', '19:00:00'),
(2, 2, 'Segunda-feira', FALSE, NULL, NULL),
(3, 3, 'Terça-feira', TRUE, '09:00:00', '19:00:00'),
(4, 4, 'Quarta-feira', TRUE, '09:00:00', '19:00:00'),
(5, 5, 'Quinta-feira', TRUE, '09:00:00', '19:00:00'),
(6, 6, 'Sexta-feira', TRUE, '09:00:00', '19:00:00'),
(7, 7, 'Sábado', TRUE, '09:00:00', '19:00:00')
ON CONFLICT (id) DO NOTHING;

-- 11. Grade de Horários
CREATE TABLE IF NOT EXISTS tab_grade_horarios (
  id SERIAL PRIMARY KEY,
  dia_funcionamento_id INT REFERENCES tab_dias_funcionamento(id) ON DELETE CASCADE,
  horario_inicio TIME NOT NULL,
  horario_fim TIME NOT NULL
);

INSERT INTO tab_grade_horarios (id, dia_funcionamento_id, horario_inicio, horario_fim) VALUES
(1, 1, '09:00:00', '09:20:00'),
(2, 1, '09:20:00', '09:40:00'),
(3, 1, '09:40:00', '10:00:00'),
(4, 1, '10:00:00', '10:20:00'),
(5, 1, '10:20:00', '10:40:00'),
(6, 1, '10:40:00', '11:00:00'),
(7, 1, '11:00:00', '11:20:00'),
(8, 1, '11:20:00', '11:40:00'),
(9, 1, '11:40:00', '12:00:00'),
(10, 1, '12:00:00', '12:20:00'),
(11, 1, '12:20:00', '12:40:00'),
(12, 1, '12:40:00', '13:00:00'),
(13, 1, '13:00:00', '13:20:00'),
(14, 1, '13:20:00', '13:40:00'),
(15, 1, '13:40:00', '14:00:00'),
(16, 1, '14:00:00', '14:20:00'),
(17, 1, '14:20:00', '14:40:00'),
(18, 1, '14:40:00', '15:00:00'),
(19, 1, '15:00:00', '15:20:00'),
(20, 1, '15:20:00', '15:40:00'),
(21, 1, '15:40:00', '16:00:00'),
(22, 1, '16:00:00', '16:20:00'),
(23, 1, '16:20:00', '16:40:00'),
(24, 1, '16:40:00', '17:00:00'),
(25, 1, '17:00:00', '17:20:00'),
(26, 1, '17:20:00', '17:40:00'),
(27, 1, '17:40:00', '18:00:00'),
(28, 1, '18:00:00', '18:20:00'),
(29, 1, '18:20:00', '18:40:00'),
(30, 1, '18:40:00', '19:00:00'),
(33, 3, '09:40:00', '10:00:00'),
(41, 3, '12:20:00', '12:40:00'),
(43, 3, '13:00:00', '13:20:00'),
(155, 6, '10:20:00', '10:40:00')
ON CONFLICT (id) DO NOTHING;

-- 12. Tabela de Serviços
CREATE TABLE IF NOT EXISTS tab_servico (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(100) NOT NULL,
  descricao VARCHAR(255),
  preco DECIMAL(10,2) NOT NULL,
  duracao INT,
  tipo VARCHAR(50),
  ativo BOOLEAN DEFAULT TRUE,
  imagem VARCHAR(255)
);

INSERT INTO tab_servico (id, nome, descricao, preco, duracao, tipo, ativo, imagem) VALUES
(1, 'Corte de Cabelo Masculino', 'Promocional', 45.00, 30, 'Tradicional', TRUE, '/img/Corte-cabelo-masculino.jpeg'),
(2, 'Barba Completa (Toalha Quente)', 'O mais pedido', 35.00, 20, 'Tradicional', TRUE, '/img/photo-1621605815971-fbc98d665033.jpeg'),
(3, 'Corte moicano', 'Corte estilo moicano', 130.00, 90, 'Exótico', TRUE, NULL),
(4, 'Trança rastafari', 'Estilo cultural', 230.00, 200, 'Cultural', TRUE, NULL),
(7, 'Combo (Cabelo + Barba)', 'Combo', 98.00, 110, 'Mais procurado', TRUE, NULL),
(8, 'Corte Infantil', 'Infanto', 45.00, 40, 'Infanto-Juvenil', TRUE, NULL)
ON CONFLICT (id) DO NOTHING;

-- 13. Tabela de Agendamentos
CREATE TABLE IF NOT EXISTS tab_agendamento (
  id BIGSERIAL PRIMARY KEY,
  cliente_nome VARCHAR(100) NOT NULL,
  cliente_telefone VARCHAR(20),
  profissional_id BIGINT NOT NULL,
  servico_id BIGINT NOT NULL,
  data_agendamento DATE NOT NULL,
  hora_inicio TIME NOT NULL,
  hora_fim TIME NOT NULL,
  status VARCHAR(20) DEFAULT 'Confirmado',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  grade_horarios_id INT REFERENCES tab_grade_horarios(id) ON DELETE SET NULL
);

INSERT INTO tab_agendamento (id, cliente_nome, cliente_telefone, profissional_id, servico_id, data_agendamento, hora_inicio, hora_fim, status, created_at, grade_horarios_id) VALUES
(1, 'Marcos Silva', '(11) 99999-1111', 2, 1, '2026-06-15', '09:00:00', '09:30:00', 'Confirmado', '2026-06-13 00:08:29', NULL),
(2, 'Arthur Rezende', '(11) 99999-2222', 2, 7, '2026-06-15', '10:00:00', '11:50:00', 'Confirmado', '2026-06-13 00:08:29', NULL),
(3, 'Bruno Oliveira', '(11) 99999-3333', 2, 2, '2026-06-15', '14:00:00', '14:20:00', 'Confirmado', '2026-06-13 00:08:29', NULL),
(4, 'Rodrigo Costa', '(11) 99999-4444', 2, 3, '2026-06-15', '15:30:00', '17:00:00', 'Confirmado', '2026-06-13 00:08:29', NULL),
(5, 'Marcos Silva', '(11) 99999-1111', 2, 1, '2026-06-15', '09:00:00', '09:30:00', 'Confirmado', '2026-06-13 00:09:34', NULL),
(9, 'Felipe Amorim', '(11) 99999-5555', 9, 4, '2026-06-15', '09:00:00', '12:20:00', 'Confirmado', '2026-06-13 00:09:34', NULL),
(10, 'Lucas (Pai: Roberto)', '(11) 99999-6666', 9, 8, '2026-06-15', '14:00:00', '14:40:00', 'Confirmado', '2026-06-13 00:09:34', NULL),
(11, 'Gustavo Henrique', '(11) 99999-7777', 9, 1, '2026-06-15', '15:00:00', '15:30:00', 'Confirmado', '2026-06-13 00:09:34', NULL)
ON CONFLICT (id) DO NOTHING;

-- 14. Tabela de Produtos
CREATE TABLE IF NOT EXISTS tab_produto (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(150) NOT NULL,
  descricao TEXT,
  preco DECIMAL(10,2) NOT NULL,
  estoque INT DEFAULT 0,
  marca VARCHAR(100),
  categoria VARCHAR(50),
  data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  imagem VARCHAR(255)
);

INSERT INTO tab_produto (id, nome, descricao, preco, estoque, marca, categoria, data_cadastro, imagem) VALUES
(1, 'Pomada Modeladora', 'Efeito Matte de alta fixação', 45.90, 20, 'Barba de Respeito', 'Estilização', '2026-05-07 19:28:21', '/img/photo-1599305090598-fe179d501227.jpeg'),
(2, 'Óleo Vikings', 'Hidratação profunda para fios rebeldes', 32.00, 15, 'Vikings', 'Cuidado Facial', '2026-05-07 19:28:21', 'https://images.unsplash.com/photo-1626015561531-77ec3411190c?w=600&q=80'),
(3, 'Shampoo Ice', 'Sensação refrescante e limpeza profunda', 28.50, 10, 'QOD Barber Shop', 'Limpeza', '2026-05-07 19:28:21', '/img/photo-1535585209827-a15fcdbc4c2d.jpeg')
ON CONFLICT (id) DO NOTHING;

-- 15. Tabela de Pedidos
CREATE TABLE IF NOT EXISTS tab_pedidos (
  id BIGSERIAL PRIMARY KEY,
  cliente_id BIGINT REFERENCES tab_cliente(id) ON DELETE SET NULL,
  nome_visitante VARCHAR(100),
  telefone_visitante VARCHAR(20),
  data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  forma_pagamento VARCHAR(50) DEFAULT 'Pagamento na Retirada',
  status VARCHAR(50) DEFAULT 'Aguardando Retirada',
  valor_total DECIMAL(10,2) NOT NULL
);

INSERT INTO tab_pedidos (id, cliente_id, nome_visitante, telefone_visitante, data_pedido, forma_pagamento, status, valor_total) VALUES
(1, NULL, 'Carlos Silva', '(11) 99999-9999', '2026-06-16 17:46:15', 'Pagamento na Retirada', 'Retirado', 32.00),
(2, NULL, 'João Paulo Silva', '111111111111', '2026-06-16 18:07:16', 'PIX', 'Aguardando Retirada', 28.50),
(3, NULL, 'João Paulo Silva', '111111111111', '2026-06-16 18:12:23', 'PIX', 'Cancelado', 28.50),
(4, NULL, 'João Paulo Silva', '111111111111', '2026-06-16 18:16:21', 'PIX', 'Retirado', 45.90)
ON CONFLICT (id) DO NOTHING;

-- 16. Tabela de Itens de Pedido
CREATE TABLE IF NOT EXISTS tab_itens_pedido (
  id BIGSERIAL PRIMARY KEY,
  pedido_id BIGINT NOT NULL REFERENCES tab_pedidos(id) ON DELETE CASCADE,
  produto_id BIGINT NOT NULL,
  nome_produto VARCHAR(150) NOT NULL,
  quantidade INT NOT NULL,
  preco_unitario DECIMAL(10,2) NOT NULL,
  preco_total_item DECIMAL(10,2) NOT NULL
);

INSERT INTO tab_itens_pedido (id, pedido_id, produto_id, nome_produto, quantidade, preco_unitario, preco_total_item) VALUES
(1, 1, 2, 'Óleo Vikings', 1, 32.00, 0.00),
(2, 4, 1, 'Pomada Modeladora', 1, 45.90, 45.90)
ON CONFLICT (id) DO NOTHING;

-- 17. Tabela de Configurações do Sistema
CREATE TABLE IF NOT EXISTS tab_setting (
  id BIGSERIAL PRIMARY KEY,
  chave VARCHAR(255) NOT NULL UNIQUE,
  valor TEXT
);

INSERT INTO tab_setting (id, chave, valor) VALUES
(1, 'nome', 'Tgo''s Barbearia'),
(2, 'descricao', 'Tradição e estilo para o homem moderno. Cuidamos do seu visual com excelência, usando as melhores técnicas e produtos para você se sentir incrível.'),
(3, 'endereco_curto', 'Rua Principal, 321 - Centro'),
(4, 'endereco_completo', 'Rua Principal, 321, Centro\nCidade, Estado - CEP 12345-678'),
(5, 'telefone', '(11) 11111-1111'),
(6, 'email', 'contato@tgosbarbearia.com'),
(7, 'horarios', 'Segunda a Sábado\nDas 09:00 às 19:00'),
(8, 'sobre_titulo', 'Tradição e Estilo'),
(9, 'sobre_texto1', 'Fundada com o propósito de resgatar a clássica experiência de ir ao barbeiro, nossa Barbearia une o ambiente nostálgico com as mais modernas técnicas de visagismo masculino.'),
(10, 'sobre_texto2', 'Acreditamos que um bom corte de cabelo e uma barba bem feita são fundamentais para a autoestima do homem contemporâneo. Aqui, cada cliente é tratado como um amigo. Sente-se, tome um café ou uma cerveja gelada e deixe o visual por nossa conta.'),
(11, 'sobre_imagem', 'https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800&q=80'),
(12, 'logo_alt', 'Fatec-FV'),
(13, 'logo_url', '/img/logo.png')
ON CONFLICT (id) DO NOTHING;

-- ========================================================
-- Sincronização de Sequências / Auto-Increment no PostgreSQL
-- ========================================================
SELECT setval(pg_get_serial_sequence('tab_perfil', 'id'), COALESCE((SELECT MAX(id) FROM tab_perfil), 1));
SELECT setval(pg_get_serial_sequence('tab_permissao', 'id'), COALESCE((SELECT MAX(id) FROM tab_permissao), 1));
SELECT setval(pg_get_serial_sequence('permissoes', 'id'), COALESCE((SELECT MAX(id) FROM permissoes), 1));
SELECT setval(pg_get_serial_sequence('tab_usuario', 'id'), COALESCE((SELECT MAX(id) FROM tab_usuario), 1));
SELECT setval(pg_get_serial_sequence('tab_endereco', 'id'), COALESCE((SELECT MAX(id) FROM tab_endereco), 1));
SELECT setval(pg_get_serial_sequence('tab_dias_funcionamento', 'id'), COALESCE((SELECT MAX(id) FROM tab_dias_funcionamento), 1));
SELECT setval(pg_get_serial_sequence('tab_grade_horarios', 'id'), COALESCE((SELECT MAX(id) FROM tab_grade_horarios), 1));
SELECT setval(pg_get_serial_sequence('tab_servico', 'id'), COALESCE((SELECT MAX(id) FROM tab_servico), 1));
SELECT setval(pg_get_serial_sequence('tab_produto', 'id'), COALESCE((SELECT MAX(id) FROM tab_produto), 1));
SELECT setval(pg_get_serial_sequence('tab_agendamento', 'id'), COALESCE((SELECT MAX(id) FROM tab_agendamento), 1));
SELECT setval(pg_get_serial_sequence('tab_pedidos', 'id'), COALESCE((SELECT MAX(id) FROM tab_pedidos), 1));
SELECT setval(pg_get_serial_sequence('tab_itens_pedido', 'id'), COALESCE((SELECT MAX(id) FROM tab_itens_pedido), 1));
SELECT setval(pg_get_serial_sequence('tab_setting', 'id'), COALESCE((SELECT MAX(id) FROM tab_setting), 1));
