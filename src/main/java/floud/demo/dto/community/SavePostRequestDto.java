package floud.demo.dto.community;

import floud.demo.domain.Community;
import floud.demo.domain.Users;
import floud.demo.domain.enums.PostType;
import lombok.Getter;

@Getter
public class SavePostRequestDto {
    private String title;
    private String content;
    private PostType postType;

    public Community toEntity(Users user){
        return Community.builder()
                .title(title)
                .content(content)
                .postType(postType)
                .users(user)
                .build();
    }
}
