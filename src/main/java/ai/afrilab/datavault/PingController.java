package ai.afrilab.datavault;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Health Check Controller")
public class PingController {
  @GetMapping
  public ResponseEntity<Map<String, String>> ping() {
    return ResponseEntity.ok(
        Map.of("message", "Pong")
    );
  }
}
