package io.leafage.basic.assets.constants;

/**
 * desc
 *
 * @author liwenqiang 2022/5/31 9:03
 **/
public enum StatisticsFieldEnum {

    VIEWED("viewed"),
    LIKES("likes"),
    COMMENTS("comments"),
    DOWNLOADS("downloads");

    public final String value;

    StatisticsFieldEnum(String value) {
        this.value = value;
    }
}
