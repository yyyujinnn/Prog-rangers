package com.prograngers.backend.exception.notfound;

import com.prograngers.backend.exception.ErrorCode;

import static com.prograngers.backend.exception.ErrorCode.MEMBER_NOT_FOUND;

public class MemberNotFoundException extends NotFoundException{
    public MemberNotFoundException() {
        super("회원을 찾을 수 없습니다.", MEMBER_NOT_FOUND);
    }
}
