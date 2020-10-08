package user.users.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_details")
@NoArgsConstructor
public class UserDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private String fullName;

    @Getter @Setter
    private String phoneNumber;

    @Getter @Setter
    private String profilePicture;

    @JsonIgnoreProperties("userDetail")
    @OneToOne(mappedBy = "userDetail", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Getter @Setter
    private UserEntity user;

    @Override
    public String toString() {
        return "id: " + this.id +"\nfullname: " + this.fullName + "\nphone: " + this.phoneNumber;
    }

}
