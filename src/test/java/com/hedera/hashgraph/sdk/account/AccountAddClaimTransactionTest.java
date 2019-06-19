package com.hedera.hashgraph.sdk.account;

import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.crypto.ed25519.Ed25519PrivateKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccountAddClaimTransactionTest {
    private static final Ed25519PrivateKey key = Ed25519PrivateKey.fromString("302e020100300506032b6570042204203b054fade7a2b0869c6bd4a63b7017cbae7855d12acc357bea718e2c3e805962");

    private static final AccountId account = new AccountId(1000);

    private static final byte[] hash = {1, 2, 2, 3, 3, 3};

    private static final AccountAddClaimTransaction txn = new AccountAddClaimTransaction()
        .setNodeAccountId(new AccountId(3))
        .setTransactionId(new TransactionId(new AccountId(1234), Instant.parse("2019-04-08T07:04:00Z")))
        .setAccountId(account)
        .setHash(hash)
        .addKey(key.getPublicKey());

    private static final String txnToString = "sigMap {\n" +
        "  sigPair {\n" +
        "    pubKeyPrefix: \"\\344\\361\\300\\353L}\\315\\303\\347\\353\\021p\\263\\b\\212=\\022\\242\\227\\364\\243\\353\\342\\362\\205\\003\\375g5F\\355\\216\"\n" +
        "    ed25519: \"\\2317\\344\\0278u\\006\\223\\212\\317lj{\\343,\\301\\301\\266\\002!08ty\\355\\303\\344\\302W\\266xa\\'\\316\\0312\\316\\035][\\017\\001;\\363rt\\234\\\"\\262\\022\\336\\b\\343\\240\\200\\257_&\\200\\217f\\006\\305\\006\"\n" +
        "  }\n" +
        "}\n" +
        "bodyBytes: \"\\n\\r\\n\\006\\b\\340\\344\\253\\345\\005\\022\\003\\030\\322\\t\\022\\002\\030\\003\\030\\240\\215\\006\\\"\\002\\bxR5\\n3\\n\\003\\030\\350\\a\\022\\006\\001\\002\\002\\003\\003\\003\\032\\$\\n\\\"\\022 \\344\\361\\300\\353L}\\315\\303\\347\\353\\021p\\263\\b\\212=\\022\\242\\227\\364\\243\\353\\342\\362\\205\\003\\375g5F\\355\\216\"\n";

    @Test
    @DisplayName("collect transaction validates")
    void correctTransactionValidates() {
        assertDoesNotThrow(txn::build);
    }

    @Test
    @DisplayName("incorrect transaction does not validate")
    void incorrectTransaction() {
        assertThrows(
            IllegalStateException.class,
            () -> new AccountAddClaimTransaction().build(),
            "transaction builder failed validation:" +
                ".setTransactionId() required" +
                ".setNodeAccountId() required" +
                ".setAccountId() required" +
                ".setHash() required" +
                ".addKey() required"
        );
    }
}
