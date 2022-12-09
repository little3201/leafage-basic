/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.hypervisor.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for Role
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "role")
public class Role extends SuperDocument<String> {

}
