package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFImage;
import com.coperatecoding.secodeverseback.domain.ctf.CTFQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CTFImageRepository extends JpaRepository<CTFImage, Long> {
    List<CTFImage> findByCtfQuestion(CTFQuestion ctfQuestion);
}
