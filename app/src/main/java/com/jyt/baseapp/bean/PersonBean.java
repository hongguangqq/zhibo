package com.jyt.baseapp.bean;

import java.util.List;

/**
 * @author LinWei on 2018/6/6 16:55
 */
public class PersonBean {

    private UserBean user;
    private List<GiftData> giftCounts;
    private List<UserBean> recent;
    private boolean isFllow;

    public UserBean getUser() {
        return user;
    }

    public List<GiftData> getGiftCounts() {
        return giftCounts;
    }

    public List<UserBean> getRecent() {
        return recent;
    }

    public boolean isFllow() {
        return isFllow;
    }

    public void setFllow(boolean isFllow){
        this.isFllow =isFllow;
    }

    public class GiftData{
        private long totalCount;
        private String image;
        private String name;

        public long getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(long totalCount) {
            this.totalCount = totalCount;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


}
