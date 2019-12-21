package com.example.demo.config;

import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

public class AppMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public AppMappingJackson2HttpMessageConverter(){
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        setSupportedMediaTypes(mediaTypes);// tag6
    }
}