package pl.akademiaqa.bos;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RequestMapping("/")
@RestController
@RequiredArgsConstructor
public class RootController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> rootJson() {
        return Map.of("message", "BookstoreAPI is up and running");
    }
}
