/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.savings.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//Use SavingsAccountRepositoryWrapper.
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long>, JpaSpecificationExecutor<SavingsAccount> {

    @Query("select s_acc from SavingsAccount s_acc where s_acc.client.id = :clientId")
    List<SavingsAccount> findSavingAccountByClientId(@Param("clientId") Long clientId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select sa from SavingsAccount sa where sa.id = :savingsId")
    SavingsAccount findOneLocked(@Param("savingsId") Long id);

    @Query("select s_acc from SavingsAccount s_acc where s_acc.gsim.id = :gsimId")
    List<SavingsAccount> findSavingAccountByGsimId(@Param("gsimId") Long gsimId);

    @Query("select s_acc from SavingsAccount s_acc where s_acc.status = :status")
    List<SavingsAccount> findSavingAccountByStatus(@Param("status") Integer status);

    @Query("select sa from SavingsAccount sa where sa.client.id = :clientId and sa.group.id = :groupId")
    List<SavingsAccount> findByClientIdAndGroupId(@Param("clientId") Long clientId, @Param("groupId") Long groupId);

    @Query("select case when (count (saving) > 0) then 'true' else 'false' end from SavingsAccount saving where saving.client.id = :clientId and saving.status in (100,200,300,303,304)")
    boolean doNonClosedSavingAccountsExistForClient(@Param("clientId") Long clientId);

    @Query("select sa from SavingsAccount sa where sa.client.id is null and sa.group.id = :groupId")
    List<SavingsAccount> findByGroupId(@Param("groupId") Long groupId);

    @Query("select sa from SavingsAccount sa where sa.id = :accountId and sa.depositType = :depositAccountTypeId")
    SavingsAccount findByIdAndDepositAccountType(@Param("accountId") Long accountId,
            @Param("depositAccountTypeId") Integer depositAccountTypeId);

    @Query("select sa from SavingsAccount sa where sa.accountNumber = :accountNumber and sa.status in (100, 200, 300, 303, 304) ")
    SavingsAccount findNonClosedAccountByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("select sa from SavingsAccount sa where sa.accountNumber = :accountNumber ")
    SavingsAccount findSavingsAccountByAccountNumber(@Param("accountNumber") String accountNumber);

    Page<SavingsAccount> findByStatus(Integer status, Pageable pageable);

    SavingsAccount findByExternalId(String externalId);

    @Query("select sa from SavingsAccount sa where sa.accountNumber = :accountNumber")
    SavingsAccount findByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("select sa from SavingsAccount sa where sa.product.id = :productId and sa.status = :status and (sa.numOfCreditTransaction != :numOfCredit or sa.numOfDebitTransaction != :numOfDebit or"
            + " sa.minBalanceForInterestCalculation != :minBalance)")
    List<SavingsAccount> findByProductIdAndStatus(@Param("productId") Long productId, @Param("status") Integer status,
            @Param("numOfCredit") Long numOfCredit, @Param("numOfDebit") Long numOfDebit, @Param("minBalance") BigDecimal minBalance);

    @Query("select sa from SavingsAccount sa where sa.client.id = :clientId and sa.group.id = :groupId and sa.gsim.id = :gsimId")
    List<SavingsAccount> findByClientIdAndGroupIdAndGsimId(@Param("clientId") Long clientId, @Param("groupId") Long groupId,
            @Param("gsimId") Long gsimId);

    @Query("SELECT sa FROM SavingsAccount sa WHERE sa.withdrawalFrequency IS NOT NULL AND sa.withdrawalFrequencyEnum IS NOT NULL AND sa.status = 300 AND sa.nextFlexWithdrawalDate IS NOT NULL AND sa.nextFlexWithdrawalDate < :today")
    List<SavingsAccount> findSavingAccountToUpdateNextFlexWithdrawalDate(@Param("today") LocalDate today);
}
