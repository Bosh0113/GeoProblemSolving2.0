package cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder;

import cn.edu.njnu.geoproblemsolving.Entity.ComputationalTaskEntity;

import javax.servlet.http.HttpServletRequest;

public interface IComputationalTaskDao {
    String saveComputationalTask(ComputationalTaskEntity computationalTask);
    Object readComputationalTask(String collaborativeId);
    String deleteComputationTask(String taskId);
    String updateComputationalTask(HttpServletRequest request);
}
