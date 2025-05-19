package qrcodeapi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.springframework.http.MediaType;


@RestController
public class TaskController {

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getImage(
            @RequestParam(name = "size") int size,
            @RequestParam(name = "type") String type) {
        if (size < 150 || size > 350) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Image size must be between 150 and 350 pixels\"}");
        }

        if (!type.equalsIgnoreCase("png") && !type.equalsIgnoreCase("jpeg") && !type.equalsIgnoreCase("gif")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Only png, jpeg and gif image types are supported"));
        }

        try {
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = image.createGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, size, size);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, type.toLowerCase(), baos);
            byte[] bytes = baos.toByteArray();

            MediaType imageFormatSelected = switch (type.toLowerCase()) {
                case "png" -> MediaType.IMAGE_PNG;
                case "jpeg" -> MediaType.IMAGE_JPEG;
                case "gif" -> MediaType.IMAGE_GIF;
                default -> MediaType.IMAGE_PNG;
            };

            return ResponseEntity
                    .ok()
                    .contentType(imageFormatSelected)
                    .body(bytes);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

