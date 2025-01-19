package kickstart.orders;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
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

	/**
	 * Gets Total for each month and puts it in a HashMap with month
	 * @return revenueData
	 */
	@GetMapping("/finance-data")
	@ResponseBody
	public Map<String, Double> monthlyRevenueData() {
		Month currentMonth = LocalDateTime.now().getMonth();
		Map<String, Double> revenueData = new LinkedHashMap<>();
		for (int i = 11; i >= 0; i--) {
			Month dataMonth;
			if(currentMonth.getValue() - i != 0){
				dataMonth = Month.of((currentMonth.getValue() - i + 12) % 12);
			}else{
				dataMonth = Month.of(12);
			}

			if(dataMonth.getValue() <= LocalDateTime.now().getMonth().getValue()){
				revenueData.put(dataMonth.toString(), myOrderManagement.getTotalOfMonthAndYear(dataMonth,
					LocalDateTime.now().getYear(), myOrderRepository.findAll()));
			}else{
				revenueData.put(dataMonth.toString(), myOrderManagement.getTotalOfMonthAndYear(dataMonth,
					LocalDateTime.now().getYear() - 1, myOrderRepository.findAll()));
			}
		}
		System.out.println(revenueData);
		return revenueData;
	}

	/**
	 *
	 * @param model
	 * @return finance-overview (html)
	 */
	@GetMapping("/finance-overview")
	@PreAuthorize("hasRole('ADMIN')")
	String financeOverview(Model model) {
		return "finance-overview";
	}

}
