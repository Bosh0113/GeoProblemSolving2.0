package cn.edu.njnu.geoproblemsolving.Entity.Activities.Enums;

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
