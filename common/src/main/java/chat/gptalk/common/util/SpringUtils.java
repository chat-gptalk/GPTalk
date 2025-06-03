package chat.gptalk.common.util;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringUtils implements BeanFactoryPostProcessor {

    private static ConfigurableListableBeanFactory beanFactory;

    public static String getActiveProfile() {
        return beanFactory.resolveEmbeddedValue("${spring.profiles.active}");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
        throws BeansException {
        beanFactory = configurableListableBeanFactory;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) beanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return beanFactory.getBean(clazz);
    }

    public static <T> Set<T> getBeansOfType(Class<T> clazz) throws BeansException {
        return new HashSet<>(beanFactory.getBeansOfType(clazz).values());
    }
}
