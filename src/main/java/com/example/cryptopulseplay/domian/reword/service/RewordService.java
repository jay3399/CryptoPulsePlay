package com.example.cryptopulseplay.domian.reword.service;

import com.example.cryptopulseplay.domian.game.model.Game;
import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.model.RewordStatus;
import com.example.cryptopulseplay.domian.reword.repository.RewordRepository;
import com.example.cryptopulseplay.domian.user.model.User;
import com.example.cryptopulseplay.domian.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RewordService {

    private final RewordRepository rewordRepository;

    private final UserRepository userRepository;


    public List<Reword> findRewordOnPending() {

        return rewordRepository.findAllByRewordStatus(RewordStatus.PENDING);

    }

    public Page<Reword> findRewordOnPendingV2(RewordStatus status, Pageable pageable) {
        return rewordRepository.findByRewordStatus(status, pageable);
    }

    /**
     * 애초에 Redis 에 저장된 game 은 엔티티가 아니다 .
     * 그곳에서 user 를 꺼내도 내가 원하는 user 가 아님 ( 영속화 돼있는 )
     * 그렇기에 진짜 내가원하는 user 를 꺼내오려면 game 의 user 에 접근한뒤 id 만을 가져와서 , 진짜 User 를 디비에서 찾아와 영속화 해야한다
     * 그 뒤에는 user 에서 컬렉션을 명시적으로 초기화한뒤 , game 을 추가해준다 .
     *
     * + 다시 생각해보니 , 양방향 매핑은 내가 나중에 편하려고 해놓은거긴한데 그만큼 귀찮은 부분이 생겨서 일단 처음엔 단방향으로 설게를 해놓는게 맞는듯하다 .
     */

    public void createReword(Game game) {

        User user = userRepository.findById(game.getUser().getId()).get();

        Hibernate.initialize(user.getGames());  // games 컬렉션 초기화

        user.getGames().add(game);

        Reword reword = Reword.create(game);

        rewordRepository.save(reword);

    }


}
