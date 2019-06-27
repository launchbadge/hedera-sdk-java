package com.hedera.hashgraph.sdk.account;

import com.hedera.hashgraph.sdk.Entity;
import com.hedera.hashgraph.sdk.crypto.Key;
import com.hedera.hashgraph.sdk.proto.AccountID;

import java.util.List;
import java.util.stream.Collectors;

public final class Claim implements Entity {
    private final com.hedera.hashgraph.sdk.proto.Claim inner;

    public Claim(com.hedera.hashgraph.sdk.proto.Claim inner) {
        this.inner = inner;
    }

    public AccountId getAcccount() {
        AccountID account = this.inner.getAccountID();

        return new AccountId(account.getShardNum(), account.getRealmNum(), account.getAccountNum());
    }

    public byte[] getHash() {
        return this.inner.getHash()
            .toByteArray();
    }

    public List<Key> getKeys() {
        //noinspection Convert2MethodRef
        return this.inner.getKeys()
            .getKeysList()
            .stream()
            // current dev version of D8 does not support static interface method references
            .map(k -> Key.fromProtoKey(k))
            .collect(Collectors.toList());
    }
}
