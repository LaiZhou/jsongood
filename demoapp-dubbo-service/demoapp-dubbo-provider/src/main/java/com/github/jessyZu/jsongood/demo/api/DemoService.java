package com.github.jessyZu.jsongood.demo.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public interface DemoService {

    public Param sayHello1(Param param1, Param param2);

    public String sayHello2(@NotNull @Size(min = 4, max = 5, message = "最小是4") String param,
                            @NotNull @Size(min = 4, max = 5, message = ">3") String param2);
}