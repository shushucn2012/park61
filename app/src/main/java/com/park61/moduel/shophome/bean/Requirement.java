package com.park61.moduel.shophome.bean;

import java.io.Serializable;

/**
 * Created by HP on 2017/3/15.
 */
public class Requirement implements Serializable {
    private String content;
    private String coverPic;
    private Long id;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCoverPic() {
        return coverPic;
    }

    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
