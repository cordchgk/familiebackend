package org.cord.REST;


import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;


public class TestBean {


    @PostConstruct
    public void init() {
        System.out.println("init");
    }

    public void add(){
        System.out.println(10 + 10);
    }




    @PreDestroy
    public void des() {
        System.out.println("destroyed");
    }
}
