package com.example.basespringboot.rest;

import lombok.SneakyThrows;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class RestClient {

    private RestTemplate restTemplate;

    HttpComponentsClientHttpRequestFactory requestFactory;
    private String url = "";
    private String nameSpace = "";
    private String path = "";

    public RestClient() {
        restTemplate = new RestTemplate();
    }

    /**
     * set timeout 3 giây
     */
    public void setTimeout3000() {
        requestFactory.setConnectTimeout(3000);
//        requestFactory.setReadTimeout(3000);
        restTemplate.setRequestFactory(requestFactory);
    }

    /**
     * set URL
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * set namespace
     * @param nameSpace
     */
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    /**
     * set path
     * @param path path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * set call theo https
     */
    @SneakyThrows
    public void acceptSsl() {
//        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
//        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
//        SSLConnectionSocketFactory cfs = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
//        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(cfs).build();
//        requestFactory.setHttpClient(httpClient);
//        restTemplate.setRequestFactory(requestFactory);
    }

    /**
     * Set call kiểu soap
     */
    public void isCallSoap() {
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().set("Content-Type", "text/xml");
                return execution.execute(request, body);
            }
        });
    }

    /**
     * Thêm header khi call service
     * @param key key
     * @param value value
     */
    public void addHeader(String key, String value) {
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().set(key, value); // Thêm header tùy chỉnh
                request.getHeaders().set("Cache-Control", "no-store");
                return execution.execute(request, body);
            }
        });
    }


    /**
     * Call
     * @param o      Đối tượng call
     * @param tClass Kiểu đối tượng trả về
     * @param <T>    Kiểu trả về
     * @return Trả về dữ liệu đã call dạng object
     */
    public <T> T postObject(Object o, Class<T> tClass) throws Exception {
        T response = restTemplate.postForObject(url + nameSpace + path, o, tClass);
        return response;
    }

}
