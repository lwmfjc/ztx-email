package com.lyclass;

public class LyEntity extends LyEntitySuper{
    private String name;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "LyEntity{" +
                "name='" + name + '\'' +
                ", age='" + super.getAge() + '\'' +
                '}';
    }
}
