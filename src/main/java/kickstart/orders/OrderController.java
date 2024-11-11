package kickstart.orders;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class OrderController {


	@GetMapping("/cart")
	String cart() {
		return "cart";
	}


}
