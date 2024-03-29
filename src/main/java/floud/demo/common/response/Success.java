package floud.demo.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {

    // Default
    SUCCESS(HttpStatus.OK, "Request successfully processed"),




    //200 SUCCESS
    GET_GOOGLE_ACCESS_TOKEN_SUCCESS(HttpStatus.OK , "구글 로그인 성공"),
    GET_KAKAO_ACCESS_TOKEN_SUCCESS(HttpStatus.OK , "카카오 로그인 성공"),
    GET_REISSUE_ACCESS_TOKEN_SUCCESS(HttpStatus.OK , "토큰 재발급 성공"),
    GET_REFRESH_TOKEN_SUCCESS(HttpStatus.OK , "리프레시 토큰을 성공적으로 가져왔습니다."),


    GET_USER_INFO_SUCCESS(HttpStatus.OK , "유저 정보를 불러왔습니다."),

    GET_HOME_SUCCESS(HttpStatus.OK , "유저의 홈 정보를 성공적으로 조회하였습니다."),

    GET_ALARM_SUCCESS(HttpStatus.OK , "유저의 알람을 성공적으로 조회하였습니다."),

    UPDATE_MEMOIR_SUCCESS(HttpStatus.OK , "성공적으로 회고를 수정하였습니다."),
    GET_MY_MEMOIR_SUCCESS(HttpStatus.OK , "나의 회고를 성공적으로 조회하였습니다."),
    GET_MULTIPLE_MEMOIR_SUCCESS(HttpStatus.OK , "주차별 회고 목록을 성공적으로 조회하였습니다."),

    GET_MYPAGE_SUCCESS(HttpStatus.OK , "마이페이지를 성공적으로 조회하였습니다."),
    GET_MYPAGE_COMMUNITY_SUCCESS(HttpStatus.OK , "내가 쓴 글을 성공적으로 조회하였습니다."),
    REJECT_MY_FRIEND_SUCCESS(HttpStatus.OK , "성공적으로 친구 관계를 삭제하였습니다."),
    CHECK_NICKNAME_DUPLICATED(HttpStatus.OK , "성공적으로 닉네임 중복을 확인하였습니다."),

    FIND_FRIEND_SUCCESS(HttpStatus.OK , "성공적으로 친구를 검색하였습니다."),
    GET_FRIEND_LIST_SUCCESS(HttpStatus.OK , "나의 친구 목록을 성공적으로 조회하였습니다."),
    GET_FRIEND_MEMOIR_SUCCESS(HttpStatus.OK , "친구의 회고를 성공적으로 조회하였습니다."),
    DELETE_FRIEND_SUCCESS(HttpStatus.OK , "친구를 성공적으로 삭제하였습니다."),

    GET_COMMUNITY_SUCCESS(HttpStatus.OK , "게시판을 성공적으로 조회하였습니다."),
    GET_COMMUNITY_DETAIL_SUCCESS(HttpStatus.OK , "하나의 게시글을 성공적으로 조회하였습니다."),
    GET_COMMUNITY_POST_SUCCESS(HttpStatus.OK , "내가 작성한 게시글을 성공적으로 조회하였습니다."),
    UPDATE_COMMUNITY_POST_SUCCESS(HttpStatus.OK , "나의 게시글을 성공적으로 수정하였습니다."),
    DELETE_COMMUNITY_POST_SUCCESS(HttpStatus.OK , "나의 게시글을 성공적으로 삭제하였습니다."),




    //201 CREATED SUCCESS
    CREATE_MEMOIR_SUCCESS(HttpStatus.CREATED, "성공적으로 회고를 등록하였습니다."),
    UPDATE_MYPAGE_SUCCESS(HttpStatus.CREATED , "마이페이지를 성공적으로 수정하였습니다."),
    REQUEST_FRIEND_SUCCESS(HttpStatus.CREATED , "친구 요청을 성공적으로 보내었습니다."),
    UPDATE_FRIEND_SUCCESS(HttpStatus.CREATED , "친구 상태를 성공적으로 변경하였습니다."),
    CREATE_COMMUNITY_POST_SUCCESS(HttpStatus.CREATED, "게시글을 성공적으로 등록하였습니다."),


    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}