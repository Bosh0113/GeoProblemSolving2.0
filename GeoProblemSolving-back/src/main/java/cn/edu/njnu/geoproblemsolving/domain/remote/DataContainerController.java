package cn.edu.njnu.geoproblemsolving.domain.remote;

import cn.edu.njnu.geoproblemsolving.domain.remote.DataItemService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;

/**
 * @ClassName DataContainerController
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/16  21:58
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/dataItem")
public class DataContainerController {
    @Autowired
    DataItemService dataItemService;

    @RequestMapping(value = "/addDataItem",method = RequestMethod.POST)
    public Object addDataItem(HttpServletRequest request) throws IOException, ServletException {
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
        Part part=multiRequest.getPart("file");
        String header = part.getHeader("Content-Disposition");
        String filename2 = header.substring(header.indexOf("filename=\"") + 10,header.lastIndexOf("\""));//filename=" (整个字符串长度为10，所以要加10)
        // 获取文件名
        String fileName = part.getName();
        File file=File.createTempFile(part.getName(),"." + FilenameUtils.getExtension(filename2));//创建临时文件
        FileUtils.copyInputStreamToFile(multiRequest.getPart("file").getInputStream(),file);
        return dataItemService.addDataItem(file);
    }

    @RequestMapping(value = "/download/{id}",method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable("id") String id) {
        return dataItemService.download(id);
    }

}
