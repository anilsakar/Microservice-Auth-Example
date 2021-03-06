package user.users.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import user.users.DTO.UserDTO;
import user.users.DTO.UserDetailDTO;
import user.users.entity.UserEntity;

public interface IUserService extends UserDetailsService{
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserByEmail(String email);
    UserEntity saveUser(UserDTO userDTO);
    UserDetailDTO updateUserDetail(UserDetailDTO userDetailDTO);
	UserDTO getUser(String userId);
	UserDetailDTO getUserDetail(String userId);
	String uploadImage(MultipartFile profilePicture);
}