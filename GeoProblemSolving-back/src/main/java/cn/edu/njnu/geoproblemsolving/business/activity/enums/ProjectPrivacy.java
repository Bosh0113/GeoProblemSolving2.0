package cn.edu.njnu.geoproblemsolving.domain.activity.enums;

import lombok.Getter;

@Getter

/**
 * Public: free to join and quit
 * Discoverable: apply to join and quit
 * Private: only for Project Level
 */
public enum ProjectPrivacy {
    Public,
    Discoverable,
    Private
}
