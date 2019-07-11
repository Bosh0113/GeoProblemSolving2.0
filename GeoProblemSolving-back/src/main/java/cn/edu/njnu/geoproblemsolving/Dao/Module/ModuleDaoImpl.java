package cn.edu.njnu.geoproblemsolving.Dao.Module;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.Folder.FolderEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ModuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@Component
public class ModuleDaoImpl implements IModuleDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ModuleDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String createModule(ModuleEntity module){

        mongoTemplate.save(module);

        FolderEntity folderEntity = new FolderEntity();
        folderEntity.setFolders(new ArrayList<>());
        folderEntity.setFiles(new ArrayList<>());
        folderEntity.setFolderName(module.getTitle());
        folderEntity.setParentId("");
        folderEntity.setFolderId(module.getModuleId());
        return module.getModuleId();
    }

    @Override
    public Object readModule(String key,String value){

        Query query=Query.query(Criteria.where(key).is(value));
        if(mongoTemplate.find(query,ModuleEntity.class).isEmpty()){
            return "None";
        }else {

            List<ModuleEntity> ModuleEntities = mongoTemplate.find(query,ModuleEntity.class);

            return ModuleEntities;
        }
    }

    @Override
    public void deleteModule(String key,String value){
        Query query=Query.query(Criteria.where(key).is(value));
        mongoTemplate.remove(query,ModuleEntity.class);
    }

    @Override
    public String updateModule(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("moduleId").is(request.getParameter("moduleId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,ModuleEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
