package com.minimo.backend.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonTypeInfo( // 다형성 처리, 어떤 하위 클래스로 역직렬화 할지
        use=JsonTypeInfo.Id.NAME, // NAME을 사용하여 각 하위 클래스에 대해 이름을 지정
        include = JsonTypeInfo.As.PROPERTY, // 타입 정보를 JSON 속성으로 포함시킨다.
        property = "success")   // property이름 : success
@JsonSubTypes({ // 어떤 응답을 사용할지 컴퓨터가 알아차리도록 도와주는 표시
        @JsonSubTypes.Type(value= SuccessResponseBody.class,name="true"), // SuccessResponseBody: name을 true로 지정
        @JsonSubTypes.Type(value= FailedResponseBody.class,name="false")  // name을 false로 지정
})

public sealed  abstract class ResponseBody<T> permits SuccessResponseBody, FailedResponseBody {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String code;
}