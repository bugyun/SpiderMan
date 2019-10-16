package vip.ruoyun.template.ext;

public class TemplateExt {

    private boolean isOpen;

    private boolean isLog;

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(final boolean open) {
        isOpen = open;
    }

    public boolean isLog() {
        return isLog;
    }

    public void setLog(final boolean log) {
        isLog = log;
    }

    @Override
    public String toString() {
        return "TemplateMode{" +
                "isOpen=" + isOpen +
                ", isLog=" + isLog +
                '}';
    }
}
