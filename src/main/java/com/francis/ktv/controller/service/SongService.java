package com.francis.ktv.controller.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.Session;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class SongService {

    private final Logger logger = LoggerFactory.getLogger(SongService.class);

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    private List<String> songNames = new LinkedList<>();

    public void andSo(Session session, String sessionName, String songName) {
        songNames.add(songName);
        redisTemplate.opsForSet().add("songs",songName);
//        session.getBasicRemote().sendText(sessionName + "刚刚点歌" + songName);
    }


    public void dis(Session session, String seesionName, String content) {
        this.andSo(session,seesionName,content);
    }
}
