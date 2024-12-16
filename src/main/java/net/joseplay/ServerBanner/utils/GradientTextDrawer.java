package net.joseplay.ServerBanner.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.TextComponent;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;

public class GradientTextDrawer {

    // Método para criar uma mensagem com MiniMessage
    public static Component createGradientMessage(String message) {
        MiniMessage mm = MiniMessage.miniMessage();
        return mm.deserialize(message);  // Não há necessidade de converter, pois MiniMessage lida com as formatações
    }

    // Método para desenhar a mensagem no Graphics2D
    public static void drawGradientMessage(Graphics2D g2d, String message, int x, int y) {
        // Crie o componente com MiniMessage
        Component component = createGradientMessage(message);

        // Renderiza o componente (texto formatado) no Graphics2D
        renderComponent(g2d, component, x, y);
    }

    // Método para renderizar o texto formatado no Graphics2D
    private static void renderComponent(Graphics2D g2d, Component component, int x, int y) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Usamos um layout para medir o texto
        FontRenderContext frc = g2d.getFontRenderContext();
        TextLayout layout = new TextLayout(component.toString(), g2d.getFont(), frc);

        // O texto será dividido conforme a formatação de cores
        if (component instanceof TextComponent) {
            TextComponent textComponent = (TextComponent) component;
            String text = textComponent.content(); // Pega o conteúdo da string
            int currentX = x;

            // Divide a mensagem em partes formatadas
            for (int i = 0; i < text.length(); i++) {
                char c = text.charAt(i);
                String colorTag = parseColor(c);  // Aqui você pode mapear as cores corretamente

                // Aplicar a cor
                g2d.setColor(getColorFromTag(colorTag));

                // Desenhar o texto
                String part = String.valueOf(c);  // Cada caractere
                g2d.drawString(part, currentX, y);

                // Atualiza a posição X
                currentX += g2d.getFontMetrics().stringWidth(part);
            }
        }
    }

    // Método para mapear as tags de cor
    private static String parseColor(char c) {
        switch (c) {
            case '0': return "#000000";  // Preto
            case '1': return "#0000AA";  // Azul
            case '2': return "#00AA00";  // Verde
            case '3': return "#00AAAA";  // Aqua
            case '4': return "#AA0000";  // Vermelho
            case '5': return "#AA00AA";  // Roxo
            case '6': return "#FFAA00";  // Dourado
            case '7': return "#AAAAAA";  // Cinza Claro
            case '8': return "#555555";  // Cinza Escuro
            case '9': return "#5555FF";  // Azul Claro
            case 'a': return "#55FF55";  // Verde Claro
            case 'b': return "#55FFFF";  // Aqua Claro
            case 'c': return "#FF5555";  // Vermelho Claro
            case 'd': return "#FF55FF";  // Magenta
            case 'e': return "#FFFF55";  // Amarelo
            case 'f': return "#FFFFFF";  // Branco
            default: return "#FFFFFF";  // Cor padrão (branco)
        }
    }

    // Método para obter a cor a partir de uma tag de cor
    private static Color getColorFromTag(String colorTag) {
        try {
            return Color.decode(colorTag);
        } catch (NumberFormatException e) {
            return Color.WHITE;  // Fallback para branco caso o código seja inválido
        }
    }
}