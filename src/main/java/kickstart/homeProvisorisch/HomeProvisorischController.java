package kickstart.homeProvisorisch;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeProvisorischController {
	@GetMapping("/")
	public String index() {
		return "homeProvisorisch";
	}
}
