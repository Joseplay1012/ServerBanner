package net.joseplay.ServerBanner.utils;

public class UnicodeFontReplace {
    public static String allianceFontReplace(String message) {
        String message1 = message.replace("&", "§");
        String colorCodePattern = "§[0-9a-fk-or]";

        StringBuilder newMessage = new StringBuilder();

        String[] parts = message1.split("(?=" + colorCodePattern + ")|(?<=" + colorCodePattern + ")");

        for (String part : parts) {
            if (part.matches(colorCodePattern)) {
                newMessage.append(part);
            } else {
                part = part.replaceAll("(?i)a", "ᴀ")
                        .replaceAll("(?i)b", "ʙ")
                        .replaceAll("(?i)c", "ᴄ")
                        .replaceAll("(?i)d", "ᴅ")
                        .replaceAll("(?i)e", "ᴇ")
                        .replaceAll("(?i)f", "ꜰ")
                        .replaceAll("(?i)g", "ɢ")
                        .replaceAll("(?i)h", "ʜ")
                        .replaceAll("(?i)i", "ɪ")
                        .replaceAll("(?i)j", "ᴊ")
                        .replaceAll("(?i)k", "ᴋ")
                        .replaceAll("(?i)l", "ʟ")
                        .replaceAll("(?i)m", "ᴍ")
                        .replaceAll("(?i)n", "ɴ")
                        .replaceAll("(?i)o", "ᴏ")
                        .replaceAll("(?i)p", "ᴘ")
                        .replaceAll("(?i)q", "ǫ")
                        .replaceAll("(?i)r", "ʀ")
                        .replaceAll("(?i)s", "ꜱ")
                        .replaceAll("(?i)t", "ᴛ")
                        .replaceAll("(?i)u", "ᴜ")
                        .replaceAll("(?i)v", "ᴠ")
                        .replaceAll("(?i)w", "ᴡ")
                        .replaceAll("(?i)x", "x")
                        .replaceAll("(?i)y", "ʏ")
                        .replaceAll("(?i)z", "ᴢ");
                newMessage.append(part);
            }
        }

        return newMessage.toString();
    }

    public static String allianceFontUnReplace(String message) {
        // Substituindo caracteres Unicode específicos por suas versões normais
        return message
                .replace("ᴀ", "a")
                .replace("ʙ", "b")
                .replace("ᴄ", "c")
                .replace("ᴅ", "d")
                .replace("ᴇ", "e")
                .replace("ꜰ", "f")
                .replace("ɢ", "g")
                .replace("ʜ", "h")
                .replace("ɪ", "i")
                .replace("ᴊ", "j")
                .replace("ᴋ", "k")
                .replace("ʟ", "l")
                .replace("ᴍ", "m")
                .replace("ɴ", "n")
                .replace("ᴏ", "o")
                .replace("ᴘ", "p")
                .replace("ǫ", "q")
                .replace("ʀ", "r")
                .replace("ꜱ", "s")
                .replace("ᴛ", "t")
                .replace("ᴜ", "u")
                .replace("ᴠ", "v")
                .replace("ᴡ", "w")
                .replace("x", "x")
                .replace("ʏ", "y")
                .replace("ᴢ", "z");
    }
}
