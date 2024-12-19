```
# Minecraft Server Banner API

A **Minecraft Server Banner API** permite que você gere uma imagem PNG com informações sobre o estado de um servidor de Minecraft. A imagem contém detalhes sobre o servidor, como o número de jogadores online, o nome do servidor, e outros detalhes relevantes. Você pode usar essa API para criar banners personalizados que mostram o status do servidor de Minecraft de maneira visual.

## Endpoints

### Gerar Banner do Servidor

- **URL**: `http://localhost:8437/api/serverbanner.png?ip=<serverip>&port=<serverport>`
  
  - **Parâmetros**:
    - `ip` (obrigatório): O endereço IP do servidor Minecraft.
    - `port` (opcional): A porta do servidor Minecraft (caso não seja especificada, a porta padrão 25565 será utilizada).
  
  **Exemplo de URL**:
  
  Para obter a imagem de um servidor de Minecraft com o IP `123.45.67.89` e porta `25565`:
  ```
  http://localhost:8437/api/serverbanner.png?ip=123.45.67.89&port=25565
  ```

  Para obter a imagem de um servidor de Minecraft com o IP `play.mysrv.com` e a porta padrão:
  ```
  http://localhost:8437/api/serverbanner.png?ip=play.mysrv.com
  ```

### Resposta da API

A resposta da API será uma imagem PNG que pode ser incorporada diretamente no seu site ou app. A imagem gerada incluirá as seguintes informações sobre o servidor:

- Nome do servidor.
- Número de jogadores online.
- Motd

## Como Usar

1. **Faça uma requisição HTTP GET** para o endpoint desejado, incluindo o endereço IP do servidor Minecraft e, se necessário, a porta.
2. A resposta será uma imagem PNG com os detalhes do servidor Minecraft.

### Exemplo com `curl`:

```bash
curl "http://localhost:8437/api/serverbanner.png?ip=play.mysrv.com&port=25565" --output server_banner.png
```

Esse comando baixará a imagem do banner do servidor Minecraft e salvará como `server_banner.png`.

### Incorporar em uma página web:

Você pode embutir a imagem diretamente em uma página web usando uma tag `<img>`:

```html
<img src="http://localhost:8437/api/serverbanner.png?ip=play.mysrv.com" alt="Minecraft Server Banner">
```

## Requisitos

- **Java 8 ou superior** (caso seja necessário para rodar a aplicação).
- Servidor web que possa hospedar a API (por exemplo, Apache, Nginx).
- **Bibliotecas necessárias**: Nenhuma dependência externa é necessária, a aplicação é baseada apenas em recursos padrão do Java.

## Instalação

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/Joseplay1012/ServerBanner.git
   ```

2. **Entre no diretório**:

   ```bash
   cd ServerBanner
   ```

3. **Compilar e rodar o servidor**:

   Caso o projeto seja baseado em Java, você pode compilar e rodar o servidor com os seguintes comandos:

   ```bash
   mvn clean install
   mvn exec:java
   ```

   Ou se o projeto já for empacotado como um arquivo JAR:

   ```bash
   java -jar ServerBanner.jar
   ```

4. A API estará rodando localmente em `http://localhost:8437`.

## Contribuições

Contribuições são bem-vindas! Se você tiver melhorias, correções ou novas funcionalidades para adicionar, envie um pull request ou abra uma issue.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).
```
