package floud.demo.domain.enums;

import lombok.Getter;

@Getter
public enum PostType {
    FIND_FRIEND("FIND_FRIEND"),
    FREE("FREE");

    private final String postType;

    PostType(String postType){
        this.postType = postType;
    }
}
