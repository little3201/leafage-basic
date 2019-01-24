package top.abeille.basic.common.enums;

/**
 * security scopes enum
 *
 * @author liwenqiang 2019/1/2 16:10
 **/
public enum AuthorityScopeEnum {

    READ("Read"),
    WRITE("Write"),
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
