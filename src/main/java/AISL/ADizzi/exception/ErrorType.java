package AISL.ADizzi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // 요청 관련 오류
    MISSING_HEADER("E001", "요청 헤더가 누락되었습니다."),
    INVALID_REQUEST_PARAMETER("E002", "잘못된 요청 파라미터입니다."),
    MISSING_REQUEST_PARAMETER("E003", "요청 파라미터가 누락되었습니다."),
    INVALID_REQUEST_BODY("E004", "잘못된 요청 본문입니다."),
    UNEXPECTED_ERROR("E005", "예기치 않은 오류가 발생하였습니다."),

    // JWT 인증 관련 오류
    JWT_TOKEN_NOT_FOUND("E101", "JWT 토큰을 찾을 수 없습니다."),
    INVALID_JWT_TOKEN("E102", "유효하지 않은 JWT 토큰입니다."),
    ACCESS_TOKEN_EXPIRED("E103", "Access Token이 만료되었습니다."),
    REFRESH_TOKEN_EXPIRED("E104", "Refresh Token이 만료되었습니다."),

    // OAuth 인증 관련 오류
    INVALID_AUTHORIZATION_CODE("E201", "잘못된 인증 코드입니다."),
    KAKAO_USER_INFO_REQUEST_FAILED("E202", "카카오 사용자 정보 요청에 실패했습니다."),
    NAVER_USER_INFO_REQUEST_FAILED("E203", "네이버 사용자 정보 요청에 실패했습니다."),
    GOOGLE_USER_INFO_REQUEST_FAILED("E204", "구글 사용자 정보 요청에 실패했습니다."),
    MAIL_SEND_FAILED("E205", "이메일 전송에 실패했습니다."),

    // 파일 관련 오류
    FILE_UPLOAD_ERROR("E301", "파일 업로드 중 오류가 발생하였습니다."),
    FILE_DELETE_ERROR("E302", "파일 삭제 중 오류가 발생하였습니다."),

    // 권한 관련 오류
    PERMISSION_DENIED("E401", "접근 권한이 없습니다."),

    // 리소스 관련 오류
    FILE_NOT_FOUND("E501", "해당 파일을 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS("E502", "이미 사용 중인 이메일입니다."),
    MEMBER_NOT_FOUND("E503", "해당 회원을 찾을 수 없습니다."),

    // 형식 관련 오류
    INVALID_DATE_FORMAT("E601", "올바른 날짜 형식이 아닙니다."),

    // 인증 관련
    INVALID_PASSWORD("E701", "비밀번호가 일치하지 않습니다."),
    INVALID_AUTHOR("E702", "작성자 본인이 아닙니다."),

    // 방 관련
    ROOM_ALREADY_EXISTS("E801", "이미 사용 중인 이름입니다."),
    ROOM_NOT_FOUND("E802", "해당 방을 찾을 수 없습니다."),

    // 수납장 관련
    CONTAINER_ALREADY_EXISTS("E1201", "이미 사용 중인 이름입니다."),
    CONTAINER_NOT_FOUND("E1202", "해당 수납장을 찾을 수 없습니다."),

    // 슬롯 관련
    SLOT_NOT_FOUND("E902","해당 슬롯을 찾을 수 없습니다." ),

    // 물건 관련
    ITEM_ALREADY_EXISTS("E1001", "이미 사용 중인 이름입니다."),
    ITEM_NOT_FOUND("E1002", "해당 물건을 찾을 수 없습니다."),

    // 이미지
    IMAGE_NOT_FOUND("E1101","해당 이미지을 찾을 수 없습니다." ),
    INVALID_IMAGE("E1102", "해당 이미지이 비어있습니다." ),
    IMAGE_UPLOAD_FAILED("E1103","업로드 실패." )
    ;

    private final String code;
    private final String message;
}
