package cn.edu.njnu.geoproblemsolving.domain.hydrologicalconcept.support.support;

import lombok.Data;

import java.util.List;

@Data
public class ConceptClassification {
    String depend;
    List<String> subConcepts;
}
