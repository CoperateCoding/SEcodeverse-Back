package com.coperatecoding.secodeverseback.repository;

import com.coperatecoding.secodeverseback.domain.ctf.CTFImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CTFImageRepository extends JpaRepository<CTFImage, Long> {
}
