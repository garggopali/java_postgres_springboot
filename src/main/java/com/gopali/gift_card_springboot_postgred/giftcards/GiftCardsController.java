package com.gopali.gift_card_springboot_postgred.giftcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gopali.gift_card_springboot_postgred.giftcards.GiftCards;
import com.gopali.gift_card_springboot_postgred.giftcards.GiftCardsService;
import com.gopali.gift_card_springboot_postgred.transaction.Transaction;
import com.gopali.gift_card_springboot_postgred.transaction.TransactionService;

import jakarta.validation.Valid;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/giftcards")

public class GiftCardsController {

	@Autowired	
	private final GiftCardsService giftCardsService;
	
	 @Autowired
	    private TransactionService transactionService;
	
	public GiftCardsController(GiftCardsService giftCardsService) {
        this.giftCardsService = giftCardsService;
    }

    
    // Get All Products
    @GetMapping
    public List<GiftCards> getAllGiftCards() {
        return giftCardsService.getAllGiftCards();
    }
    
    
    @PostMapping
    public ResponseEntity<GiftCards> createGiftCards(@RequestBody Map<String, Object> request) {
        BigDecimal initialBalance = new BigDecimal(request.get("initialBalance").toString());
        String currency = request.get("currency").toString();

        GiftCards newGiftCard = giftCardsService.createGiftCards(initialBalance, currency);
        return ResponseEntity.ok(newGiftCard);
    }

    // get with redemptionCode eg. http://localhost:9092/api/giftcards/ABCD1234EFG
    @GetMapping("/{redemptionCode}")
    public ResponseEntity<?> getGiftCardDetails(@PathVariable String redemptionCode) {
        GiftCards giftCards = giftCardsService.findByRedemptionCode(redemptionCode);
        if (giftCards == null) {
            return ResponseEntity.status(404).body("Gift Card Not Found");
        }
        return ResponseEntity.ok(Map.of(
            "cardNumber", giftCards.getCardNumber(),
            "redemptionCode", giftCards.getRedemptionCode(),
            "balance", giftCards.getBalance(),
            "currency", giftCards.getCurrency(),
            "status", giftCards.getStatus()
        ));
    }
    // Get balance using redemptionCode eg. http://localhost:9092/api/giftcards/ABCD1234EFG/balance
    @GetMapping("/{redemptionCode}/balance")
    public BigDecimal getGiftCardBalance(@PathVariable String redemptionCode) {
        return giftCardsService.getGiftCardBalance(redemptionCode);
    }
    
    // 🔥 NEW: Pay with Gift Card API   (POST /api/giftcards/{redemptionCode}/pay)
    @PostMapping("/{redemptionCode}/pay")
    public Map<String, Object> payWithGiftCard(
            @PathVariable String redemptionCode,
            @RequestBody Map<String, Object> request
    ) {
        BigDecimal amount = new BigDecimal(request.get("amount").toString());
        boolean block = (boolean) request.get("block");

        Transaction transaction = transactionService.processPayment(redemptionCode, amount, block);

        return Map.of(
                "transactionId", transaction.getId(),
                "message", block ? "Funds blocked successfully" : "Payment processed successfully",
                "remainingBalance", transaction.getGiftCard().getBalance()
        );
    }
    
}
