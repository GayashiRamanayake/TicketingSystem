package org.ticketingSystem;

import org.json.JSONObject;
import java.io.*;
import java.util.Scanner;

public class Main {
   public static void main(String[] args) {
       Scanner scanner = new Scanner(System.in);
       JSONObject jsonConfig = new JSONObject();
       Configuration config = null;

       System.out.println("Welcome to the Ticketing System!");
       System.out.print("Do you want to load configuration from a file? (yes/no): ");
       String loadConfig = scanner.next();

       if ("yes".equalsIgnoreCase(loadConfig)) {
           try (FileReader reader = new FileReader("config.json")) {
               int i;
               StringBuilder jsonString = new StringBuilder();
               while ((i = reader.read()) != -1) {
                   jsonString.append((char) i);
               }
               jsonConfig = new JSONObject(jsonString.toString());
               
               // Create Configuration object from JSON
               config = new Configuration();
               config.maxTicketCapacity = jsonConfig.getInt("maxTicketCapacity");
               config.totalTickets = jsonConfig.getInt("totalTickets");
               config.ticketReleaseRate = jsonConfig.getInt("ticketReleaseRate");
               config.customerRetrievalRate = jsonConfig.getInt("customerRetrievalRate");
               config.numberOfVendors = jsonConfig.getInt("numberOfVendors");
               config.numberOfCustomers = jsonConfig.getInt("numberOfCustomers");
               
               // Validate loaded configuration
               boolean isValid = true;
               StringBuilder errorMessage = new StringBuilder("Configuration validation failed:\n");
               
               if (config.maxTicketCapacity <= 0) {
                   errorMessage.append("- Maximum ticket capacity must be positive\n");
                   isValid = false;
               }
               if (config.totalTickets <= 0) {
                   errorMessage.append("- Total tickets must be positive\n");
                   isValid = false;
               }
               if (config.totalTickets > config.maxTicketCapacity) {
                   errorMessage.append("- Total tickets cannot exceed maximum capacity\n");
                   isValid = false;
               }
               if (config.ticketReleaseRate <= 0) {
                   config.ticketReleaseRate = 2000;
                   System.out.println("Warning: Using default ticket release rate (2000ms)");
               }
               if (config.customerRetrievalRate <= 0) {
                   config.customerRetrievalRate = 1000;
                   System.out.println("Warning: Using default customer retrieval rate (1000ms)");
               }
               if (config.numberOfVendors <= 0) {
                   errorMessage.append("- Number of vendors must be positive\n");
                   isValid = false;
               }
               if (config.numberOfVendors > config.totalTickets) {
                   errorMessage.append("- Number of vendors cannot exceed total tickets\n");
                   isValid = false;
               }
               if (config.numberOfCustomers <= 0) {
                   errorMessage.append("- Number of customers must be positive\n");
                   isValid = false;
               }

               if (!isValid) {
                   System.out.println(errorMessage.toString());
                   System.out.println("Using manual configuration instead.");
                   config = null;
               } else {
                   System.out.println("Configuration loaded successfully from config.json");
               }
           } catch (IOException e) {
               System.out.println("Failed to load configuration file: " + e.getMessage());
               System.out.println("Using manual configuration instead.");
               config = null;
           } catch (Exception e) {
               System.out.println("Error processing configuration file: " + e.getMessage());
               System.out.println("Using manual configuration instead.");
               config = null;
           }
       }

       if (config == null) {
           config = new Configuration();

           // Input validation for maximum ticket capacity
           while (true) {
               System.out.print("Enter maximum ticket capacity: ");
               try {
                   config.maxTicketCapacity = Integer.parseInt(scanner.next());
                   if (config.maxTicketCapacity <= 0) {
                       System.out.println("Error: Maximum ticket capacity must be positive.");
                       continue;
                   }
                   break;
               } catch (NumberFormatException e) {
                   System.out.println("Error: Please enter a valid number.");
               }
           }

           // Input validation for total tickets
           while (true) {
               System.out.print("Enter total number of tickets: ");
               try {
                   config.totalTickets = Integer.parseInt(scanner.next());
                   if (config.totalTickets <= 0) {
                       System.out.println("Error: Total tickets must be positive.");
                       continue;
                   }
                   if (config.totalTickets > config.maxTicketCapacity) {
                       System.out.println("Error: Total tickets cannot exceed maximum capacity (" + config.maxTicketCapacity + ").");
                       continue;
                   }
                   break;
               } catch (NumberFormatException e) {
                   System.out.println("Error: Please enter a valid number.");
               }
           }

           // Input validation for ticket release rate
           while (true) {
               System.out.print("Enter ticket release rate (ms) (default 2000): ");
               try {
                   config.ticketReleaseRate = Integer.parseInt(scanner.next());
                   if (config.ticketReleaseRate <= 0) {
                       System.out.println("Using default value: 2000ms");
                       config.ticketReleaseRate = 2000;
                   }
                   break;
               } catch (NumberFormatException e) {
                   System.out.println("Error: Please enter a valid number.");
               }
           }

           // Input validation for customer retrieval rate
           while (true) {
               System.out.print("Enter customer retrieval rate (ms) (default 1000): ");
               try {
                   config.customerRetrievalRate = Integer.parseInt(scanner.next());
                   if (config.customerRetrievalRate <= 0) {
                       System.out.println("Using default value: 1000ms");
                       config.customerRetrievalRate = 1000;
                   }
                   break;
               } catch (NumberFormatException e) {
                   System.out.println("Error: Please enter a valid number.");
               }
           }

           // Input validation for number of vendors
           while (true) {
               System.out.print("Enter number of vendors: ");
               try {
                   config.numberOfVendors = Integer.parseInt(scanner.next());
                   if (config.numberOfVendors <= 0) {
                       System.out.println("Error: Number of vendors must be positive.");
                       continue;
                   }
                   if (config.numberOfVendors > config.totalTickets) {
                       System.out.println("Error: Number of vendors cannot exceed total tickets.");
                       continue;
                   }
                   break;
               } catch (NumberFormatException e) {
                   System.out.println("Error: Please enter a valid number.");
               }
           }

           // Input validation for number of customers
           while (true) {
               System.out.print("Enter number of customers: ");
               try {
                   config.numberOfCustomers = Integer.parseInt(scanner.next());
                   if (config.numberOfCustomers <= 0) {
                       System.out.println("Error: Number of customers must be positive.");
                       continue;
                   }
                   break;
               } catch (NumberFormatException e) {
                   System.out.println("Error: Please enter a valid number.");
               }
           }

           jsonConfig.put("maxTicketCapacity", config.maxTicketCapacity);
           jsonConfig.put("totalTickets", config.totalTickets);
           jsonConfig.put("ticketReleaseRate", config.ticketReleaseRate);
           jsonConfig.put("customerRetrievalRate", config.customerRetrievalRate);
           jsonConfig.put("numberOfVendors", config.numberOfVendors);
           jsonConfig.put("numberOfCustomers", config.numberOfCustomers);

           // Append configuration data to the JSON file
           appendConfigToFile(jsonConfig);

           System.out.println("Configuration saved to config.json");
       }

       // Wait for user to start the simulation
       System.out.println("\nConfiguration complete!");
       System.out.println("Summary of configuration:");
       System.out.println("- Maximum ticket capacity: " + config.maxTicketCapacity);
       System.out.println("- Total tickets: " + config.totalTickets);
       System.out.println("- Number of vendors: " + config.numberOfVendors);
       System.out.println("- Number of customers: " + config.numberOfCustomers);
       System.out.println("- Ticket release rate: " + config.ticketReleaseRate + "ms");
       System.out.println("- Customer retrieval rate: " + config.customerRetrievalRate + "ms");
       
       while (true) {
           System.out.print("\nType 'start' to begin the simulation: ");
           String startCommand = scanner.next().trim().toLowerCase();
           if ("start".equals(startCommand)) {
               break;
           } else {
               System.out.println("Invalid command. Please type 'start' to begin.");
           }
       }

       System.out.println("\nStarting simulation...\n");

       TicketPool ticketPool = new TicketPool(config.maxTicketCapacity);

       // Calculate tickets per vendor
       int ticketsPerVendor = config.totalTickets / config.numberOfVendors;

       // Create and store vendor threads
       Vendor[] vendors = new Vendor[config.numberOfVendors];
       Thread[] vendorThreads = new Thread[config.numberOfVendors];
       for (int i = 0; i < config.numberOfVendors; i++) {
           vendors[i] = new Vendor(ticketPool, "TicketType" + (i + 1), ticketsPerVendor, config.ticketReleaseRate);
           vendorThreads[i] = new Thread(vendors[i]);
           vendorThreads[i].start();
       }

       // Create and store customer threads
       Customer[] customers = new Customer[config.numberOfCustomers];
       Thread[] customerThreads = new Thread[config.numberOfCustomers];
       for (int i = 0; i < config.numberOfCustomers; i++) {
           customers[i] = new Customer(ticketPool, config.customerRetrievalRate);
           customerThreads[i] = new Thread(customers[i]);
           customerThreads[i].start();
       }

       System.out.println("Type 'stop' to stop the system...");

       // Stop the system when user types 'stop'
       while (true) {
           String input = scanner.next();
           if ("stop".equalsIgnoreCase(input)) {
               break;
           }
       }

       // Stop all vendor threads
       for (Vendor vendor : vendors) {
           vendor.stop();
       }
       for (Thread thread : vendorThreads) {
           thread.interrupt();
       }

       // Stop all customer threads
       for (Customer customer : customers) {
           customer.stop();
       }
       for (Thread thread : customerThreads) {
           thread.interrupt();
       }

       scanner.close();
       System.out.println("System stopped.");
   }

   private static void appendConfigToFile(JSONObject config) {
       try (FileWriter writer = new FileWriter("config.json", true)) {
           writer.write(config.toString(4) + "\n"); // Use 4 spaces for pretty printing and append a new line
           System.out.println("Configuration appended to config.json");
       } catch (IOException e) {
           System.out.println("Failed to append configuration.");
           e.printStackTrace();
       }
   }
}
