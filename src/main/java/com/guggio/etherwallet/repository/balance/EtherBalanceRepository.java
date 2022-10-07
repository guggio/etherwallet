package com.guggio.etherwallet.repository.balance;

import com.guggio.etherwallet.entity.balance.EtherBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtherBalanceRepository extends JpaRepository<EtherBalance, Long> {
}