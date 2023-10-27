package com.example.cryptopulseplay.domian.reword.repository;

import com.example.cryptopulseplay.domian.reword.model.Reword;
import com.example.cryptopulseplay.domian.reword.model.RewordStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewordRepository extends JpaRepository<Reword, Long> {

    List<Reword> findAllByRewordStatus(RewordStatus rewordStatus);
    Page<Reword> findByRewordStatus(RewordStatus rewordStatus, Pageable pageable);
}
