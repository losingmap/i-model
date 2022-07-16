# 安装配置

```xml

<dependency>
    <groupId>com.lft</groupId>
    <artifactId>i-model</artifactId>
    <version>1.0.0</version>
</dependency>
```

```java

@Configuration
@Import(ControllerIntercept.class)
@ConditionalOnWebApplication
public class AutoConfiguration implements WebMvcConfigurer {
    private final ModelFactory factory;

    public AutoConfiguration() {
        this.factory = new ModelFactory();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ModelResolver(factory));
    }

    public ModelFactory modelFactory() {
        return factory;
    }
}
```

# 应用开发

要使用IModel，则需要在路由方法上添加IModel参数

- Restful使用IModel返回值
- 普通路由使用String返回值

```java

@RestController
public class MainController {
    @GetMapping("/hello")
    public IModel hello(IModel model) {
        model.add("hello", "234");
        return model.success();
    }
}
```

## 验证实体

实现`ValidationEntity`接口将实体标记为验证实体，验证实体作为路由参数时，可通过`valid()`方法判定参数是否合法

```java
public class User implements ValidationEntity {
    private Integer id;

    private String name;

    private String password;

    @Override
    public boolean valid() {
        return name != null && password != null;
    }

    // getter&setter
}
```

注意的是，在路由方法中**最开始处**需要绑定合法性验证回调`invalidCallback`，否则意味着忽略合法实体验证

```java

@RestController
public class MainController {
    @GetMapping("/hello")
    public IModel hello(IModel model, User u) {
        // 参数不合法时调用绑定的回调
        model.invalidCallback(info -> {
            model.err(info.toString());
        });

        // 参数合法时调用
        model.add("hello", "234");
        return model.success();
    }
}
```

## 禁止执行

使用model的`failure`方法可以阻止方法继续执行，可以添加`failureCallback`回调来处理后续

```java
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class MainController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public IModel register(IModel model, User user) {
        model.invalidCallback(info -> {
            model.err(info.toString());
        });
        model.failureCallback(info -> {
            model.err(info.getMessage());
        });
        if (!userService.register(user)) {
            model.failure("用户名已被占用");
        }

        return model.success();
    }
}
```

## 临时跳转
使用`redirectTo`或`forwardTo`方法来中途跳转页面，执行跳转方法后会以阻止当前方法继续执行
```java
@RestController
public class MainController {
    @GetMapping("/hello")
    public IModel hello(IModel model, User u) {
        model.invalidCallback(info -> {
            model.redirectTo("/error");

            System.out.println("我不会被执行");
        });
        
        model.add("hello", "234");
        return model;
    }
}
```






