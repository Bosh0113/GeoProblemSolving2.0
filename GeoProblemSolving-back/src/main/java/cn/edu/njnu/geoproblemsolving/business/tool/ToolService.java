package cn.edu.njnu.geoproblemsolving.business.tool;

import cn.edu.njnu.geoproblemsolving.common.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.common.exception.MyException;
import cn.edu.njnu.geoproblemsolving.business.tool.support.dto.AddToolEntityDTO;
import cn.edu.njnu.geoproblemsolving.business.tool.support.dto.UpdateToolEntityDTO;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.*;

/**
 * @Author Zhiyi
 * @Date 2020/5/27  22:57
 * @Version 1.0.0
 */
@Service
public class ToolService  {
    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public JsonResult createTool(AddToolEntityDTO add) {
        try {
            toolRepository.findFirstByToolNameAndProvider(add.getToolName(), add.getProvider()).ifPresent((el)->{
                throw new MyException(ResultEnum.EXISTS_OBJECT);
            });

            ToolEntity toolEntity = new ToolEntity();
            String tid = UUID.randomUUID().toString();
            add.setTid(tid);
            add.convertTo(toolEntity);

            return ResultUtils.success(toolRepository.insert(toolEntity));
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult deleteByTid(String tid) {
        try {
            toolRepository.deleteById(tid);
            return ResultUtils.success("Success");
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult findAllByProvider(String provider) {
        try {
            return ResultUtils.success(toolRepository.findAllByProvider(provider));
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult updateTool(String tid,UpdateToolEntityDTO update) {
        try {
            ToolEntity toolEntity = toolRepository.findFirstByTid(tid).orElseThrow(MyException::noObject);
            update.updateTo(toolEntity);
            return ResultUtils.success(toolRepository.save(toolEntity));
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult readTool(String key, String value) {
        try {
            Query query = Query.query(Criteria.where(key).is(value));
            if (mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
                return ResultUtils.success(Collections.emptyList());
            } else {
                List<ToolEntity> ToolEntities = mongoTemplate.find(query, ToolEntity.class);
                return ResultUtils.success(ToolEntities);
            }
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    public JsonResult readTools(String[] tools) {
        try {
            List<ToolEntity> ToolEntities = new ArrayList<>();
            for(int i = 0; i < tools.length; i++){
                Query query = Query.query(Criteria.where("id").is(tools[i]));
                if (!mongoTemplate.find(query, ToolEntity.class).isEmpty()) {
                    ToolEntities.add(mongoTemplate.find(query, ToolEntity.class).get(0));
                }
            }
            return ResultUtils.success(ToolEntities);
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

    /**
     * @Deprecated 统一使用资源那边的接口
     * @param request
     * @return
     */
    public static JsonResult uploadPicture(HttpServletRequest request) {
        try {
            //
            return ResultUtils.success("");
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }

}
