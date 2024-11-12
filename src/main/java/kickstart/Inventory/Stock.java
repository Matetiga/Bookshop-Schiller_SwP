//package kickstart.Inventory;
//import org.salespointframework.inventory.Inventory;
//
//public class Stock extends Inventory<ShopProduct> {
//
//}
//No se que querias hacer con esto, por ahora lo desabilite


// Preguntas
// is this class needed
// or is the Inventory initializer responsible for keeping track of the current stock
// // how is it made in Videoshop
// why does videoshop use : private final UniqueInventory<UniqueInventoryItem> inventory;
// and not InventoryTtem
// inventoryItem associates a product with its quantity
// UniqueInventoryItem associates a product with its quantity and a unique identifier

// TODO
// this class us not necessaty
// the inventory controller should be the one to create the inventory
// that means that the inventory initializer should store the items in tis inventory attribute
// like in the videoshop example

// TODO, separate the genre from the merch and calendar,
// they should beother classes