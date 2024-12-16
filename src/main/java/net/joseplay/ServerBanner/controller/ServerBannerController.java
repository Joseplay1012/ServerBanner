package net.joseplay.ServerBanner.controller;

import net.joseplay.ServerBanner.api.minecraft.MinecraftUtils;
import net.joseplay.ServerBanner.api.minecraft.MotdType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
class ServerBannerController {

    @GetMapping("/api/serverbanner.png")
    public ResponseEntity<byte[]> generateBanner(
            @RequestParam("ip") String ip,
            @RequestParam(required = false, name = "port") String port
    ) {
        try {

            // Retornando a imagem como resposta
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "image/png");

            return new ResponseEntity<>(new MinecraftUtils().gereStatusImage(ip, port, MotdType.RAW), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
