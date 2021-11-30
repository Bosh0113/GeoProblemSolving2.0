package cn.edu.njnu.geoproblemsolving.business.activity.docParse;

import cn.edu.njnu.geoproblemsolving.business.activity.repository.ActivityDocRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName DocParseService
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/11/22
 **/
@Service
public class DocParseService {
    @Autowired
    ActivityDocRepository docRepository;
    
}
