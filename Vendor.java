package org.ticketingSystem;

import java.util.Random;

class Vendor implements Runnable {
    private final TicketPool pool;
    private final String ticketType;
    private final int ticketsPerBatch;
    private final int releaseInterval;
    private volatile boolean running = true;

    public Vendor(TicketPool pool, String ticketType, int ticketsPerBatch, int releaseInterval) {
        this.pool = pool;
        this.ticketType = ticketType;
        this.ticketsPerBatch = ticketsPerBatch;
        this.releaseInterval = releaseInterval;
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            while (running) {
                Thread.sleep(releaseInterval + random.nextInt(500)); // Add random jitter to delay
                pool.addTickets(ticketType, ticketsPerBatch);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor stopped.");
        }
    }
}
