package qrcodeapi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TaskController {

    @GetMapping("/api/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @GetMapping("/api/qrcode")
    public ResponseEntity<String> qrcode() {
        return new ResponseEntity<>("NOT IMPLEMENTED", HttpStatus.NOT_IMPLEMENTED);
    }

}
