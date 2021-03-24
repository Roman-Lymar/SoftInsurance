package com.pasha.springboot.cassandraproject;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.Session;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Paths;

@SpringBootApplication
public class CassandraProjectApplication {

	public static void main(String[] args) {

		Session session = Cluster.builder()
				.withCloudSecureConnectBundle(new File("src/main/resources/secure-connect-database_name.zip"))
				.withAuthProvider(new PlainTextAuthProvider("wwUhLTYJQNokeTnQLzeCrEjC", "DxQLLkU_qrgSqzvwEaQ_SN12zSerFGeb32hJyo4PqMIlCrMgGEmzItY_pbgkpT5rSmhmO63_1J49Ufc4WkKm2uSYDL5gWrmtXJ+dgizNLIyYxxti6zj7WwS4lLldnQqY"))
				.withoutJMXReporting()
				.build()
				.connect("simple_keyspace");
//		Session session = CqlSession.builder()
//				.withCloudSecureConnectBundle(Paths.get("src/main/resources/secure-connect-softinsurance.zip"))
//				.withAuthCredentials("wwUhLTYJQNokeTnQLzeCrEjC",
//						"DxQLLkU_qrgSqzvwEaQ_SN12zSerFGeb32hJyo4PqMIlCrMgGEmzItY_pbgkpT5rSmhmO63_1J49Ufc4WkKm2uSYDL5gWrmtXJ+dgizNLIyYxxti6zj7WwS4lLldnQqY")
//				.withKeyspace("simple_keyspace")
//				.build())

		SpringApplication.run(CassandraProjectApplication.class, args);

		// Create the CqlSession object:
//		try (CqlSession session = CqlSession.builder()
//				.withCloudSecureConnectBundle(Paths.get("src/main/resources/secure-connect-softinsurance.zip"))
//				.withAuthCredentials("wwUhLTYJQNokeTnQLzeCrEjC",
//						"DxQLLkU_qrgSqzvwEaQ_SN12zSerFGeb32hJyo4PqMIlCrMgGEmzItY_pbgkpT5rSmhmO63_1J49Ufc4WkKm2uSYDL5gWrmtXJ+dgizNLIyYxxti6zj7WwS4lLldnQqY")
//				.withKeyspace("simple_keyspace")
//				.build()) {
//			// Select the release_version from the system.local table:
//			ResultSet rs = session.execute("select release_version from system.local");
//			Row row = rs.one();
//			//Print the results of the CQL query to the console:
//			if (row != null) {
//				System.out.println(row.getString("release_version"));
//			} else {
//				System.out.println("An error occurred.");
//			}
//		}
//		System.exit(0);
	}

}
