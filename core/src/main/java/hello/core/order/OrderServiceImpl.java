package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;

public class OrderServiceImpl implements OrderSerivce{

    //순수하게 인터페이스만 의존.
    //OrderServiceImpl입장에서 생성자를 통해 어떤 객체가 들어올지 알 수 없다.
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override //주문생성 요청이 오면
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId); //회원정보 조회
        int discountPrice = discountPolicy.discount(member, itemPrice); //할인정책에 회원을 넘김

        return new Order(memberId, itemName, itemPrice, discountPrice); //최종 주문을 반환
    }
}
