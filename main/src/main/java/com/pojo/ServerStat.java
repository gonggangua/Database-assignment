package com.pojo;

import java.sql.Time;

public class ServerStat {
    private int messageCnt;
    private int totalLength;
    private int callCnt;
    private Time totalTime;

    public ServerStat() {
    }

    public ServerStat(int messageCnt, int totalLength, int callCnt, Time totalTime) {
        this.messageCnt = messageCnt;
        this.totalLength = totalLength;
        this.callCnt = callCnt;
        this.totalTime = totalTime;
    }

    public int getMessageCnt() {
        return messageCnt;
    }

    public int getTotalLength() {
        return totalLength;
    }

    public int getCallCnt() {
        return callCnt;
    }

    public Time getTotalTime() {
        return totalTime;
    }

    @Override
    public String toString() {
        return "ServerStat{" +
                "messageCnt=" + messageCnt +
                ", totalLength=" + totalLength +
                ", callCnt=" + callCnt +
                ", totalTime=" + totalTime +
                '}';
    }
}
