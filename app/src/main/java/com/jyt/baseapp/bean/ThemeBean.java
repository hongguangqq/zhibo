package com.jyt.baseapp.bean;

import java.util.List;

/**
 * @author LinWei on 2018/5/29 14:45
 */
public class ThemeBean  {
    private List<ThemeBeanDta> content;

    public class ThemeBeanDta{
        private int id;
        private String title;
        private String content;
        private int isActivity;
        private String imgs;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getIsActivity() {
            return isActivity;
        }

        public void setIsActivity(int isActivity) {
            this.isActivity = isActivity;
        }

        public void setImgs(String imgs){
            this.imgs = imgs;
        }

        public String getImgs(){
            return imgs;
        }

    }

    public List<ThemeBeanDta> getContent(){
        return content;
    }
}
