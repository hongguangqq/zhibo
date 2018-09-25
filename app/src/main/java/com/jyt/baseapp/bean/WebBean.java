package com.jyt.baseapp.bean;

/**
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class WebBean {
    private int id;
    private String code;
    private String title;
    private int isActivity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(int isActivity) {
        this.isActivity = isActivity;
    }
}
