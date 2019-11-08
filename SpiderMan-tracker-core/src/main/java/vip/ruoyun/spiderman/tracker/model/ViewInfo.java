package vip.ruoyun.spiderman.tracker.model;

import java.util.HashMap;

/**
 * Created by ruoyun on 2019-10-31.
 * Author:若云
 * Mail:zyhdvlp@gmail.com
 * Depiction:
 */
public class ViewInfo {

    //按钮路径
    private String viewPath;

    //事件类型
    private String eventType;

    //时间戳
    private String operateTime;

    //页面id screenName
    private String pageId;

    //页面的标题
    private String pageName;

    //元素（按钮）内容
    private String content;

    //事件数据
    private Object trackData;

    //
    private String pageSession;

    //
    private String elementId;

    private HashMap<String, String> propertys;

    public ViewInfo() {
        operateTime = String.valueOf(System.currentTimeMillis());
    }

    public String getViewPath() {
        return viewPath;
    }

    public void setViewPath(final String viewPath) {
        this.viewPath = viewPath;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(final String eventType) {
        this.eventType = eventType;
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

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public Object getTrackData() {
        return trackData;
    }

    public void setTrackData(final Object trackData) {
        this.trackData = trackData;
    }

    public String getPageSession() {
        return pageSession;
    }

    public void setPageSession(final String pageSession) {
        this.pageSession = pageSession;
    }

    public String getElementId() {
        return elementId;
    }

    public void setElementId(final String elementId) {
        this.elementId = elementId;
    }

    public HashMap<String, String> getPropertys() {
        return propertys;
    }

    public void setPropertys(final HashMap<String, String> propertys) {
        this.propertys = propertys;
    }

    @Override
    public String toString() {
        return "ViewInfo{" +
                "viewPath='" + viewPath + '\'' +
                ", eventType='" + eventType + '\'' +
                ", operateTime='" + operateTime + '\'' +
                ", pageId='" + pageId + '\'' +
                ", pageName='" + pageName + '\'' +
                ", content='" + content + '\'' +
                ", trackData=" + trackData +
                ", pageSession='" + pageSession + '\'' +
                ", elementId='" + elementId + '\'' +
                ", propertys=" + propertys +
                '}';
    }
}
