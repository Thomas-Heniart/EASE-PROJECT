package com.Ease.Utils.Test;

import javax.persistence.*;

@Entity(name = "testC")
public class TestC {
    @Id
    @GeneratedValue
    protected Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TestA testA;

    public TestC() {

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