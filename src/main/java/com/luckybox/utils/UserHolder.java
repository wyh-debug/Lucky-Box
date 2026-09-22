package com.luckybox.utils;

import com.luckybox.pojo.dto.UserDTO;

public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();
    public static void saveUser(UserDTO user) {
        tl.set(user);
    }
    public static UserDTO getUser() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }

}
