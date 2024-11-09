package kickstart.Inventory;

public class Calendar extends Product{
	public Calendar(String name, int price, String description){
		super(name, price, description);
		// should ID be in the Product class or here?
		// if in product class, then book will have it -> replace it for ISBN or differentiate them?
	}


}
