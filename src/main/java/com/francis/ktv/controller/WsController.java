package com.francis.ktv.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.francis.ktv.config.MessageDecoder;
import com.francis.ktv.config.MessageEncoder;
import com.francis.ktv.controller.service.SongService;
import com.francis.ktv.dto.ReqDto;
import com.francis.ktv.utils.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@ServerEndpoint(value = "/websocket/{userId}", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class WsController {


    //日志记录
    private Logger logger = LoggerFactory.getLogger(WsController.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //记录每个用户下多个终端的连接
    private static Map<Long, Set<WsController>> userSocket = new HashMap<>();

    //需要session来对用户发送数据, 获取连接特征userId
    private Session session;
    private Long userId;

    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, Session session) {
        this.session = session;
        this.userId = userId;
        onlineCount++;
        //根据该用户当前是否已经在别的终端登录进行添加操作
        if (userSocket.containsKey(this.userId)) {
            logger.debug("当前用户id:{}已有其他终端登录", this.userId);
            userSocket.get(this.userId).add(this); //增加该用户set中的连接实例
        } else {
            logger.debug("当前用户id:{}第一个终端登录", this.userId);
            Set<WsController> addUserSet = new HashSet<>();
            addUserSet.add(this);
            userSocket.put(this.userId, addUserSet);
        }
        logger.info("用户{}登录的终端个数是为{}", userId, userSocket.get(this.userId).size());
        logger.info("当前在线用户数为：{},所有终端个数为：{}", userSocket.size(), onlineCount);
    }

    @OnMessage
    public void onMessages(String message, Session session) {
        ObjectMapper objectMapper = new ObjectMapper();
        SongService songService = SpringContextUtils.getBean(SongService.class);
        //判断操作
        try {
            ReqDto reqDto = objectMapper.readValue(message, ReqDto.class);
            switch (reqDto.getControlType()){
                case 1 :
                    songService.andSo(session,getSeesionName(session.getId()), reqDto.getContent());
                    this.sendMessageToAllUsers("用户" + userId +"点歌[" + reqDto.getContent() + "]1");
                    break;
                case 2 :
                    songService.dis(session,getSeesionName(session.getId()), reqDto.getContent());
                    this.sendMessageToAllUsers("用户" + userId + "评论说[" + reqDto.getContent() + "]2");
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose() {
        //移除当前用户终端登录的websocket信息,如果该用户的所有终端都下线了，则删除该用户的记录
        if (userSocket.get(this.userId).size() == 0 || userSocket.get(this.userId).size() == 1) {
            userSocket.remove(this.userId);
            logger.info("用户{}已在所有终端下线", this.userId);
        } else {
            userSocket.get(this.userId).remove(this);
            logger.info("用户{}登录的终端个数是为{}", this.userId, userSocket.get(this.userId).size());
        }
        onlineCount--;
//        logger.info("用户{}登录的终端个数是为{}",this.userId,userSocket.get(this.userId).size());
        logger.info("当前在线用户数为：{},所有终端个数为：{}", userSocket.size(), onlineCount);
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.debug("用户id为：{}的连接发送错误", this.userId);
        error.printStackTrace();
    }

    /**
     * @param @param  userId 用户id
     * @param @param  message 发送的消息
     * @param @return 发送成功返回true，反则返回false
     * @Title: sendMessageToUser
     * @Description: 发送消息给用户下的所有终端
     */
    public  Boolean sendMessageToUser(Long userId, String message) {
        if (userSocket.containsKey(userId)) {
            logger.info(" 给用户id为：{}的所有终端发送消息：{}", userId, message);
            for (WsController WS : userSocket.get(userId)) {
                logger.info("sessionId为:{}", WS.session.getId());
                try {
                    WS.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.info(" 给用户id为：{}发送消息失败", userId);
                    return false;
                }
            }
            return true;
        }
        logger.info("发送错误：当前连接不包含id为：{}的用户", userId);
        return false;
    }

    public  void sendMessageToAllUsers(String message){
        Set<Long> keys = userSocket.keySet();
        List<WsController> wsControllers = new ArrayList<>();
        keys.stream().forEach(one -> {
            Set<WsController> wss = userSocket.get(one);
            wsControllers.addAll(wss);
        });
        for (WsController ws : wsControllers)
        this.sendMessageToUser(ws.userId,message);
    }


    private String getSeesionName(String sessionId){
        return "有人";
    }

}
