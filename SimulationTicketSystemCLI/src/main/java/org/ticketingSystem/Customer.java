package org.ticketingSystem;

import java.util.Random;

class Customer implements Runnable {
    private final TicketPool pool;
    private final int retrievalRate;
    private volatile boolean running = true;

    public Customer(TicketPool pool, int retrievalRate) {
        this.pool = pool;
        this.retrievalRate = retrievalRate;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            while (running) {
                Thread.sleep(retrievalRate + random.nextInt(300)); // Add random jitter to delay
                pool.removeTicket();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Customer stopped.");
        }
    }
}
