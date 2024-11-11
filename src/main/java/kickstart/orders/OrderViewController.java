package kickstart.orders;

import jakarta.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderViewController {

	@PostMapping("/orderview")
	private String addOrder(@RequestParam("orders") Order order){
		return "orderview";
	}

	@GetMapping("/orderview")
	private String orders(){
		return "orderview";
	}
}
