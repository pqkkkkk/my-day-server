package org.pqkkkkk.my_day_server.user.dto;

import org.pqkkkkk.my_day_server.user.entity.User;

public class DTO {
    public record UserDTO(
        String username,
        String userEmail,
        String userFullName
    ){
        public static UserDTO fromEntity(User user){
            return new UserDTO(
                user.getUsername(),
                user.getUserEmail(),
                user.getUserFullName()
            );
        }
    }
}
