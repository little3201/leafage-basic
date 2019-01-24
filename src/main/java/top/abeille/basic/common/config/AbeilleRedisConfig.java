package top.abeille.basic.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置
 *
 * @author liwenqiang 2018/8/1 13:51
 **/
@Configuration
public class AbeilleRedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        //初始化序列化对象
        // redisTemplate的序列化默认JdkSerializationRedisSerializer，但是会有问题，在数据前加入\xAC\xED\x00\x05t\x00!
        // 解决方法一：采用StringRedisSerializer，建议key使用这个进行序列化
        // 解决方法二：采用其他序列化类，如Jackson或fastJson
        // 使用Jackson2JsonRedisSerializer，必须提供要序列化对象的类型信息(.class对象)
        template.setConnectionFactory(redisConnectionFactory);
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

}
