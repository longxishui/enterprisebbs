package com.aspirecn.corpsocial.common.eventbus;

/**
 * Created by duyinzhou on 2015/9/10.
 */
public class CommandData {
    public enum CommandType{
        ACCEPT_ADD_FRIEND,
        ADD_FRIEND,
        MODIFY_FREQUENTLY_CONTACT,
        NOTIFY,
        QUIT,
        CREATE_OR_MODIFY_GROUP,
        APPROVE_GROUP_APPLY,
        DIALOG_MESSAGE
    }
    private CommandHead commandHead;
    private String content;
    public void setContent(String content){
        this.content = content;
    }
    public void setCommandHead(CommandHead commandHead){
        this.commandHead = commandHead;
    }
    public CommandHead getCommandHeader(){
    return commandHead;
    }
    public String getContent(){
        return content;
    }
}
