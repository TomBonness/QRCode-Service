package qrcodeapi;
import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.MediaType;


@RestController
public class TaskController {

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }


    @GetMapping("/api/qrcode")
    public ResponseEntity<?> getImage(
            @RequestParam(name = "contents") String contents,
            @RequestParam(name = "size", defaultValue = "250") int size,
            @RequestParam(name = "type", defaultValue = "png") String type,
            @RequestParam(name = "correction", defaultValue = "L") String correction){

        if (contents == null || contents.trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Contents cannot be null or blank\"}");
        }

        if (size < 150 || size > 350) {
            return ResponseEntity
                    .badRequest()
                    .body("{\"error\": \"Image size must be between 150 and 350 pixels\"}");
        }

        if (!correction.equalsIgnoreCase("L") && !correction.equalsIgnoreCase("M") && !correction.equalsIgnoreCase("Q") && !correction.equalsIgnoreCase("H")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Permitted error correction levels are L, M, Q, H"));
        }

        if (!type.equalsIgnoreCase("png") && !type.equalsIgnoreCase("jpeg") && !type.equalsIgnoreCase("gif")) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Only png, jpeg and gif image types are supported"));
        }



        ErrorCorrectionLevel level;
        switch (correction.toUpperCase()) {
            case "L" -> level = ErrorCorrectionLevel.L;
            case "M" -> level = ErrorCorrectionLevel.M;
            case "Q" -> level = ErrorCorrectionLevel.Q;
            case "H" -> level = ErrorCorrectionLevel.H;
            default -> {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("error", "Permitted error correction levels are L, M, Q, H"));
            }
        }

        try {
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, ?> hints = Map.of(EncodeHintType.ERROR_CORRECTION, level);
            BitMatrix bitMatrix = writer.encode(contents, BarcodeFormat.QR_CODE, size, size, hints);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, type.toLowerCase(), baos);
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
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
    }
}
