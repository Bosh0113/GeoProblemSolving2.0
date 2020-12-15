package cn.edu.njnu.geoproblemsolving.business.modeltask;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.UUID;

public class Utils {
    public static JSONObject convertMdl(JSONObject data) {

        JSONObject jsonObject = JSON.parseObject(JSONObject.toJSONString(data.getJSONObject("mdlJson")));
        String md5 = data.getString("md5");
        String doi = data.getString("oid");
        JSONArray jsonStates = jsonObject.getJSONArray("ModelClass").getJSONObject(0).getJSONArray("Behavior").getJSONObject(0).getJSONArray("StateGroup").getJSONObject(0).getJSONArray("States").getJSONObject(0).getJSONArray("State");
        JSONArray datasetItem = jsonObject.getJSONArray("ModelClass").getJSONObject(0).getJSONArray("Behavior").getJSONObject(0).getJSONArray("RelatedDatasets").getJSONObject(0).getJSONArray("DatasetItem");

        for (int j = 0; j < jsonStates.size(); j++) {
            String stateId =jsonStates.getJSONObject(j).getString("id");
            String stateName =jsonStates.getJSONObject(j).getString("name");
            for (int i = 0; i < jsonStates.getJSONObject(j).getJSONArray("Event").size(); i++) {
                JSONObject event = (JSONObject) jsonStates.getJSONObject(j).getJSONArray("Event").get(i);
                event.put("eventId", UUID.randomUUID().toString());
//                event.put("stateId", stateId);
                event.put("stateName", stateName);
                event.put("md5", md5);
                event.put("doi", doi);

                if (event.containsKey("ResponseParameter")) {
                    String datasetReference = ((JSONObject) event.getJSONArray("ResponseParameter").get(0)).getString("datasetReference");
                    for (int a = 0; a < datasetItem.size(); a++) {
                        JSONObject item = (JSONObject) datasetItem.get(a);
                        if (item.getString("name").equals(datasetReference)) {
                            event.put("datasetItem", item);
                        }
                    }
                }

                if (event.containsKey("DispatchParameter")) {
                    String datasetReference = ((JSONObject) event.getJSONArray("DispatchParameter").get(0)).getString("datasetReference");
                    for (int a = 0; a < datasetItem.size(); a++) {
                        JSONObject item = (JSONObject) datasetItem.get(a);
                        if (item.getString("name").equals(datasetReference)) {
                            event.put("datasetItem", item);
                        }
                    }
                }

                if (event.containsKey("ControlParameter")) {
                    String datasetReference = ((JSONObject) event.getJSONArray("ControlParameter").get(0)).getString("datasetReference");
                    for (int a = 0; a < datasetItem.size(); a++) {
                        JSONObject item = (JSONObject) datasetItem.get(a);
                        if (item.getString("name").equals(datasetReference)) {
                            event.put("datasetItem", item);
                        }
                    }
                }

            }
        }

            data.put("convertMdlJson",jsonStates);
            return data;
    }
}
