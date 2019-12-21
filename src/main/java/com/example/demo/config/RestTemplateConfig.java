package com.example.demo.config; /**
 *
 */

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by caohuwei on 2018年9月10日下午4:37:35
 */
@Configuration
public class RestTemplateConfig {

    /*
     * 通过SSLContextBuilder绕过https安全验证
     */
    @Bean
    public RestTemplate bairongRestTemplate(List<RequestHeaderInterceptor> interceptors)
            throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        // factory.setConnectionRequestTimeout(requestTimeout);
        // factory.setConnectTimeout(connectTimeout);
        // factory.setReadTimeout(readTimeout);
        // https
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(),
                new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", new PlainConnectionSocketFactory()).register("https", socketFactory).build();
        PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
        phccm.setMaxTotal(200);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                .setConnectionManager(phccm).setConnectionManagerShared(true).build();
        factory.setHttpClient(httpClient);

        RestTemplate restTemplate = new RestTemplate(factory);
        List<ClientHttpRequestInterceptor> clientInterceptorList = new ArrayList<>();
        for (RequestHeaderInterceptor i : interceptors) {
            ClientHttpRequestInterceptor interceptor = i;
            clientInterceptorList.add(interceptor);
        }
        restTemplate.setInterceptors(clientInterceptorList);

        restTemplate.getMessageConverters().add(new AppMappingJackson2HttpMessageConverter());
        return restTemplate;

    }

    @Bean
    public RequestHeaderInterceptor requestHeaderInterceptor() {
        return new RequestHeaderInterceptor();
    }

    @Bean
    public ClientHttpRequestFactory appClientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpclient());
    }

    @Bean
    public HttpClient httpclient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(100);// 请求最大连接 TODO
        connectionManager.setDefaultMaxPerRoute(100);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000 * 60).setConnectTimeout(5000)
                .setConnectionRequestTimeout(3000).build();
        return HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).setConnectionManager(connectionManager)
                .build();
    }
}