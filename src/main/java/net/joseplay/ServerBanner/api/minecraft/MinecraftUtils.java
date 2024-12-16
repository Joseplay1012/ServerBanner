package net.joseplay.ServerBanner.api.minecraft;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.joseplay.ServerBanner.utils.UnicodeFontReplace.allianceFontUnReplace;

public class MinecraftUtils {
    public final String API_URL = "https://api.mcsrvstat.us/3/";

    public ServerInfo getServerInfo(String ip, String port, MotdType motdType) {
        try {
            URL url = new URL(API_URL + ip);

            if (port != null){
                url = new URL(API_URL + ip + ":" + port);
            }

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                JsonObject jsonResponse = new Gson().fromJson(reader, JsonObject.class);

                ServerInfo serverInfo = new ServerInfo();

                if (ip.contains("bedrock.")) {
                    ip = ip.replace("bedrock.", "");
                }

                serverInfo.setIp(ip);

                if (!jsonResponse.getAsJsonObject("debug").get("ping").getAsBoolean()){
                    JsonArray motd = new JsonArray();
                    motd.add("§cServer UnKnow...");
                    serverInfo.setMotd(motd);
                    serverInfo.setOnlinePlayers(0);
                    serverInfo.setMaxPlayers(0);
                    serverInfo.setLatency(0);
                    serverInfo.setIcon(null);
                    serverInfo.setPlayerList(null);

                    return serverInfo;
                }

                switch (motdType){
                    case RAW:
                        serverInfo.setMotd(jsonResponse.getAsJsonObject("motd").get("raw").getAsJsonArray());
                        break;
                    case CLEAN:
                        serverInfo.setMotd(jsonResponse.getAsJsonObject("motd").get("clean").getAsJsonArray());
                        break;
                    case HTML:
                        serverInfo.setMotd(jsonResponse.getAsJsonObject("motd").get("html").getAsJsonArray());
                }


                serverInfo.setOnlinePlayers(jsonResponse.getAsJsonObject("players").get("online").getAsInt());
                serverInfo.setMaxPlayers(jsonResponse.getAsJsonObject("players").get("max").getAsInt());

                //serverInfo.setLatency(getWebsiteLatency(ip, port));

                //Verifica se tem a player list
                if (jsonResponse.getAsJsonObject("players").get("list") != null) {
                    serverInfo.setPlayerList(jsonResponse.getAsJsonObject("players").get("list").getAsJsonArray());
                }


                // Decodifica o ícone do servidor
                if (jsonResponse.get("icon") != null) {

                    String iconBase64 = jsonResponse.get("icon").getAsString().split(",")[1];
                    byte[] iconBytes = Base64.getDecoder().decode(iconBase64);
                    serverInfo.setIcon(iconBytes);
                }

                return serverInfo;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public byte[] gereStatusImage(String ip, String port, MotdType motdType) {
        byte[] imageBytes;
        File bgFile = new File("assets/minecraft/serverutils/pngs/serverlistbg.png");  // Arquivo de fundo
        File pingFile = new File("assets/minecraft/serverutils/pngs/serverlistping.png"); //Arquivo de ping
        File unKnowIconFile = new File("assets/minecraft/serverutils/pngs/serverlistunknownicon.png");

        try {
            // Obter informações do servidor usando a API do MCStats
            ServerInfo serverInfo = getServerInfo(ip, port, motdType);
            if (serverInfo == null) {
                return null;
            }

            //Verifica se tem a imagem de fundo, caso não ele pega a padrao
            if (!bgFile.exists()) {

                bgFile.getParentFile().mkdirs();

                try (InputStream inputStream = getClass().getResourceAsStream("/assets/minecraft/pngs/serverlistbg.png")) {
                    if (inputStream != null) {
                        Files.copy(inputStream, bgFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("O arquivo serverlistbg.png não foi encontrado nos recursos.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Verifica se tem a imagem de ping
            if (!pingFile.exists()) {

                pingFile.getParentFile().mkdirs();

                try (InputStream inputStream = getClass().getResourceAsStream("/assets/minecraft/pngs/serverlistping.png")) {
                    if (inputStream != null) {
                        Files.copy(inputStream, pingFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("O arquivo serverlistping.png não foi encontrado nos recursos.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //Verifica se tem a imagem de unknow
            if (!unKnowIconFile.exists()) {

                unKnowIconFile.getParentFile().mkdirs();

                try (InputStream inputStream = getClass().getResourceAsStream("/assets/minecraft/pngs/serverlistunknownicon.png")) {
                    if (inputStream != null) {
                        Files.copy(inputStream, unKnowIconFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("O arquivo serverlistunknownicon.png não foi encontrado nos recursos.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Carregar o fundo da imagem
            BufferedImage bgImage = ImageIO.read(bgFile);
            BufferedImage image = new BufferedImage(1848, 270, BufferedImage.TYPE_INT_ARGB);

            File fontFile = new File("assets/minecraft/serverutils/fonts/serverlistfont.ttf"); // Caminho do arquivo TTF
            Font customFont;
            Font emojiFont = new Font("Segoe UI Emoji", Font.PLAIN, 50); // Fonte para emojis

            //Verifica se a fonte existe
            if (!fontFile.exists()) {

                fontFile.getParentFile().mkdirs();

                try (InputStream inputStream = getClass().getResourceAsStream("/assets/minecraft/fonts/serverlistfont.ttf")) {
                    if (inputStream != null) {
                        Files.copy(inputStream, fontFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } else {
                        System.out.println("O arquivo serverlistfont.png não foi encontrado nos recursos.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(50f);
            } catch (FontFormatException e) {
                customFont = new Font("Segoe UI", Font.PLAIN, 50);
            }

            // Criar um Graphics2D para desenhar sobre a imagem
            Graphics2D g = image.createGraphics();

            // Desenhar o fundo carregado
            g.drawImage(bgImage, 0, 0, 1848, 270, null);


            //Desenha icon
            if (serverInfo.getIcon() != null) {
                BufferedImage serverIcon = ImageIO.read(new ByteArrayInputStream(serverInfo.getIcon()));
                g.drawImage(serverIcon, 10, 10, 256, 256, null);
            } else {
                BufferedImage unKnowServerIcon = ImageIO.read(unKnowIconFile);
                g.drawImage(unKnowServerIcon, 10, 10, 256, 256, null);
            }

            //Desenha o ping
            if (pingFile.exists()) {
                BufferedImage pingImage = ImageIO.read(pingFile);
                g.drawImage(pingImage, 1780, 5, 50, 50, null);

            }

            // Adicionar texto com informações do servidor
            g.setFont(customFont);
            g.setColor(Color.WHITE);
            drawColoredText(g, "§f" + serverInfo.getIp(), 300, 50);
            //drawColoredText(g, serverInfo.getLatency() + "§ams", 600, 80);
            drawColoredText(g, "§8" + serverInfo.getOnlinePlayers() + " §7/ §8" + serverInfo.getMaxPlayers(), 1400, 50);


            int offSet = 150;
            int height = 300;
            for (JsonElement s : serverInfo.getMotd()) {
                drawColoredText(g, allianceFontUnReplace(s.getAsString().replace("§r", "")), height, offSet);
                System.out.println(s.getAsString());
                offSet = offSet + 60;
            }

            // Finalizar a renderização
            g.dispose();

            // Salvar a imagem gerada em um ByteArrayOutputStream
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            imageBytes = baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return imageBytes;
    }

    public void drawColoredText(Graphics2D g2d, String text, int x, int y) {
        // Regex para capturar códigos de cores, estilos e texto
        String regex = "(\\&#([A-Fa-f0-9]{6})|§#([A-Fa-f0-9]{6})|§x(§[A-Fa-f0-9]){6}|§([0-9a-fA-Fk-oK-OrR]))?([^§&]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        int currentX = x;
        Color defaultColor = g2d.getColor(); // Cor padrão inicial
        Color currentColor = defaultColor;  // Cor atual
        Font defaultFont = g2d.getFont();   // Fonte padrão inicial
        boolean bold = false, italic = false, underlined = false, strikethrough = false, obfuscated = false;

        while (matcher.find()) {
            String hexColor1 = matcher.group(2); // &#rrggbb
            String hexColor2 = matcher.group(3); // §#rrggbb
            String hexColorVanilla = matcher.group(4); // §x§r§r§g§g§b§b
            String mcCode = matcher.group(5);    // Códigos do Minecraft
            String segmentText = matcher.group(6); // Texto sem códigos

            // Resetar para padrão ao encontrar §r
            if ("r".equalsIgnoreCase(mcCode)) {
                currentColor = defaultColor;
                bold = italic = underlined = strikethrough = obfuscated = false;
                g2d.setColor(currentColor);
                g2d.setFont(defaultFont);
                continue;
            }

            // Aplicar cor
            if (hexColor1 != null) {
                currentColor = Color.decode("#" + hexColor1);
                g2d.setColor(currentColor);
            } else if (hexColor2 != null) {
                currentColor = Color.decode("#" + hexColor2);
                g2d.setColor(currentColor);
            } else if (hexColorVanilla != null) {
                String hex = hexColorVanilla.replace("§x", "").replace("§", "");
                currentColor = Color.decode("#" + hex);
                g2d.setColor(currentColor);
            } else if (mcCode != null && mcCode.matches("[0-9a-fA-F]")) {
                currentColor = mapMinecraftColor(mcCode);
                g2d.setColor(currentColor);
            }

            // Aplicar estilos
            if (mcCode != null) {
                switch (mcCode.toLowerCase()) {
                    case "l": bold = true; break;        // Negrito
                    case "o": italic = true; break;      // Itálico
                    case "n": underlined = true; break;  // Sublinhado
                    case "m": strikethrough = true; break; // Tachado
                    case "k": obfuscated = true; break;  // Obfuscado
                }
            }

            // Configurar a fonte com base nos estilos
            Font font = defaultFont;
            if (bold && italic) font = defaultFont.deriveFont(Font.BOLD | Font.ITALIC);
            else if (bold) font = defaultFont.deriveFont(Font.BOLD);
            else if (italic) font = defaultFont.deriveFont(Font.ITALIC);
            g2d.setFont(font);

            // Substituir emojis do Minecraft
            if (segmentText != null) {
                segmentText = replaceMinecraftEmojis(segmentText); // Aqui você pode incluir kaomojis
            }

            // Desenhar texto com estilos adicionais
            if (segmentText != null && !segmentText.isEmpty()) {
                if (obfuscated) segmentText = obfuscateText(segmentText);

                g2d.drawString(segmentText, currentX, y);

                // Aplicar sublinhado ou tachado manualmente
                if (underlined || strikethrough) {
                    int textWidth = g2d.getFontMetrics().stringWidth(segmentText);
                    int lineY = y + 1; // Ajuste fino para a linha
                    if (underlined) g2d.drawLine(currentX, lineY, currentX + textWidth, lineY);
                    if (strikethrough) g2d.drawLine(currentX, y - g2d.getFontMetrics().getHeight() / 3, currentX + textWidth, y - g2d.getFontMetrics().getHeight() / 3);
                }

                currentX += g2d.getFontMetrics().stringWidth(segmentText); // Atualizar posição do texto
            }
        }
    }

    // Substituir emojis personalizados do Minecraft
    private String replaceMinecraftEmojis(String text) {
        return text
                .replace(":music:", "♫")
                .replace(":sparkle:", "✧")
                .replace(":happy:", "(ﾉ◕ヮ◕)ﾉ *:･ﾟ✧");
    }

    // Metodo para mapear códigos de cores do Minecraft para valores RGB
    private Color mapMinecraftColor(String mcColorCode) {
        switch (mcColorCode.toLowerCase()) {
            case "0": return new Color(0, 0, 0);       // Preto
            case "1": return new Color(0, 0, 170);     // Azul escuro
            case "2": return new Color(0, 170, 0);     // Verde escuro
            case "3": return new Color(0, 170, 170);   // Aqua
            case "4": return new Color(170, 0, 0);     // Vermelho escuro
            case "5": return new Color(170, 0, 170);   // Roxo
            case "6": return new Color(255, 170, 0);   // Dourado
            case "7": return new Color(170, 170, 170); // Cinza claro
            case "8": return new Color(85, 85, 85);    // Cinza escuro
            case "9": return new Color(85, 85, 255);   // Azul
            case "a": return new Color(85, 255, 85);   // Verde claro
            case "b": return new Color(85, 255, 255);  // Aqua claro
            case "c": return new Color(255, 85, 85);   // Vermelho claro
            case "d": return new Color(255, 85, 255);  // Magenta
            case "e": return new Color(255, 255, 85);  // Amarelo
            case "f": return new Color(255, 255, 255); // Branco
            default: return Color.WHITE; // Cor padrão (branco)
        }
    }

    // Metodo para aplicar obfuscation
    private String obfuscateText(String text) {
        StringBuilder obfuscated = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isWhitespace(c)) {
                obfuscated.append(c); // Preserva espaços
            } else {
                obfuscated.append((char) ('a' + Math.random() * 26)); // Gera caracteres aleatórios
            }
        }
        return obfuscated.toString();
    }

    public static long getWebsiteLatency(String ip, int port) {
            try (Socket socket = new Socket()) {
            long startTime = System.currentTimeMillis(); // Marca o tempo de início
            socket.connect(new InetSocketAddress(ip, port), 5000); // Conecta ao host e porta com timeout
            long endTime = System.currentTimeMillis(); // Marca o tempo de término
            return endTime - startTime; // Retorna a latência em milissegundos
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Se ocorrer uma exceção, considera como offline
        }
    }

    public String convertTimestamp(String timestamp) {
        // Parse o timestamp para ZonedDateTime
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(timestamp);

        // Converte para a zona de horário brasileira
        LocalDateTime localDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("America/Sao_Paulo")).toLocalDateTime();

        // Define o formato de saída
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Retorna o resultado formatado
        return localDateTime.format(formatter);
    }
}