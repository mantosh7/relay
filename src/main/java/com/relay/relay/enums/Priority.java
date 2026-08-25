package com.relay.relay.enums;

public enum Priority {
    HIGH(10),
    MEDIUM(5),
    LOW(1);

    private final int level ;

    Priority(int level){
        this.level = level;
    }

    public int getLevel(){
        return level;
    }
}
