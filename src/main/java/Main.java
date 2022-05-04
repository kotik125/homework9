import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static final String URI = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {

        try {
            CloseableHttpClient http = HttpClientBuilder.create()
                    .setUserAgent("Facts about cats")
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(4000)
                            .setSocketTimeout(20000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();

            HttpGet request = new HttpGet(URI);
            request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

            CloseableHttpResponse response = http.execute(request);

            Arrays.stream(response.getAllHeaders()).forEach(System.out::println);

            List<Cat> cats = objectMapper.readValue(
                    response.getEntity().getContent(),
                    new TypeReference<>() {
                    }
            );
            cats.stream()
                    .filter(cat -> cat.getUpvotes() != 0 && cat.getUpvotes() > 0)
                    .forEach(System.out::println);

            response.close();
            http.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
