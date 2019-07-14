package com.park61.moduel.me.bean;

import java.io.Serializable;

/**
 * Created by HP on 2016/9/2.
 */
public class MemberButtonVo implements Serializable{
    private String buttonNamePic;
    private String buttonNameStr;
    private String buttonParams;
    private String memberName;

    public String getButtonNamePic() {
        return buttonNamePic;
    }

    public void setButtonNamePic(String buttonNamePic) {
        this.buttonNamePic = buttonNamePic;
    }

    public String getButtonNameStr() {
        return buttonNameStr;
    }

    public void setButtonNameStr(String buttonNameStr) {
        this.buttonNameStr = buttonNameStr;
    }

    public String getButtonParams() {
        return buttonParams;
    }

    public void setButtonParams(String buttonParams) {
        this.buttonParams = buttonParams;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}
