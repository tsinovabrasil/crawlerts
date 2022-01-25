## ARQUIVO DE CONFIGURAÇÃO

O arquivo default de entrada do plugin é crawlerts.json, mas é possível passar o caminho do arquivo como argumento também, ex.: --file-config=config/crawlerts.json

O arquivo tem esse padrão:

```

{
  "output": {
    "logstash": {
      "port": 5045,
      "host": "51.159.25.73"
    }
  },
  "beat": {
    "name": "wirelessts",
    "version": "1.0.0",
    "tags": ["Cubee"],
    "clientName": "IPSM",
    "clientId": 6    
  },
  "devices": [
    {
      "appendFields": {
        "unit": {
          "location": "Prédio 576",
          "name": "IPSM - BH"
        },
        "OriginLocation": {          
          "lat": "",
          "lon": ""
        },
        "responsible_name": "Marco Tulio",
        "sector_name": "TI"
      },
      "interval": 300,
      "host": "http://192.168.50.248",
      "username": "admin",
      "password": "tlp@hbh",
      "manufacturer": "Alvarion",
      "category": "Wireless"
    }
  ]
}

```

## COMO FAZER MELHORIAS NO CÓDIGO

- Clone o projeto e abra-o com o Netbeans
- Crie uma nova branch para atuar em um hotfix ou em uma nova feature, ex: feature/enviar-metricas-email
- Se tiver tudo certo, abra uma PR pra master e solicite a revisão e aprovação

## COMO CRIAR UMA RELEASE PARA LIBERAR EM PRODUÇÃO

- Compile o projeto usando o Netbeans
- Uma pasta com o nome dist/ será gerada com 2 arquivos: crawlerts.jar e lib. Adicione a essa pasta outros três arquivos que estão na raiz do projeto: crawlerts.json, crawlerts.service e start
- Compacta a pasta dist com o nome crawlerts-v{versao}.zip. Esse arquivo deve estar dentro da pasta dist pois não deve ser enviado ao github.
- Faça o commit e push se tiver alguma atualização pra subir
- Crie uma tag e indique a versão, ex.: git tag -a crawlerts-v1.0.0 -m "Versão inicial do plugin"
- Dê push na tag, ex.: git push origin crawlerts-v1.0.0
- Crie uma release no github: selecione a tag criada anteriormente, adicione uma descrição e anexe o arquivo zip criado
- Pronto, uma nova release está pronta para ser baixada e instalada. Ex.: https://github.com/tsinovabrasil/crawlerts/releases/tag/crawlerts-v1.0.0

## COMO INSTALAR NO LINUX

Existem dois arquivos importantes:

crawlerts.service: Descreve as caracteristicas do serviço como seu nome, nome do usuário, local de instalação e arquivo de execução.

```
[Unit]
Description=Crawlerts
[Service]
User=root
WorkingDirectory=/etc/elastic/crawlerts/dist
ExecStart=/etc/elastic/crawlerts/dist/start
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target
```

start: Comando para iniciar/executar o plugin

```
#!/bin/sh
java -jar crawlerts.jar server config.yml
```

Para instalar, devemos seguir 5 etapas:

- Baixe a pasta dist e entre dentro dela
- Atualize o arquivo crawlerts.service com o nome, nome do usuário e local de instalação
- sudo cp crawlerts.service /etc/systemd/system/crawlerts.service
- sudo /bin/systemctl daemon-reload
- sudo /bin/systemctl enable crawlerts.service

Para iniciar, execute o comando abaixo:

- sudo systemctl start crawlerts.service

Para pausar:

- sudo systemctl stop crawlerts.service

Para verificar o status:

- sudo systemctl status crawlerts.service