package cn.edu.njnu.geoproblemsolving.Dao.ContextDefinition;

import cn.edu.njnu.geoproblemsolving.Entity.ContextDefinitionEntity;

import javax.servlet.http.HttpServletRequest;

public interface IContextDefinitionDao {
    String createStep(ContextDefinitionEntity contextDefinition);
    Object readContextDefinition(String key,String value);
    void deleteContextDefinition(String key,String value);
    String updateContextDefinition(HttpServletRequest request);
}





