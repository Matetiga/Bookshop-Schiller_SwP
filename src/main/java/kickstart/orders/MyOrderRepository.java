package kickstart.orders;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyOrderRepository extends CrudRepository<MyOrder, MyOrder.OrderIdentifier> {
}
