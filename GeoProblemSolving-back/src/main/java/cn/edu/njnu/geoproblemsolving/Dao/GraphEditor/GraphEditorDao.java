package cn.edu.njnu.geoproblemsolving.Dao.GraphEditor;

import cn.edu.njnu.geoproblemsolving.Entity.GraphEditorEntity;

import javax.servlet.http.HttpServletRequest;

public interface GraphEditorDao {

    Object saveTask(GraphEditorEntity editorEntity);

    Object loadTaskList(String scopeId);

    Object updateTask(HttpServletRequest request);

    String deleteTask(String taskId);
}
