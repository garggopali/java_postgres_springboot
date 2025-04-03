package com.gopali.gift_card_springboot_postgred.transaction;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.gopali.gift_card_springboot_postgred.giftcards.GiftCards;
import com.gopali.gift_card_springboot_postgred.giftcards.GiftCardsRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

	private final GiftCardsRepository giftCardsRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(GiftCardsRepository giftCardRepository, TransactionRepository transactionRepository) {
        this.giftCardsRepository = giftCardRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction processPayment(String redemptionCode, BigDecimal amount, boolean block) {
        GiftCards giftCards = giftCardsRepository.findByRedemptionCode(redemptionCode)
                .orElseThrow(() -> new RuntimeException("Gift card not found"));

        if (giftCards.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID());
        transaction.setGiftCard(giftCards);
        transaction.setType(block ? TransactionType.BLOCK : TransactionType.PAYMENT);
        transaction.setAmount(amount);
        transaction.setCurrency(giftCards.getCurrency());
        transaction.setStatus((block ? TransactionStatus.BLOCKED : TransactionStatus.COMPLETED));

        transactionRepository.saveAndFlush(transaction);

        if (!block) {
            giftCards.setBalance(giftCards.getBalance().subtract(amount));
            giftCardsRepository.save(giftCards);
        }

        return transaction;
    }
    
}
