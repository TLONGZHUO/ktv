package com.francis.ktv.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.francis.ktv.dto.ReqDto;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by wangzhuo on 2017/12/11.
 */
public class MessageEncoder implements Encoder.Text<ReqDto> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String encode(ReqDto reqDto) throws EncodeException {

        String message = null;
        try {
            message = objectMapper.writeValueAsString(reqDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return message;

    }

    @Override
    public void init(EndpointConfig ec) {
        System.out.println("MessageEncoder - init method called");
    }

    @Override
    public void destroy() {
        System.out.println("MessageEncoder - destroy method called");
    }

}
