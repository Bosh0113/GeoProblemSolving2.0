package cn.edu.njnu.geoproblemsolving.comparison.dao.solution;

import cn.edu.njnu.geoproblemsolving.comparison.dao.project.CmpProjectDaoImpl;
import cn.edu.njnu.geoproblemsolving.comparison.dao.user.CmpUserImpl;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpProject;
import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSolution;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:35 2019/8/5
 * @Modified By:
 **/
public class SolutionImpl implements ISolutionDao {

    private final MongoTemplate mongoTemplate;

    public SolutionImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public CmpSolution addSolution(CmpSolution solution) {
        String solutionId = UUID.randomUUID().toString();
        solution.setSolutionId(solutionId);
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        solution.setCreateTime(dateFormat.format(date));
        mongoTemplate.save(solution);

        //更新project信息
        CmpProjectDaoImpl cmpProjectDao = new CmpProjectDaoImpl(mongoTemplate);
        cmpProjectDao.updateSolutionList(solution.getProjectId(),solutionId,true);
        //更新user信息
        CmpUserImpl cmpUserDao = new CmpUserImpl(mongoTemplate);
        cmpUserDao.updateSolutions(solution.getOwnerId(),solutionId,true);

        return solution;
    }

    @Override
    public String deleteSolution(CmpSolution solution) {
        return null;
    }

    @Override
    public String updateSolution(CmpSolution solution) {
        return null;
    }

    @Override
    public List<CmpSolution> getAllSolution() {
        return null;
    }

    @Override
    public CmpSolution findSolutionById(String id) {
        return null;
    }

    @Override
    public List<CmpSolution> findSolutionsByIdList(List<String> idList) {
        Query query = Query.query(Criteria.where("solutionId").in(idList));
        List<CmpSolution> cmpSolutions = mongoTemplate.find(query, CmpSolution.class);
        return cmpSolutions;
    }

    @Override
    public List<CmpSolution> getSolutions(String key, String value) {
        return null;
    }
}
