package kickstart.orders;

import org.salespointframework.order.Order;
import org.springframework.data.repository.CrudRepository;

public interface MyOrderRepository extends CrudRepository<MyOrder, Order.OrderIdentifier> {
}
