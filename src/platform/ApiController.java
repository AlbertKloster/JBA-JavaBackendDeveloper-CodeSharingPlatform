package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
public class ApiController {

    private final CodeSnippetService service;

    @Autowired
    public ApiController(CodeSnippetService service) {
        this.service = service;
    }

    @GetMapping("/{uuid}")
    public Object getCode(@PathVariable String uuid) {
        CodeSnippet codeSnippet = service.findCodeSnippetById(uuid);
        if (codeSnippet.getViews() > 0)
            codeSnippet.setViews(codeSnippet.getViews() - 1); // see CodeSnippetService.deleteIfViewLimit
        return codeSnippet;
    }

    @GetMapping("/latest")
    public Object getCode() {
        return service.findLatest();
    }

    @PostMapping("/new")
    public Object postCode(@RequestBody CodeSnippet codeSnippet) {
        return service.save(codeSnippet);
    }

}
