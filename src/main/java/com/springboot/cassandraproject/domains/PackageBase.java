package com.springboot.cassandraproject.domains;

import io.swagger.annotations.ApiModel;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("package_base")
@ApiModel(description = "Details about the base package.")
public class PackageBase extends PackageTemplate{
}
