## phpMyAdmin local acessando BD do VPS remoto

> Para conectar o seu phpMyAdmin local ao banco de dados do VPS remoto com segurança, o melhor método é criar um túnel SSH no seu computador e apontar a configuração do phpMyAdmin para uma porta local. Isso evita expor a porta do MySQL/MariaDB do servidor para a internet.

1. Criar o Túnel SSH no seu computador (Debian 13 local)

Abra o terminal do seu computador e rode o comando abaixo para encaminhar uma porta local (ex: 3307, para não conflitar com um banco local na 3306) até a porta 3306 do seu VPS:
```bash
ssh -N -L 3307:127.0.0.1:3306 -p 2244 deploy@10.10.0.1
```
* -N: Mantém o túnel aberto sem abrir um terminal remoto.
* -L 3307:127.0.0.1:3306: Redireciona a porta 3307 do seu computador para a porta 3306 interna do VPS.
* Deixe esse terminal aberto: Enquanto o comando estiver rodando, a conexão segura estará ativa.

2. Configurar o phpMyAdmin local

Agora, adicione o servidor remoto no arquivo de configuração do seu phpMyAdmin local (no Debian, geralmente fica em /etc/phpmyadmin/config.inc.php).
Abra o arquivo com privilégios de administrador:
```bash
sudo nano /etc/phpmyadmin/config.inc.php
```
Vá até o final do arquivo e adicione o bloco abaixo para criar uma nova opção de servidor na tela de login do phpMyAdmin:
```php
$i++;
$cfg['Servers'][$i]['verbose'] = 'VPS Remoto';
$cfg['Servers'][$i]['host'] = '127.0.0.1';
$cfg['Servers'][$i]['port'] = '3307';
$cfg['Servers'][$i]['connect_type'] = 'tcp';
$cfg['Servers'][$i]['auth_type'] = 'cookie';
```
* Atenção ao host: Use 127.0.0.1 em vez de localhost. No PHP, escrever localhost força a conexão via socket local e ignora a porta 3307 do túnel SSH.
* Tela de login: Salve o arquivo, acesse o phpMyAdmin no seu navegador, escolha VPS Remoto no seletor de servidor e entre com o usuário e senha do banco de dados do VPS.

3. Dica para automatizar o túnel (Opcional)

Para não precisar digitar aquele comando gigante toda vez, adicione o redirecionamento direto no seu arquivo ~/.ssh/config local:
```text
Host vps-db
    HostName 10.10.0.1
    Port 2244
    User deploy
    LocalForward 3307 127.0.0.1:3306
```
Com isso, basta rodar ssh -N vps-db no terminal sempre que quiser usar o phpMyAdmin com o banco remoto.
