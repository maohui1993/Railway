package cn.dazhou.im.entity;

import org.jivesoftware.smack.packet.ExtensionElement;

/**
 * Created by hooyee on 2017/6/8.
 * 用于群聊时，传送当前发送用户的jid信息
 */

public class UserExtensionElement implements ExtensionElement {
    public static final String NAME_SPACE = "com.xml.extension";
    //用户信息元素名称
    public static final String ELEMENT_NAME = "userinfo";

    //用户昵称元素名称
    private String nameElement = "name";
    //用户昵称元素文本(对外开放)
    private String nameText = "";

    //用户头像地址元素名称
    private String urlElement = "url";
    //用户头像地址元素文本(对外开放)
    private String urlText = "";

    public String getNameText() {
        return nameText;
    }
    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getUrlText() {
        return urlText;
    }
    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    @Override
    public String getNamespace() {
        return NAME_SPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public CharSequence toXML() {
        StringBuilder sb = new StringBuilder();

        sb.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAME_SPACE).append("\">");
        sb.append("<" + nameElement + ">").append(nameText).append("</"+nameElement+">");
        sb.append("<" + urlElement + ">").append(urlText).append("</"+urlElement+">");
        sb.append("</"+ELEMENT_NAME+">");

        return sb.toString();
    }
}
