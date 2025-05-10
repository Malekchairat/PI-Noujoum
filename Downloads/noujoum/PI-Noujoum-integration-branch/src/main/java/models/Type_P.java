//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package models;

public enum Type_P {
    CREDIT_CARD,
    PAYPAL,
    CASH;

    private Type_P() {
    }

    public String getTypeString() {
        return this.name();
    }
}
