package com.czq.cas;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *  这个配置是空值,是为了让spring 加载 这个包下 标注了  @Service @Component @Controller 等注解的Bean
 *  并需要在resource/META-INF/spring.factories 中配置
 * @author chengzequn@foxmail.com
 * @since 2020/8/10 17:16
 */
@Configuration
@ComponentScan("com.czq.cas")
public class SpringConfig {
}
