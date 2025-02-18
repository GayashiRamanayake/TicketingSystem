package org.ticketingSystem;

import java.util.LinkedList;
import java.util.Queue;

class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int capacity;

    public TicketPool(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void addTickets(String ticket, int count) {
        try {
            while (tickets.size() + count > capacity) {
                System.out.println("Ticket pool full. Vendor is waiting...");
                wait();
            }
            for (int i = 0; i < count; i++) {
                tickets.add(ticket + "-" + (tickets.size() + 1));
            }
            System.out.println(count + " tickets added. Total tickets: " + tickets.size());
            notifyAll();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor interrupted.");
        }
    }

    public synchronized String removeTicket() {
        try {
            while (tickets.isEmpty()) {
                System.out.println("No tickets available. Customer is waiting...");
                wait();
            }
            String ticket = tickets.poll();
            System.out.println("Ticket " + ticket + " purchased. Remaining tickets: " + tickets.size());
            notifyAll();
            return ticket;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Customer interrupted.");
            return null;
        }
    }
}
