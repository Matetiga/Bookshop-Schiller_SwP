package orders;

import jakarta.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InventoryController {
	@Autowired
	private CrudRepository<Order, Id> orderRepository;


	@PostMapping("/orders")
	private String addOrder(@RequestParam("order") Order order){
		orderRepository.save(order);
		return "orders";
	}

	@GetMapping("/orders")
	private String orders(){
		//model.addAttribute("stock", orderRepository.findAll());
		//orderRepository.findById(id).getNam
		return "orders";
	}
}
