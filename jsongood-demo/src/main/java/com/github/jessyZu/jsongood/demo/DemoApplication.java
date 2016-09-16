package com.github.jessyZu.jsongood.demo;

import com.github.jessyZu.jsongood.servlet.ServletRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
@Controller
//@ImportResource(locations = { "classpath:applicationContext.xml" })
@ImportResource(locations = { "classpath:applicationContext-dubbo.xml" })
public class DemoApplication extends SpringBootServletInitializer {

    @Autowired
    private ServletRpcServer      rpcServer;

    private final ExecutorService ES = Executors.newCachedThreadPool();

    @RequestMapping("/api")
    void apiGateway(HttpServletRequest request, HttpServletResponse response) throws IOException {
        rpcServer.handle(request, response);
    }

    @RequestMapping("/async-api")
    @ResponseBody
    public DeferredResult<String> asyncApiGateway(final HttpServletRequest request, final HttpServletResponse response) {
        final DeferredResult<String> deferredResult = new DeferredResult<String>(60000L);//timeout is 60s
        ES.submit(new Runnable() {

            @Override
            public void run() {
                try {
                    rpcServer.handle(request, response);
                    deferredResult.setResult(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return deferredResult;
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);

    }
}
