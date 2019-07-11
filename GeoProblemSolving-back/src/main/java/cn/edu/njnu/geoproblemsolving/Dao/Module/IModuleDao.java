package cn.edu.njnu.geoproblemsolving.Dao.Module;

import cn.edu.njnu.geoproblemsolving.Entity.ModuleEntity;

import javax.servlet.http.HttpServletRequest;

public interface IModuleDao {

    String createModule(ModuleEntity module);
    Object readModule(String key,String value);
    void deleteModule(String key,String value);
    String updateModule(HttpServletRequest request);
}
