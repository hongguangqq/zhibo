package com.jyt.baseapp.bean;

import java.util.List;

/**
 * @author Admin
 * @version $Rev$
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class AccountBean {
    private List<WalletBean> content;

    public List<WalletBean> getContent() {
        return content;
    }

    public void setContent(List<WalletBean> content) {
        this.content = content;
    }
}
