package cn.edu.njnu.geoproblemsolving.Entity;

public class EmailEntity {

    private String recipient;  //xxx@xx.xxx
    private String mailTitle;
    private String mailContent;

    public String getMailContent() {
        return mailContent;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}
