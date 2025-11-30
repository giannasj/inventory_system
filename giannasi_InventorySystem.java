package giannasi_InventorySystem;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.PrintWriter;
import java.io.FileOutputStream;

public class giannasi_InventorySystem {

	public static void main(String[] args) {
		ArrayList<Product> inventory = new ArrayList<>(); //inventory to store products
		//reading file
		try {
			Scanner fileScanner = new Scanner(new File("inventory.csv"));
			if (fileScanner.hasNextLine()) fileScanner.nextLine(); //skipping headers
			
			while (fileScanner.hasNextLine()) { 
				String line = fileScanner.nextLine();
				String[] parts = line.split(",");//splitting row into string list
				
				//creating new product for each row, adding to inventory
				inventory.add(new Product(parts[0],Integer.parseInt(parts[1]),Double.parseDouble(parts[2]),parts[3])); 
			}
			fileScanner.close();
		} catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
		
		System.out.println("Available Products:");
		
		int counter = 0; //numbers products
		for (Product product : inventory) {
			System.out.println(++counter+". "+product.getSKU()+" | "+product.getDescription()+" | $"+product.getPrice()+" | Stock: "+product.getQuantity());
		}
		
		Scanner sin = new Scanner(System.in);
		ArrayList<String[]> cart = new ArrayList<>(); //stores relevant info about products (quantity, description, total price) for the receipt
		
		while(true) {
			
			String input = "";
			Product desiredProduct=null;
			boolean validSKU = false;
			int quantity = -1;
			boolean validQuan = false;
			
			while(!validSKU) { //SKU validation
				System.out.print("Enter SKU to purchase (or \'done\' to checkout): ");
				input = sin.nextLine().trim();
				if (input.equals("done")) break; //exits SKU validation
				for (Product product:inventory) {
					if (input.equals(product.getSKU())) {
						validSKU = true;
						desiredProduct=product; //store desired product to easily access price, description, quantity, etc.
						break; //exit for-loop
					}
				}
				if (!validSKU) { //if no match was found in the previous for-loop, this code will be reached
					System.out.println("SKU not found. Please enter a valid SKU (e.g. \'SKU-001\')");
				}	
			}
			
			if (input.equals("done")) break; //exits purchasing loop if user is done
			
			while(!validQuan) { //quantity validation
				System.out.print("Enter quantity: ");
				try {
					quantity = sin.nextInt();
					if (quantity>desiredProduct.getQuantity()) { //checks if inventory is sufficient to supply the order
						System.out.println("Invalid input. Desired quantity exceeds current inventory.");
					} else {
						if (quantity>0) { //validates positive quantities
							validQuan = true;
						} else { //invalidates negative/zero quantities
							System.out.println("Invalid input. Please enter a positive integer.");
						}
					}
				} catch(InputMismatchException e) { //reached if sin.nextInt() fails (non-integer input)
					System.out.println("Invalid input. Please enter a positive integer.");
				} finally {
					sin.nextLine();
				}
			}
			
			desiredProduct.setQuantity(desiredProduct.getQuantity()-quantity); //updates quantity of product
			String[] x = {String.valueOf(quantity),desiredProduct.getDescription(),String.valueOf(desiredProduct.getPrice()*quantity)};
			cart.add(x); //creates string with relevant product information (quantity, description, total price) and adds it to the cart
			System.out.println("Added to cart: "+quantity+"x "+desiredProduct.getDescription()+" @ $"+desiredProduct.getPrice()+" each");
		}
		sin.close();
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("inventory.csv"));
			writer.println("SKU,Quantity,Price,Description"); //writes headers
			for (Product product:inventory) { //writes data for each product in the inventory
				writer.println(product.getSKU()+","+product.getQuantity()+","+product.getPrice()+","+product.getDescription());
			}
			writer.close();
			System.out.println("\nSuccessfully updated inventory");
		} catch (FileNotFoundException e) {
			System.out.println("\nError writing to file.");
		}
		
		double total = 0;
		System.out.println("\n=====RECEIPT=====");
		for (String[] item:cart) {
			total+=Double.parseDouble(item[2]); //adds cost of item to the total
			System.out.println(item[0]+"x "+item[1]+" - $"+item[2]); //prints quantity, description, and price
		}
		total=((int)(total*100))/(double)100; //truncates to two decimal places
		System.out.println("\nSubtotal: $"+total);
		double taxTotal = 0.07*total; //calculates sales tax
		taxTotal = ((int)(taxTotal*100))/(double)100; //truncates to two decimal places
		System.out.println("Sales Tax (7%): $"+taxTotal);
		System.out.println("\nTotal: $"+(total+taxTotal));
		
	}
}
