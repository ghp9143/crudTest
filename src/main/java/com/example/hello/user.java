package com.example.hello;

public class user {
    static class User {
        private String name;
        private int age;

        public User() {

        }

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }
}
