/*
 * Copyright (c) 2024.  little3201.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.leafage.basic.hypervisor.domain;

import jakarta.persistence.*;

/**
 * <p>Authorities class.</p>
 *
 * @author wq li
 */
@Entity
@Table(name = "authorities")
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * username
     */
    @Column(name = "username", length = 50)
    private String username;

    /**
     * authority
     */
    @Column(name = "authority", length = 50)
    private String authority;


    /**
     * <p>Getter for the field <code>id</code>.</p>
     *
     * @return a {@link java.lang.Long} object
     */
    public Long getId() {
        return id;
    }

    /**
     * <p>Setter for the field <code>id</code>.</p>
     *
     * @param id a {@link java.lang.Long} object
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <p>Getter for the field <code>username</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getUsername() {
        return username;
    }

    /**
     * <p>Setter for the field <code>username</code>.</p>
     *
     * @param username a {@link java.lang.String} object
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * <p>Getter for the field <code>authority</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getAuthority() {
        return authority;
    }

    /**
     * <p>Setter for the field <code>authority</code>.</p>
     *
     * @param authority a {@link java.lang.String} object
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
