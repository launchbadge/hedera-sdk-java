package com.hedera.sdk;

import com.google.protobuf.ByteString;
import com.hedera.sdk.crypto.ed25519.Ed25519PrivateKey;
import com.hedera.sdk.crypto.ed25519.Ed25519Signature;
import com.hedera.sdk.proto.ResponseCodeEnum;
import com.hedera.sdk.proto.Signature;
import com.hedera.sdk.proto.SignatureList;
import com.hedera.sdk.proto.TransactionResponse;
import io.grpc.*;

import java.util.Objects;
import javax.annotation.Nullable;

public final class Transaction extends HederaCall<com.hedera.sdk.proto.Transaction, TransactionResponse, ResponseCodeEnum> {
    private final com.hedera.sdk.proto.Transaction.Builder inner;
    private final io.grpc.MethodDescriptor<com.hedera.sdk.proto.Transaction, com.hedera.sdk.proto.TransactionResponse> methodDescriptor;

    @Nullable
    private final Channel rpcChannel;

    @Nullable
    private byte[] bodyBytes;

    Transaction(
        @Nullable Channel channel,
        com.hedera.sdk.proto.Transaction.Builder inner,
        MethodDescriptor<com.hedera.sdk.proto.Transaction, TransactionResponse> methodDescriptor
    ) {
        super(TransactionResponse::getNodeTransactionPrecheckCode);
        this.rpcChannel = channel;
        this.inner = inner;
        this.methodDescriptor = methodDescriptor;
    }

    public Transaction sign(Ed25519PrivateKey privateKey) {
        var signature = Ed25519Signature.forMessage(privateKey, getBodyBytes())
            .toBytes();

        // FIXME: This nested signature is only for account IDs < 1000
        // FIXME: spotless makes this.. lovely
        // FIXME: Is `ByteString.copyFrom` ideal here?
        inner.getSigsBuilder()
            .addSigs(
                Signature.newBuilder()
                    .setSignatureList(
                        SignatureList.newBuilder()
                            .addSigs(
                                Signature.newBuilder()
                                    .setEd25519(ByteString.copyFrom(signature))
                            )
                    )
            );

        return this;
    }

    public final com.hedera.sdk.proto.Transaction toProto() {
        if (inner.getSigsBuilder()
            .getSigsCount() == 0) {
            throw new IllegalStateException("Transaction is not signed");
        }

        if (!inner.getBodyBuilder()
            .hasTransactionID()) {
            throw new IllegalStateException("Transaction needs an ID");
        }

        return inner.build();
    }

    private byte[] getBodyBytes() {
        if (bodyBytes == null) {
            bodyBytes = inner.getBody()
                .toByteArray();
        }

        return bodyBytes;
    }

    @Override
    protected MethodDescriptor<com.hedera.sdk.proto.Transaction, TransactionResponse> getMethod() {
        return methodDescriptor;
    }

    @Override
    protected com.hedera.sdk.proto.Transaction buildRequest() {
        return toProto();
    }

    @Override
    protected Channel getChannel() {
        Objects.requireNonNull(rpcChannel, "Transaction.rpcChannel must be non-null in regular use");
        return rpcChannel;
    }
}
