package jpabook.jpashop.api;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberApiController {
    private final MemberService memberService;

    //전체 회원조회
    @GetMapping("/members")
    public Result findMembers() {
        List<Member> findMembers = memberService.findMembers();
        //Entity -> DTO 변환
        List<MemberDTO> collect = findMembers.stream()
                .map(member -> new MemberDTO(member.getName(), member.getAddress()))
                .collect(Collectors.toList());

        return new Result(collect);
    }

    //특정 회원조회
    @GetMapping("/members/{id}")
    public Result findMember(@PathVariable("id") Long id){
        Member findMember = memberService.findOne(id);

        return new Result(new MemberDTO(findMember.getName(), findMember.getAddress()));
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDTO {
        private String name;
        private Address address;
    }


    //회원가입
    @PostMapping("/members")
    public CreateMemberResponse saveMember(@RequestBody @Valid CreateMemberRequest request){

        Member member = new Member();
        member.setName(request.getName());
        member.setAddress(new Address(request.getCity(),
                                      request.getStreet(),
                                      request.getZipcode()));
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //회원수정
    @PutMapping("/members/{id}")
    public UpdateMemberResponse updateMember(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id,
                request.getName(),
                request.getCity(),
                request.getStreet(),
                request.getZipcode());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(),
                                        findMember.getName(),
                                        findMember.getAddress());
    }

    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String city;
        @NotEmpty
        private String street;
        @NotEmpty
        private String zipcode;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;
        @NotEmpty
        private String city;
        @NotEmpty
        private String street;
        @NotEmpty
        private String zipcode;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
        private Address address;

    }


}
