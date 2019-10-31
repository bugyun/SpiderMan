package vip.ruoyun.spiderman.tracker.model;

/**
 * Created by ruoyun on 2019-10-31.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:界面的信息
 */
public class PageInfo {

    //时间戳
    private String operateTime;

    //页面id screenName,包的全路径 track_screen_name
    private String pageId;

    //通过注解获取
    private String pageName;

    private String pageSession;

    // 操作结束时间
    private String endTime;

    public PageInfo() {
        operateTime = String.valueOf(System.currentTimeMillis());
    }

    public String getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(final String operateTime) {
        this.operateTime = operateTime;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(final String pageId) {
        this.pageId = pageId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(final String pageName) {
        this.pageName = pageName;
    }

    public String getPageSession() {
        return pageSession;
    }

    public void setPageSession(final String pageSession) {
        this.pageSession = pageSession;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(final String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "PageInfo{" +
                "operateTime='" + operateTime + '\'' +
                ", pageId='" + pageId + '\'' +
                ", pageName='" + pageName + '\'' +
                ", pageSession='" + pageSession + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
