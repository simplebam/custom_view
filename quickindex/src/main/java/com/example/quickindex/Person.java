package com.example.quickindex;

/**
 * 作用：姓名：阿福
 *      拼音：AFU
 */
public class Person {

    private String name;

    private String pinyin;

    public Person(String name){
        this.name = name;
        this.pinyin = PinYinUtils.getPinYin(name);
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", pinyin='" + pinyin + '\'' +
                '}';
    }
}
