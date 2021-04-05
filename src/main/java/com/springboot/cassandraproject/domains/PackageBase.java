package com.springboot.cassandraproject.domains;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("package_base")
@Schema(description = "Details about the base package.")
public class PackageBase extends PackageTemplate{
}
