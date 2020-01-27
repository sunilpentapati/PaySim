package org.paysim.actors;

import org.paysim.parameters.Parameters;
import org.paysim.utils.BoundedArrayDeque;

import java.util.*;

public abstract class SuperActor {
    protected static Deque<Client> prevInteractions;
    protected final Parameters parameters;
    private final String id;
    private String name;
    private boolean isFraud = false;
    double balance = 0;
    double overdraftLimit;
    private Map<String, String> properties;

    public enum Type {
        BANK,
        CLIENT,
        FRAUDSTER,
        MERCHANT,
        MULE
    }

    SuperActor(String id, String name, Parameters parameters) {
        this.id = id;
        this.name = name;
        this.parameters = parameters;
        this.properties = new HashMap<>();
        prevInteractions = new BoundedArrayDeque<>(100);
    }

    void deposit(double amount) {
        balance += amount;
    }

    boolean withdraw(double amount) {
        boolean unauthorizedOverdraft = false;

        if (balance - amount < overdraftLimit) {
            unauthorizedOverdraft = true;
        } else {
            balance -= amount;
        }

        return unauthorizedOverdraft;
    }

    boolean isFraud() {
        return isFraud;
    }

    void setFraud(boolean isFraud) {
        this.isFraud = isFraud;
    }

    protected double getBalance() {
        return balance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void rememberClient(Client client) {
        prevInteractions.push(client);
    }

    public List<Client> getRecentClients() {
        return Arrays.asList(prevInteractions.toArray(new Client[prevInteractions.size()]));
    }

    public abstract Type getType();

    public Map<String, String> getProperties() {
        return properties;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }

    public String setProperty(String key, String value) {
        return properties.put(key, value);
    }

    @Override
    public String toString() {
        return String.format("%s [%s]", id, getType());
    }
}
