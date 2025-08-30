package com.minimo.backend.global.response;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Getter;

@Getter
@JsonTypeName("true") // 이 클래스가 직렬화될 때 어떤 이름을 가질지 명시적으로 지정, 명시성을 높이기 위해 재지정한 것임

public final class SuccessResponseBody<T> extends ResponseBody<T> {  // 더 이상 다른 클래스에 의해 상속될 수 없다는 뜻, 불필요한 상속으로 인한 복잡성을 줄이고, 클래스의 무결성을 유지할 수 있다.
    private final T data;  // 성공 응답이 생성된 후에는 그 응답의 데이터가 변경되지 않아야 하는 경우가 많다.
    public SuccessResponseBody(){
        data=null;
    }
    public SuccessResponseBody(T result){
        this.data=result;
    }
}