package com.prograngers.backend.service;

import com.prograngers.backend.dto.request.UpdateMemberAccountInfoRequest;
import com.prograngers.backend.dto.response.member.MemberAccountInfoResponse;
import com.prograngers.backend.entity.member.Member;
import com.prograngers.backend.exception.badrequest.BlankNicknameException;
import com.prograngers.backend.exception.notfound.MemberNotFoundException;
import com.prograngers.backend.exception.unauthorization.AlreadyExistNicknameException;
import com.prograngers.backend.exception.unauthorization.IncorrectPasswordException;
import com.prograngers.backend.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberAccountInfoResponse getMemberAccount(Long memberId){
        return MemberAccountInfoResponse.from(findById(memberId));
    }

    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    }


    private void validCorrectPassword(UpdateMemberAccountInfoRequest updateMemberAccountInfoRequest, Member member) {
        if(member.getPassword().equals(updateMemberAccountInfoRequest.getOldPassword()))
            throw new IncorrectPasswordException();
    }
    private void validNicknameBlank(String nickname) {
        if(nickname.isBlank()) throw new BlankNicknameException();
    }

    private void validNicknameDuplication(String nickname) {
        if(memberRepository.findByNickname(nickname).isPresent())
            throw new AlreadyExistNicknameException();
    }
}
