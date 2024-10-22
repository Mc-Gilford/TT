package com.matias.domuapp.models;

public class Cuenta {
    private String cardNumber;
    private String typeCard;
    private String accounNumber;
    private Double amount;

    @Override
    public String toString() {
        return "Cuenta{" +
                "cardNumber='" + cardNumber + '\'' +
                ", typeCard='" + typeCard + '\'' +
                ", accounNumber='" + accounNumber + '\'' +
                ", amount=" + amount +
                '}';
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getTypeCard() {
        return typeCard;
    }

    public void setTypeCard(String typeCard) {
        this.typeCard = typeCard;
    }

    public String getAccounNumber() {
        return accounNumber;
    }

    public void setAccounNumber(String accounNumber) {
        this.accounNumber = accounNumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
