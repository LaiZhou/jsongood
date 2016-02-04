package com.github.jessyZu.jsongood.demo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.jessyZu.jsongood.servlet.ServletRpcServer;

@SpringBootApplication
@Controller
@ImportResource(locations = { "classpath:applicationContext.xml" })
//@ImportResource(locations = { "classpath:applicationContext-dubbo.xml" })
public class DemoApplication extends SpringBootServletInitializer {

    @Autowired
    private ServletRpcServer rpcServer;

    @RequestMapping("/api")
    void apiGateway(HttpServletRequest request, HttpServletResponse response) throws IOException {
        rpcServer.handle(request, response);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);

    }
}
