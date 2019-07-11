package cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder;

import cn.edu.njnu.geoproblemsolving.Entity.LogicalTaskEntity;

import javax.servlet.http.HttpServletRequest;

public interface ILogicalTaskDao {
    String saveLogicalTask(LogicalTaskEntity logicalTask);
    Object readLogicalTask(String collaborativeId);
    String deleteLogicalTask(String taskId);
    String updateLogicalTask(HttpServletRequest request);
}
