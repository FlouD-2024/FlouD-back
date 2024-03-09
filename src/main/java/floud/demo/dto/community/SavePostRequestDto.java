package floud.demo.dto.community;

import floud.demo.domain.Community;
import floud.demo.domain.Users;
import lombok.Getter;

@Getter
public class SavePostRequestDto {
    private String title;
    private String content;

    public Community toEntity(Users user){
        return Community.builder()
                .title(title)
                .content(content)
                .users(user)
                .build();
    }
}
