package kickstart.orders;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
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
	public Map<String, Double> MonthlyRevenueData(@RequestParam(value = "amount", defaultValue = "12") int amount) {
		Month month = LocalDateTime.now().getMonth();
//		String year = Integer.toString(LocalDateTime.now().getYear());

		Map<String, Double> revenueData = new LinkedHashMap<>();
		for (int i = amount - 1 ; i >= 0; i--) {
			revenueData.put(Month.of(month.getValue() - i).toString(), myOrderManagement.getTotalOfMonth(Month.of(month.getValue() - i), myOrderRepository.findAll()));
		}
		return revenueData;
	}

	@GetMapping("/finance-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String financeOverview(Model model) {
		return "finance-overview";
	}

}
