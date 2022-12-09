/*
 * Copyright (c) 2021. Leafage All Right Reserved.
 */
package io.leafage.basic.assets.document;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model class for posts
 *
 * @author liwenqiang 2020-10-06 22:09
 */
@Document(collection = "posts")
public class Posts extends SuperDocument {

}
