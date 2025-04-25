package com.sparta.demo.config;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {

    private final List<String> routingReplicas;
    private final AtomicInteger index;

    public RoutingDataSource(List<String> routingReplicas) {
        this.routingReplicas = routingReplicas;
        index = new AtomicInteger(0);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            System.out.println("routing DataSource Slave");
            return routingReplicas.get(index.getAndIncrement() % routingReplicas.size());
        } else {
            System.out.println("source");
            return "source";
        }
    }

}
