/*
 * Copyright (c) 2019. Abeille All Right Reserved.
 */
package top.abeille.basic.authority.enums;

/**
 * security scopes enum
 *
 * @author liwenqiang 2019/1/2 16:10
 **/
public enum AuthorityScopeEnum {

    /**
     * 只读
     */
    READ("Read"),
    /**
     * 只写
     */
    WRITE("Write"),
    /**
     * 读写
     */
    ALL("All");

    private String alias;

    AuthorityScopeEnum(String alias) {
        this.alias = alias;
    }

    public static AuthorityScopeEnum getScopeEnum(String alias) {
        for (AuthorityScopeEnum scopeEnum : AuthorityScopeEnum.values()) {
            if (alias.equalsIgnoreCase(scopeEnum.alias)) {
                return scopeEnum;
            }
        }
        return null;
    }

}
