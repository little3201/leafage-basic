package top.abeille.basic.authority;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * VO Test Parent
 *
 * @author liwenqiang 2018/12/28 14:40
 **/
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public abstract class BasicVOTest<T> {

    protected abstract T setVO();

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * model的get和set方法
     */
    public void testGetAndSet() throws IllegalAccessException, InstantiationException, IntrospectionException,
            InvocationTargetException {
        T t = setVO();
        Class modelClass = t.getClass();
        Object obj = modelClass.newInstance();
        Field[] fields = modelClass.getDeclaredFields();
        for (Field f : fields) {
            PropertyDescriptor pd = new PropertyDescriptor(f.getName(), modelClass);
            Method get = pd.getReadMethod();
            Method set = pd.getWriteMethod();
            set.invoke(obj, get.invoke(obj));
        }
    }

    @Test
    public void getAndSetTest() throws InvocationTargetException, IntrospectionException,
            InstantiationException, IllegalAccessException {
        this.testGetAndSet();
    }

}