package cn.edu.njnu.geoproblemsolving.comparison.dao.user;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
/**
 * @Author: SongJie
 * @Description:
 * @Date: Created in 15:22 2019/8/7
 * @Modified By:
 **/
public interface ICmpUserDao {
    void updateSolutions(String userId,String solutionId,boolean isAdd);

    void updateProjects(String userId,String projectId,boolean isAdd);
}
