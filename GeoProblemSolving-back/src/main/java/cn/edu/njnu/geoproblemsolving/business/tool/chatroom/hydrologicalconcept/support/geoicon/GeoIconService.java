package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.geoicon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * Description: HydrologicalConcept
 * <p>
 * Created by LYC on 2019/12/24 14:32
 */
@Service
public class GeoIconService {

    @Autowired
    GeoIconDao geoIconDao;

    @Value("${resourcePath}")
    String resourcePath;

    public String uploadGeoIcons(MultipartFile mfile, GeoIcon geoIcon) throws IOException {

        String folderPath = resourcePath + "/geoIcon";
        File folder = new File(folderPath);
        if (!folder.exists())
        {
            folder.mkdirs();
        }
        File file = new File(folderPath,new Date().getTime() + "_geoIcon.png");
        mfile.transferTo(file);

        geoIcon.setPathUrl("/static/static/geoIcon/" + file.getName());
        geoIconDao.insert(geoIcon);
        return "ok";
    }

    public GeoIcon getGeoIconByGeoId(String geoId){
        GeoIcon icon =geoIconDao.findGeoIconByGeoId(geoId);
        return icon;
    }

    public List<GeoIcon> getAllGeoIcons(){
        return geoIconDao.findAll();
    }

    public List<GeoIconDTO> getGeoIconByNameContains(String key){
        return geoIconDao.findGeoIconByNameContains(key);
    }
}
