package giannasi_InventorySystem;

public class Product {
	private String SKU;
	private int quantity;
	private double price;
	private String description;
	
	public Product (String SKU, int quant, double price, String desc) {
		this.SKU=SKU;
		this.quantity=quant;
		this.price=price;
		this.description=desc;
	}

	public String getSKU() {
		return SKU;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int newQuantity) {
		this.quantity = newQuantity;
	}

	public double getPrice() {
		return price;
	}

	public String getDescription() {
		return description;
	}
}
