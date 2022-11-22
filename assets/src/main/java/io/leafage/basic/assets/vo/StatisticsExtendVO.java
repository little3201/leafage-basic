package io.leafage.basic.assets.vo;

import java.io.Serial;
import java.time.LocalDate;

/**
 * everyday statistics vo
 *
 * @author liwenqiang 2022/5/25 19:53
 */
public class StatisticsExtendVO extends StatisticsVO {

    @Serial
    private static final long serialVersionUID = 4288475041155960116L;

    /**
     * 统计日期
     */
    private LocalDate date;
    /**
     * 浏览量环比
     */
    private double overViewed;
    /**
     * 点赞量环比
     */
    private double overLikes;
    /**
     * 评论量环比
     */
    private double overComments;
    /**
     * 下载量环比
     */
    private double overDownloads;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getOverViewed() {
        return overViewed;
    }

    public void setOverViewed(double overViewed) {
        this.overViewed = overViewed;
    }

    public double getOverLikes() {
        return overLikes;
    }

    public void setOverLikes(double overLikes) {
        this.overLikes = overLikes;
    }

    public double getOverComments() {
        return overComments;
    }

    public void setOverComments(double overComments) {
        this.overComments = overComments;
    }

    public double getOverDownloads() {
        return overDownloads;
    }

    public void setOverDownloads(double overDownloads) {
        this.overDownloads = overDownloads;
    }
}
