// ez itt egy nagyon egyszeru "el-e" vegpont
package hu.szakdolgozat.azonositas.vezerlo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EloEController {

    // GET http://localhost:8081/api/elo  -> "ok"
    @GetMapping("/api/elo")
    public String elo() {
        return "ok";
    }
}
