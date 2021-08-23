package cn.edu.njnu.geoproblemsolving.business.collaboration.compute.util;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.mongodb.core.query.Update;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName CommonUtil
 * @Description 通用工具类
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
public class CommonUtil {
    //获取空字段名
    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object propertyValue = src.getPropertyValue(pd.getName());
            if (propertyValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * 输入 Map
     * @param putInfo
     * @return
     */
    public Update setUpdate(Map<String, Object> putInfo){
        Update update = new Update();
        for (Map.Entry<String, Object> item : putInfo.entrySet()){
            String key = item.getKey();
            Object value = item.getValue();
            update.set(key, value);
        }
        return update;
    }
}
