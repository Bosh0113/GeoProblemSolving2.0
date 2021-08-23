package cn.edu.njnu.geoproblemsolving.business.activity.dao;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.TaskEntity;

import javax.servlet.http.HttpServletRequest;

public interface ITaskDao {

    Object saveTask(TaskEntity task);

    Object inquiryTask(String key,String value);

    Object inquiryTodo(String subProjectId);

    Object inquiryDoing(String subProjectId);

    Object inquiryDone(String subProjectId);

    String deleteTask(String taskId);

    Object updateTask(HttpServletRequest request);
}
