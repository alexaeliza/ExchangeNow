package org.alexaoanaeliza.protocol.request;

public class GetDebitCardByIdRequest implements Request {
    private final Long id;

    public GetDebitCardByIdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
