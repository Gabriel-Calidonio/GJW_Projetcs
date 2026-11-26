# Implantação do binário via VPN

> Como já possuíamos o alias meu-vps configurado no arquivo ~/.ssh/config (já apontando para o usuário deploy, a chave /home/sickla/.ssh/id_ed25519 e o IP da VPN), podemos usar esse mesmo alias diretamente no scp em vez de deploy@<IP da VPN>:
bash
scp target/tgos-barbearia meu-vps:/tmp/tgos-barbearia
Use o código com cuidado.

Detalhes importantes

* Por que funciona: o scp lê as mesmas configurações de host, IP, porta e chave SSH definidas para o ssh em ~/.ssh/config.
* Onde executar: rode esse comando na sua máquina local (sickla@VPS), dentro da pasta onde o diretório target/ está localizado.
* Caminho de destino: o arquivo será salvo exatamente em /tmp/tgos-barbearia na VPS remota (srv2161876). Se quiser apenas indicar a pasta, meu-vps:/tmp/ também funciona.
