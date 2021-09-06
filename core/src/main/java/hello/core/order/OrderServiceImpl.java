package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor // final 붙은 생성자 생성
public class OrderServiceImpl implements OrderService {

    //순수하게 인터페이스만 의존.
    //OrderServiceImpl 입장에서 생성자를 통해 어떤 객체가 들어올지 알 수 없다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
/*
      1. 생성자 주입
      이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법이다.
      지금까지 우리가 진행했던 방법이 바로 생성자 주입이다.
      특징 : 생성자 호출시점에 딱 1번만 호출되는 것이 보장 / 불변, 필수 의존관계에 사용
*/
    @Autowired //생성자가 딱 1개만 있으면 @Autowired 를 생략해도 자동 주입
    public OrderServiceImpl(MemberRepository memberRepository,@MainDiscountPolicy DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

/*
      2. 수정자 주입(setter 주입)
      setter라 불리는 필드의 값을 변경하는 수정자 메서드를 통해서 의존관계를 주입하는 방법이다.
      특징 : 선택, 변경 가능성이 있는 의존관계에 사용 / 자바빈 프로퍼티 규약의 수정자 메서드 방식을 사용하는 방법이다.
*/
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }

/*
      3. 필드 주입
      이름 그대로 필드에 바로 주입하는 방법이다
      특징 : 코드가 간결하지만 외부에서 변경이 불가능 / 사용하지 말자!
*/
//    @Autowired private MemberRepository memberRepository;
//    @Autowired private DiscountPolicy discountPolicy;

/*
      4. 일반 메서드 주입
      일반 메서드를 통해서 주입 받을 수 있다.
      특징 : 한번에 여러 필드를 주입 받을 수 있다. / 일반적으로 잘 사용하지 않는다.
*/
//    @Autowired
//    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//        this.memberRepository = memberRepository;
//        this.discountPolicy = discountPolicy;
//    }

    @Override //주문생성 요청이 오면
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId); //회원정보 조회
        int discountPrice = discountPolicy.discount(member, itemPrice); //할인정책에 회원을 넘김

        return new Order(memberId, itemName, itemPrice, discountPrice); //최종 주문을 반환
    }

    //테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}

/*정리 : 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 동작한다*/
