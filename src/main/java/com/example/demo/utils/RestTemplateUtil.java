package com.example.demo.utils;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class RestTemplateUtil {
	private static final Logger logger=Logger.getLogger(RestTemplateUtil.class);
	public static SimpleClientHttpRequestFactory simpleClientHttpRequestFactory=new SimpleClientHttpRequestFactory();
	public static RestTemplate restTemplate;
	//设置超时时间
	static{
		//设置超时时间
		simpleClientHttpRequestFactory.setConnectTimeout(60*1000);
		simpleClientHttpRequestFactory.setReadTimeout(60*1000);
		restTemplate=new RestTemplate(simpleClientHttpRequestFactory);
		//设置字符集编码
		HttpMessageConverter<?> converterTarget=null;
		List<HttpMessageConverter<?>> converterList=restTemplate.getMessageConverters();
		for (HttpMessageConverter<?> item : converterList) {
			if (StringHttpMessageConverter.class==item.getClass()) {
				converterTarget=item;
				break;
			}
		}
		if (null !=converterTarget) {
			converterList.remove(converterTarget);
		}
		converterList.add(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
	}
	
	/**
	 * 功能:post请求
	 * 作者:chenyang yan
	 * 日期:2018-9-30下午01:15:10
	 * @param url
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static String postForEntity(String url,Object request) throws Exception{
		String body=null;
		try {
			ResponseEntity<String> response = restTemplate.postForEntity(url,request,String.class);
			body=response.getBody();
		} catch (Exception e) {
			logger.info(e);
			throw new Exception(e);
		}
		return body;
	}

	/**
	 * 功能:删除,delete请求
	 * 作者:chenyang yan
	 * 日期:2018-10-30上午11:01:18
	 * @param url
	 * @param urlVariables
	 * @return
	 * @throws Exception
	 */
	public static String delete(String url,Object... urlVariables) throws Exception{
		String body=null;
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE,null,String.class,urlVariables);
			body=response.getBody();
		} catch (Exception e) {
			logger.info(e);
			throw new Exception(e);
		}
		return body;
	}

	/**
	 * 功能:修改，put请求
	 * 作者:chenyang yan
	 * 日期:2018-10-30上午11:02:18
	 * @param url
	 * @param map
	 * @param urlVariables
	 * @return
	 * @throws Exception
	 */
	public static String put(String url, MultiValueMap<String,String>  map, Object... urlVariables) throws Exception{
		String body=null;
		try {
			HttpEntity<Object> httpEntity=new HttpEntity<Object>(map,new HttpHeaders());
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT,httpEntity,String.class,urlVariables);
			body=response.getBody();
		} catch (Exception e) {
			logger.info(e);
			throw new Exception(e);
		}
		return body;
	}
	
	/**
	*@Description: get请求
	*@Param: url
	*@return: 
	*@Author: ty
	*@Date: 2019/3/13
	*/
	public static String get(String url,  Object... urlVariables) throws Exception{
		String body=null;
		try {
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET,null,String.class,urlVariables);
			body=response.getBody();
		} catch (Exception e) {
			logger.info(e);
			throw new Exception(e);
		}
		return body;
	}
}
