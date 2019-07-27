package cn.edu.njnu.geoproblemsolving.comparison.controller;

import cn.edu.njnu.geoproblemsolving.comparison.bean.JsonResult;
import cn.edu.njnu.geoproblemsolving.comparison.dao.item.CmpItemDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.project.CmpProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpItem;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.enums.ResultEnum;
import cn.edu.njnu.geoproblemsolving.comparison.utils.ResultUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 20:06 2019/7/25
 * @Modified By:
 **/
@CrossOrigin
@RestController
@RequestMapping("/cmp_item")
public class CmpItemController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/createCmpItem", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public JsonResult createCmpItem(@RequestBody CmpItem item) {
        CmpItemDaoImpl cmpItemDao = new CmpItemDaoImpl(mongoTemplate);
        try {
            CmpItem cmpItem = cmpItemDao.addCmpItem(item);
            //更新上级项目
            CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
            cmpProjectDao.updateCmpItems(item.getProjectId(),item.getItemId());
            return ResultUtils.success(cmpItem);
        } catch (Exception e) {
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }


    @RequestMapping(value = "/getCmpItemsByIdList",method = RequestMethod.POST)
    public JsonResult getCmpItemsByIdList(@RequestBody List<String> idList){
        CmpItemDaoImpl cmpItemDao = new CmpItemDaoImpl(mongoTemplate);
        try{
            List<CmpItem> itemByItemIdList = cmpItemDao.findItemByItemIdList(idList);
            return ResultUtils.success(itemByItemIdList);
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }

    @RequestMapping(value = "/getCmpItem",method = RequestMethod.GET)
    public JsonResult getCmpItem(@RequestParam("key") String key, @RequestParam("value") String value){
        CmpItemDaoImpl cmpItemDao = new CmpItemDaoImpl(mongoTemplate);
        try{
            List<CmpItem> items = cmpItemDao.getItems(key, value);
            return ResultUtils.success(items);
        }catch (Exception e){
            return ResultUtils.error(ResultEnum.FAILED);
        }
    }
}
