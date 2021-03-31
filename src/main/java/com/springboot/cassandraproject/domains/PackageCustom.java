package com.springboot.cassandraproject.domains;

import io.swagger.annotations.ApiModel;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("package_custom")
@ApiModel(description = "Details about the custom package.")
public class PackageCustom extends PackageTemplate{
}
