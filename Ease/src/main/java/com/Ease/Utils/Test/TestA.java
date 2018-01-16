package com.Ease.Utils.Test;

import javax.persistence.*;
import java.util.List;

@Entity(name = "testA")
public class TestA {
    @Id
    @GeneratedValue
    private Integer id;

    @OneToMany(mappedBy = "testA")
    private List<TestB> testBSet;

    @Transient
    private String foo;

    public TestA() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TestB> getTestBSet() {
        return testBSet;
    }

    public void setTestBSet(List<TestB> testBSet) {
        this.testBSet = testBSet;
    }

    public void bar() {
        this.foo = "bar";
    }
}