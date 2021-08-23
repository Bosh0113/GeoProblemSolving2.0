package cn.edu.njnu.geoproblemsolving.business.collaboration.compute.dao;

import cn.edu.njnu.geoproblemsolving.business.collaboration.compute.entity.ComputeTask;

public interface ComputeTaskDao {
    ComputeTask createTask(ComputeTask computeTask);
    ComputeTask updateTask(ComputeTask putTask);
}
