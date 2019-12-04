package cn.edu.njnu.geoproblemsolving.Entity.Model.support;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskData {
    String statename;
    String event;
    String url;
    String tag;
}
