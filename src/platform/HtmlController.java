package platform;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/code")
public class HtmlController {

    private final CodeSnippetService service;

    @Autowired
    public HtmlController(CodeSnippetService service) {
        this.service = service;
    }

    @GetMapping("{uuid}")
    public String getCode(@PathVariable String uuid, Model model) {
        model.addAttribute("codeSnippet", service.findCodeSnippetById(uuid));
        return "code";
    }

    @GetMapping("/new")
    public String getNew() {
        return "new";
    }

    @GetMapping("/latest")
    public String getLatest(Model model) {
        model.addAttribute("list", service.findLatest());
        return "latest";
    }

}
