package com.francis.ktv.config;

/**
 * Created by wangzhuo on 2017/12/11.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.francis.ktv.dto.ReqDto;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.IOException;

public class MessageDecoder implements Decoder.Text<ReqDto> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ReqDto decode(String jsonMessage) throws DecodeException {

        ReqDto message = null;
        try {
            message = objectMapper.readValue(jsonMessage,ReqDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;

    }

    @Override
    public boolean willDecode(String jsonMessage) {
        try {
            // Check if incoming message is valid JSON
            //Json.createReader(new StringReader(jsonMessage)).readObject();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("MessageDecoder -init method called");
    }

    @Override
    public void destroy() {
        System.out.println("MessageDecoder - destroy method called");
    }

}
