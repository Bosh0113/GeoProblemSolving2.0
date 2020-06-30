package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.userimage;

import cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.concepts.Concepts;
import cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.concepts.ConceptsService;
import cn.edu.njnu.geoproblemsolving.domain.support.MxGraphUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserImageService {

    @Autowired
    UserImageDao userImageDao;

    @Autowired
    ConceptsService conceptsService;

    @Value("${resourcePath}")
    String resourcePath;

    public UserImage upload(MultipartFile mfile, String conceptName) throws IOException {
        String folderPath = resourcePath + "/userImage";
        File folder = new File(folderPath);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        File file = new File(folderPath,new Date().getTime() + "_userImage.png");
        mfile.transferTo(file);
        UserImage userImage = new UserImage();
        userImage.setGeoId(UUID.randomUUID().toString());
        userImage.setName(mfile.getOriginalFilename());
        userImage.setPathUrl("/static/userImage/" + file.getName());

        //获取conceptId
        Concepts c = conceptsService.findByName(conceptName);
        userImage.setConceptId(c.getConceptID());

        userImageDao.save(userImage);
        return userImage;
    }

    public UserImage userDIY(String width,String height,String xml,String type,String conceptName) throws Exception {
        UserImage userImage = new UserImage();

        String name = new Date().getTime() + "_"+type+".png";
        MxGraphUtils mxGraphUtils = new MxGraphUtils();
        mxGraphUtils.exportImage((int)Double.parseDouble(width),(int)Double.parseDouble(height), xml, resourcePath+"/userImage/", name);

        userImage.setGeoId(UUID.randomUUID().toString());
        userImage.setName(name);
        userImage.setPathUrl("/static/userImage/" + name);

        //获取conceptId
        Concepts c = conceptsService.findByName(conceptName);
        userImage.setConceptId(c.getConceptID());

        userImageDao.save(userImage);
        return userImage;
    }

    public List<UserImage> findByConceptId(String conceptId){
        return userImageDao.findByConceptId(conceptId);
    }
}
