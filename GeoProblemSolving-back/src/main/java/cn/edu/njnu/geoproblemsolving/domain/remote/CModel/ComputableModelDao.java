package cn.edu.njnu.geoproblemsolving.domain.remote.CModel;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.ModelItem.ModelItemEntity;

public interface ComputableModelDao {
    Object readComputableModel(String oid);
    ModelItemEntity addDataTemplate();
}
