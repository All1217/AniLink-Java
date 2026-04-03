package com.video;

interface Runnable {
    public abstract void run();
}

interface Eatable {
    public abstract void eat();
}

class People implements Eatable, Runnable {
    private String name;

    People(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println(name + "跑动");
    }

    @Override
    public void eat() {
        System.out.println(name + "吃东西");
    }
}

public class TestMultiImplements {
    public static void main(String[] args) {
        People p = new People("张三");
        p.run();
        p.eat();
    }
}
