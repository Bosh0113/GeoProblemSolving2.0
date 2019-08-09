package cn.edu.njnu.geoproblemsolving.Dao.Resource;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IResourceDao {

    Object saveResource(HttpServletRequest request);

    Object readResource(String key,String value);

    Object readPublicResource();

    Object updateResource(HttpServletRequest request);

    String deleteResource(String key,String value);

    void getZipResource(HttpServletRequest request, HttpServletResponse response, String key, String value);

    void packageToZip(HttpServletRequest request,HttpServletResponse response);

    String copyFileToPersonalCenter(String resourceId, String userId,String privacy);
}
