package cn.edu.njnu.geoproblemsolving.business.tool.chatroom.hydrologicalconcept.support.concepts;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.*;

/**
 * Description: HydrologicalConcept
 * <p>
 * Created by LYC on 2019/12/23 14:47
 */
@Data
@Document(collection = "ConceptSemantic")
public class Concepts implements Serializable {
    String conceptID;
    String name;
    String definition;
    String conceptType;
    Integer frequency;

    public Concepts myclone() {
        Concepts outer = null;
        try { // 将该对象序列化成流,因为写在流里的是对象的一个拷贝，而原对象仍然存在于JVM里面。所以利用这个特性可以实现对象的深拷贝
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            // 将流序列化成对象
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            outer = (Concepts) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return outer;
    }
}
