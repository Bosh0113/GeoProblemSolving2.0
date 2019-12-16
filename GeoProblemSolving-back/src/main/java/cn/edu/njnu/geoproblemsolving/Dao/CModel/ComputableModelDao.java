package cn.edu.njnu.geoproblemsolving.Dao.CModel;

import cn.edu.njnu.geoproblemsolving.Entity.ModelItem.ModelItemEntity;

public interface ComputableModelDao {
    Object readComputableModel(String oid);
    ModelItemEntity addDataTemplate();
}
