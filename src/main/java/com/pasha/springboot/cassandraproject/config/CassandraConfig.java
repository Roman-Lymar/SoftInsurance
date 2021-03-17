package com.pasha.springboot.cassandraproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories(basePackages = "com.pasha.springboot.cassandraproject.repositories")
public class CassandraConfig extends AbstractCassandraConfiguration {

    public static final String KEYSPACE = "simple_keyspace";

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE;
    }
}
