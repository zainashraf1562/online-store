package com.pluralsight;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Starter code for the Online Store workshop.
 * Students will complete the TODO sections to make the program work.
 */
public class Store {

    public static void main(String[] args) {

        // Create lists for inventory and the shopping cart
        ArrayList<Product> inventory = new ArrayList<>();
        ArrayList<Product> cart = new ArrayList<>();

        // Load inventory from the data file (pipe-delimited: id|name|price)
        loadInventory("products.csv", inventory);

        // Main menu loop
        Scanner scanner = new Scanner(System.in);
        int choice = -1;
        while (choice != 3) {
            System.out.println("\nWelcome to the Online Store!");
            System.out.println("1. Show Products");
            System.out.println("2. Show Cart");
            System.out.println("3. Exit");
            System.out.print("Your choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter 1, 2, or 3.");
                scanner.nextLine();                 // discard bad input
                continue;
            }
            choice = scanner.nextInt();
            scanner.nextLine();                     // clear newline

            switch (choice) {
                case 1 -> displayProducts(inventory, cart, scanner);
                case 2 -> displayCart(cart, scanner);
                case 3 -> System.out.println("Thank you for shopping with us!");
                default -> System.out.println("Invalid choice!");
            }
        }
        scanner.close();
    }

    /**
     * Reads product data from a file and populates the inventory list.
     * File format (pipe-delimited):
     * id|name|price
     * <p>
     * Example line:
     * A17|Wireless Mouse|19.99
     */
    public static void loadInventory(String fileName, ArrayList<Product> inventory) {
        try(BufferedReader reader = new BufferedReader((new FileReader(fileName)))){
            String input = reader.readLine();
            while((input = reader.readLine()) != null){
                String[] token = input.split("\\|");
                String sku = token[0];
                String name = token[1];
                double price = Double.parseDouble(token[2]);
                String department = token[3];
                inventory.add(new Product(sku, name, price, department));
            }
        }
        catch(IOException ex){
            System.err.println("Error Reading File");
        }
    }

    /**
     * Displays all products and lets the user add one to the cart.
     * Typing X returns to the main menu.
     */
    public static void displayProducts(ArrayList<Product> inventory,
                                       ArrayList<Product> cart,
                                       Scanner scanner) {
        for(Product product : inventory){
            System.out.println(product.displayStringCart());
        }

        System.out.println("Enter Product Id: ");
        System.out.println("(X) Return to Main Menu");
        String id = scanner.nextLine();

        if(id.equalsIgnoreCase("X")) return;

        Product selected = findProductById(id, inventory);
        if(selected != null){
            cart.add(selected);
            System.out.println("Product Added");
        }
    }

    /**
     * Shows the contents of the cart, calculates the total,
     * and offers the option to check out.
     */
    public static void displayCart(ArrayList<Product> cart, Scanner scanner) {
        double totalPrice = 0;
        System.out.println("Your cart: ");

        for(Product product : cart){
            System.out.println(product.displayStringCart());
            totalPrice += product.getPrice();
        }

        System.out.println("(C) CheckOut: ");
        System.out.println("(X) Return: ");
        String userInput = scanner.nextLine();
        if(userInput.equalsIgnoreCase("C")){
            checkOut(cart, totalPrice, scanner);
            return;
        }
        System.out.println("MENU");

    }

    /**
     * Handles the checkout process:
     * 1. Confirm that the user wants to buy.
     * 2. Accept payment and calculate change.
     * 3. Display a simple receipt.
     * 4. Clear the cart.
     */
    public static void checkOut(ArrayList<Product> cart,
                                double totalAmount,
                                Scanner scanner) {
        System.out.println("(Y) CheckOut");
        System.out.println("(R) Remove A Item");
        String userInput = scanner.nextLine();

        boolean exit = false;
        while(!exit){
            switch(userInput.toUpperCase()) {
                case "R":
                    removeItem(cart, scanner);
                    break;
                case "Y":
                    checkOut(cart, scanner, totalAmount);
                    exit = true;
                    break;
                default:
                    System.out.println("MENU");
                    exit = true;
            }
        }
    }

    /**
     * Searches a list for a product by its id.
     *
     * @return the matching Product, or null if not found
     */
    public static Product findProductById(String id, ArrayList<Product> inventory) {
        for(Product product : inventory){
            if(id.equalsIgnoreCase(product.getSku())){
                return product;
            }
        }
        System.out.println("Product Non-Existent");
        return null;
    }



    public static void checkOut(ArrayList<Product> cart, Scanner scanner, double totalAmount){
        System.out.println("Enter Payment.");
        double userPay = scanner.nextDouble();
        scanner.nextLine();
        double change = userPay - totalAmount;
        System.out.println("Items Bought: ");

        for (Product product : cart) {
            System.out.println(product.displayStringCart() + "\n");
        }

        if(change != 0){
            System.out.printf("Change: $%.2f%n", change);
            cart.clear();
        } else if (change < 0) {
            System.out.println("Not Enough Money!");
        }

    }
    public static void removeItem(ArrayList<Product> cart, Scanner scanner){
        System.out.println("Enter Id: ");
        System.out.println("(X) Return");
        String idInput = scanner.nextLine();
        if (idInput.equalsIgnoreCase("X")){
            return;
        }
        for (int i = 0; i < cart.size(); i++) {
            if (idInput.equalsIgnoreCase(cart.get(i).getSku())) {
                cart.remove(i);
                System.out.println("Removed!");
                return;
            }
        }
        System.out.println("Invalid");
    }

}
