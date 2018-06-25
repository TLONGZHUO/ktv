package com.francis.ktv.api;

import org.springframework.web.bind.annotation.RequestParam;

import javax.websocket.Session;

//点歌Api
public interface SongApi {

    //点歌
    void andSo(Session session, @RequestParam String songName);
}
