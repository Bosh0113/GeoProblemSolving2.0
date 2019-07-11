package cn.edu.njnu.geoproblemsolving.Dao.ModelBuilder;

import cn.edu.njnu.geoproblemsolving.Entity.ConceptualTaskEntity;

import javax.servlet.http.HttpServletRequest;

public interface IConceptualTaskDao {
    String saveConceptualTask(ConceptualTaskEntity conceptualTask);
    Object readConceptualTask(String collaborativeId);
    String deleteConceptualTask(String taskId);
    String updateConceptualTask(HttpServletRequest request);
}
