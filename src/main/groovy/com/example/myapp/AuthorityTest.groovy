package com.example.myapp

class AuthorityTest {

    public static String[] authorities = [
            'ROLE_ADMIN'
    ]

    public static String getCommaSeparatedAuthorities() {
        return authorities.collect { "'${it}'" }.join(", ")
    }

}