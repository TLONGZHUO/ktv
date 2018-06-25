//package com.francis.ktv.controller.service;
//
//import com.francis.ktv.controller.WsController;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//public class MessageService {
//
//    private final static Logger logger = LoggerFactory.getLogger(MessageService.class);
//
//    private static Map<Long, Set<WsController>> userSocket = null;
//
//    public void sendToAll(Map<Long, Set<WsController>> userSocket){
//        this.userSocket = userSocket;
//        Set<Long> keys = userSocket.keySet();
//        for (Long one : keys) {
//            for (WsController WS : userSocket.get(one)) {
//                this.sendMessageToUser(one, "信息");
//            }
//        }
//    }
//
//
//    /**
//     * @param @param  userId 用户id
//     * @param @param  message 发送的消息
//     * @param @return 发送成功返回true，反则返回false
//     * @Title: sendMessageToUser
//     * @Description: 发送消息给用户下的所有终端
//     */
//    private Boolean sendMessageToUser(Long userId, String message) {
//        if (userSocket.containsKey(userId)) {
//            logger.info(" 给用户id为：{}的所有终端发送消息：{}", userId, message);
//            for (WsController WS : userSocket.get(userId)) {
//                logger.info("sessionId为:{}", WS.session.getId());
//                try {
//                    WS.session.getBasicRemote().sendText(message);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    logger.info(" 给用户id为：{}发送消息失败", userId);
//                    return false;
//                }
//            }
//            return true;
//        }
//        logger.info("发送错误：当前连接不包含id为：{}的用户", userId);
//        return false;
//    }
//}
