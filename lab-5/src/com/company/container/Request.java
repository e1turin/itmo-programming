package com.company.container;

public class Request extends Message{
    private String cmdName;

    public Request(String cmdName, String content) {
        super(content);
        this.cmdName = cmdName;
    }

    public String getCmdName(){
        return cmdName;
    }
}
