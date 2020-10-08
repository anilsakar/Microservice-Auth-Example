package user.users.model;

import lombok.Getter;
import lombok.Setter;

public class UserDetailResponseModel {

    @Getter @Setter
    private String fullName;

    @Getter @Setter
    private String phoneNumber;

    @Getter @Setter
    private String profilePicture;

    @Getter @Setter
    private String redirect;
    
}
