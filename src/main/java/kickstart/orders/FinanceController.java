package kickstart.orders;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Month;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class FinanceController {


	private final MyOrderManagement myOrderManagement;
	private final MyOrderRepository myOrderRepository;

	public FinanceController(MyOrderManagement myOrderManagement, MyOrderRepository myOrderRepository) {
		this.myOrderManagement = myOrderManagement;
		this.myOrderRepository = myOrderRepository;
	}

	@GetMapping("/finance-data")
	@ResponseBody
	public Map<String, Double> getMonthlyRevenueData() {

		// Einnahmen nach Monaten
		Map<String, Double> revenueData = new LinkedHashMap<>();
		revenueData.put("August", 420.75);
		revenueData.put("September", 344.87);
		revenueData.put("Oktober", 556.98);
		revenueData.put("November", 690.56);
		revenueData.put("Dezember", myOrderManagement.getTotalOfMonth(Month.DECEMBER, myOrderRepository.findAll()));

		return revenueData;
	}

	@GetMapping("/finance-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String financeOverview(Model model) {
		return "finance-overview";
	}

}
