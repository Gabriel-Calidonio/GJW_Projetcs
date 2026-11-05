-- 1. Configuração de Usuário (conforme application.properties)
CREATE USER IF NOT EXISTS 'desenvolvedor'@'%' IDENTIFIED BY 'b2#FbXPQTu4FYw';
GRANT ALL PRIVILEGES ON `gwj2`.* TO 'desenvolvedor'@'%';
FLUSH PRIVILEGES;