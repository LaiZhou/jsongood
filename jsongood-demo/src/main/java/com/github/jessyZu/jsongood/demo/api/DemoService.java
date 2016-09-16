package com.github.jessyZu.jsongood.demo.api;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public interface DemoService {

    Param sayHello1(Param param1, Param param2, List<Param> params);

    void sayHello2(@NotNull @Size(min = 4, max = 5, message = "must >=4") String param1,
                   @NotNull @Size(min = 4, max = 5, message = "must >=4") String param2);

    Param sayHello3(Param param1, Param param2, List<String> params);
}
