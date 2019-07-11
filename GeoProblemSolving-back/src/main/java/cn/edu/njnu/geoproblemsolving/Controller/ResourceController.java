package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Resource.ResourceDaoImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Object uploadResource(HttpServletRequest request) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.saveResource(request);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readResource(@RequestParam("key") String key, @RequestParam("value") String value) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.readResource(key, value);
    }

    @RequestMapping(value = "/allPublic", method = RequestMethod.GET)
    public Object readAllPublic() {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.readPublicResource();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteResource(@RequestParam("resourceId") String resourceId) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        return resourceDao.deleteResource("resourceId", resourceId);
    }

    @RequestMapping(value = "/operateZip", method = RequestMethod.GET)
    public void getZipResource(HttpServletRequest request, HttpServletResponse response, @RequestParam("key") String key, @RequestParam("value") String value) {
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        resourceDao.getZipResource(request, response, key, value);
    }

    @RequestMapping(value = "/packageZIP",method = RequestMethod.POST)
    public void packageZIP(HttpServletRequest request,HttpServletResponse response){
        ResourceDaoImpl resourceDao = new ResourceDaoImpl(mongoTemplate);
        resourceDao.packageToZip(request,response);
    }
}
