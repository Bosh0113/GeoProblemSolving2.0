package cn.edu.njnu.geoproblemsolving.domain.activity.enums;

import lombok.Getter;


/**
 * Siblings: activities with the unidirectional resource flow
 * Twins: activities with the bidirectional resource flow
 * Cousins: activities without resource flow
 */
@Getter
public enum ProtocolType {
    Siblings,
    Twins,
    Cousins
}
