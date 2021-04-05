package study.datajpa.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDTO;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;
import study.datajpa.repository.MemberRepository;
import study.datajpa.repository.TeamRepository;

import javax.annotation.PostConstruct;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;

    @Data
    @AllArgsConstructor
    public class Result<T> {
        private T data;
    }


//    @GetMapping("/members/{id}")
//    public Result findMember(@PathVariable("id") Long id, Pageable pageable) {
//        MemberDTO dto = memberRepository.findById(id).map(member -> new MemberDTO(member)).orElseThrow();
//        EntityModel<MemberDTO> model = new EntityModel<>(dto);
//        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).list(pageable));
//        model.add(linkTo.withRel("all-users"));
//        return new Result<>(model);
//    }

    @GetMapping("/members2/{id}")
    public String findMemberName(@PathVariable("id") Long id) {
        MemberDTO dto = memberRepository.findById(id).map(member -> new MemberDTO(member)).orElseThrow();
        return dto.getUsername();
    }

//    @GetMapping("/members2/{id}")
//    public String findMember2(@PathVariable("id") Member member) {
//        return member.getUsername();
//    }

    @GetMapping("/members")
    public Page<MemberDTO> list(@PageableDefault(size = 5, sort = "id") Pageable pageable) {
        return memberRepository.findAll(pageable).map(MemberDTO::new);
    }

    @PostConstruct
    public void init() {
//        Team saveTeam1 = teamRepository.save(new Team("teamA"));
        for (int i = 1; i <= 20; i++) {
            Team saveTeam2 = teamRepository.save(new Team("team"+i));
//            if(i % 2 == 0) memberRepository.save(new Member("user" + i, i, saveTeam2));
            memberRepository.save(new Member("user" + i, i, saveTeam2));
        }
    }
}
