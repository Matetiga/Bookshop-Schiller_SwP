package kickstart.Inventory;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;

public interface BookCatalog extends Catalog<ShopProduct> {

	Streamable<ShopProduct> findByType(ShopProduct.ProductType type, Sort sort);
	//Implementar catalogo, que nos de la funcinalidad del framework para poder iterar a traves de los libros. Eso
	//en torno nos hara el trabajo mas facil a la hora de quitar y poner cosas en el inventario y ya de ahi seguir
}
