package cn.edu.njnu.geoproblemsolving.Dao.Step;

import cn.edu.njnu.geoproblemsolving.Entity.StepEntity;

import javax.servlet.http.HttpServletRequest;

public interface IStepDao {
    String createStep(StepEntity step);
    Object readStep(String key,String value);
    void deleteStep(String key,String value);
    String updateStep(HttpServletRequest request);
}





