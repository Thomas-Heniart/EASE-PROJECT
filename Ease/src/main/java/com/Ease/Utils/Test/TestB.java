package com.Ease.Utils.Test;

import javax.persistence.*;

@Entity(name = "testB")
public class TestB {
    @Id
    @GeneratedValue
    protected Integer id;

    @ManyToOne
    @JoinColumn(name = "testA_id")
    private TestA testA;

    public TestB() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TestA getTestA() {
        return testA;
    }

    public void setTestA(TestA testA) {
        this.testA = testA;
    }
}