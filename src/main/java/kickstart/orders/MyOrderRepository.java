package kickstart.orders;

import org.salespointframework.order.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MyOrderRepository extends CrudRepository<MyOrder, Order.OrderIdentifier> {
}
