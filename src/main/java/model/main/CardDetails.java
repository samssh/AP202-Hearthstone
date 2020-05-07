package model.main;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CardDetails {
    @Column
    @Getter
    @Setter
    private int repeatedTimes,usage;

    public CardDetails() {
    }

    public CardDetails(int repeatedTimes) {
        this.repeatedTimes=repeatedTimes;
        this.usage = 0;
    }

    public void vRepeatedTimes(int i) {
        repeatedTimes += i;
    }
}
