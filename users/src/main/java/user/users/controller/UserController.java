package user.users.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.Jwts;
import user.users.DAO.ConfirmationTokenDAO;
import user.users.DTO.UserDTO;
import user.users.DTO.UserDetailDTO;
import user.users.entity.ConfirmationTokenEntity;
import user.users.model.StringResponse;
import user.users.model.UserDetailRequestModel;
import user.users.model.UserDetailResponseModel;
import user.users.model.UserRequestModel;
import user.users.model.UserResponseModel;
import user.users.service.IUserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Environment environment;

    @Autowired
    IUserService userService;

    @Autowired
    ConfirmationTokenDAO confirmationTokenDAO;

    public String getInformation(String token) {
        if(token == null)
            return null;

        System.out.println(environment.getProperty("authorization.token.header.prefix"));
        String myToken = token.replace(environment.getProperty("authorization.token.header.prefix"), "");
        String information = Jwts.parser().setSigningKey(environment.getProperty("token.secret")).parseClaimsJws(myToken)
                .getBody().getSubject();
        return information;
    }

    @GetMapping
    public ResponseEntity<UserResponseModel> getUser(@RequestHeader(name = "Authorization") String token) {

        String userId = getInformation(token);

        if(userId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        UserDTO tempUser = userService.getUser(userId);

        if (tempUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserResponseModel returnValue = modelMapper.map(tempUser, UserResponseModel.class);

        returnValue.setRedirect("Inside UserController.java");

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody UserRequestModel userRequestModel) {

        /// Created our modelMapper to map userRequestModel to userDTO.
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = modelMapper.map(userRequestModel, UserDTO.class);

        /// Send userDTO to the userService to create our user into the database.
        UserDTO createdUser = userService.createUser(userDTO);

        /// If createdUser is null return badRequest.
        if (createdUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        /// Map userDTO to UserResponseModel.
        UserResponseModel returnValue = modelMapper.map(createdUser, UserResponseModel.class);
        returnValue.setRedirect("Inside UserController.java");

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping("/userDetails")
    public ResponseEntity<UserDetailResponseModel> getUserDetail(@RequestHeader(name = "Authorization") String token) {

        String userId = getInformation(token);

        if (userId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        UserDetailDTO tempUserDetail = userService.getUserDetail(userId);

        if (tempUserDetail == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDetailResponseModel returnValue = modelMapper.map(tempUserDetail, UserDetailResponseModel.class);

        returnValue.setRedirect("Inside UserController.java");

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);

    }

    @PatchMapping("/userDetails")
    public ResponseEntity<UserDetailResponseModel> updateUserDetails(
            @Valid @RequestBody UserDetailRequestModel userDetailRequestModel,
            @RequestHeader(name = "Authorization") String token) {

        String userId = getInformation(token);

        if (userId == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        userDetailRequestModel.setUserId(userId);

        /// Create our modelMapper to map userDetailRequestModel to userDetailDTO.
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDetailDTO userDetailDTO = modelMapper.map(userDetailRequestModel, UserDetailDTO.class);

        /// Send userDetailDTO to the userService to update our userDetail.
        UserDetailDTO updatedUserDetail = userService.updateUserDetail(userDetailDTO);

        /// If updatedUserDetail is null return badRequest.
        if (updatedUserDetail == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);

        /// Map updatedUserDetail to UserDetailResponseModel.
        UserDetailResponseModel returnValue = modelMapper.map(updatedUserDetail, UserDetailResponseModel.class);

        returnValue.setRedirect("Inside UserController.java");

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

    @RequestMapping(value = "/confirm-account", method = { RequestMethod.GET, RequestMethod.POST })
    public ResponseEntity<Boolean> confirmUserAccount(@RequestParam("mailToken") String confirmationToken) {
        ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenDAO
                .findByConfirmationToken(confirmationToken);

        if (confirmationTokenEntity != null) {
            UserDTO userDTO = userService.getUserByEmail(confirmationTokenEntity.getUserEntity().getEmail());
            userDTO.setEnabled(true);
            userService.saveUser(userDTO);
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

    }

    @PostMapping("/upload/picture")
    public ResponseEntity<StringResponse> uploadImage(@RequestPart MultipartFile profilePicture){

        if(profilePicture == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse("No image selected."));
        }

        String pictureUrl = userService.uploadImage(profilePicture);

        if(pictureUrl == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new StringResponse("Something happened while uploading image."));
        }

        StringResponse returnValue = new StringResponse(pictureUrl);

        return ResponseEntity.status(HttpStatus.OK).body(returnValue);
    }

}