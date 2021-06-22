package cn.edu.njnu.geoproblemsolving.business.activity.enums;

import lombok.Getter;


/**
 * None: don't accept resources from last activities
 * Given: accept explicitly given resources from last activities
 * Constraints: accept resources from last activities according to the some conditions
 * All: accept all resources
 */
@Getter
public enum ResProtocol {
    None,
    Constraints,
    All
}
