package cn.edu.njnu.geoproblemsolving.Enums;

import lombok.Getter;

@Getter
public enum UserTitle {
    Prof("Prof."), Dr("Dr."), Mr("Mr"), Ms("Ms"), Miss("Miss"), Mrs("Mrs"), Mx("Mx");

    private String desc;

    UserTitle(String desc) {
        this.desc = desc;
    }
}
