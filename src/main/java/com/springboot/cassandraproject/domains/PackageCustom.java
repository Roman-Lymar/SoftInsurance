package com.springboot.cassandraproject.domains;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("package_custom")
@Schema(description = "Details about the custom package.")
public class PackageCustom extends PackageTemplate{
}
