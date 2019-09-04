package cn.edu.njnu.geoproblemsolving.comparison.dao.solution;

import cn.edu.njnu.geoproblemsolving.comparison.entity.CmpSolution;

import java.util.List;

/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:32 2019/8/5
 * @Modified By:
 **/
public interface ISolutionDao {
    CmpSolution addSolution(CmpSolution solution);

    String deleteSolution(CmpSolution solution);

    String updateSolution(CmpSolution solution);

    List<CmpSolution> getAllSolution();

    CmpSolution findSolutionById(String id);

    List<CmpSolution> findSolutionsByIdList(List<String> idList);

    List<CmpSolution> getSolutions(String key, String value);
}
