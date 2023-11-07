//package org.nidhishah.meetingscheduler.services;
//
//import org.nidhishah.meetingscheduler.dto.SignUPDTO;
//import org.nidhishah.meetingscheduler.entity.User;
//import org.nidhishah.meetingscheduler.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.context.annotation.Lazy;
//
//@Service
//public class UserSignUpServiceImpl implements UserSignUpService{
//
//    private final UserRepository userRepository;
//    private final BCryptPasswordEncoder encoder;
//
//    @Autowired
//    public UserSignUpServiceImpl(UserRepository userRepository, @Lazy BCryptPasswordEncoder encoder) {
//        this.userRepository = userRepository;
//        this.encoder = encoder;
//    }
//
//    /*
//     * ********** If sign up person is not admin-> complete Sign Up process
//     * *********** return rolename or empty string
//     * */
//    @Override
//    public String completeUserSignUpProcess(SignUPDTO signUPDTO) {
//        //check the user
//        User user = userRepository.findUserByEmailAndOrganization(signUPDTO.getEmail(), signUPDTO.getOrganization());
//        System.out.println("SignUP Procsess: found user: "+ user.getOrganization().getOrgName() );
//        // user role is not null and not "admin"
//        if (user.getRole() != null && !"admin".equals(user.getRole().getRoleName())) {
//            System.out.println("SignUP Procsess: user role: "+ user.getRole().getRoleName());
//            //if user is not enabled, and password is empty: good to go for completing sign up
//            System.out.println("SignUP Procsess: user enable: "+ user.getisEnabled() );
////            System.out.println("SignUP Procsess: user password Empty?: "+ user.getPassword().isEmpty() );
//            if (!user.getisEnabled()) {
//
//                System.out.println("SignUP Procsess: user temp passcode from database: "+ user.getTempPasscode());
//                System.out.println("SignUP Procsess: user input passcode: "+ signUPDTO.getTempPasccode() );
//                //check given passcode is matching with user database passcode
//                if (signUPDTO.getTempPasccode().equals(user.getTempPasscode())) {
//                    // complete sign up
//                    user.setUsername(signUPDTO.getEmail().split("@")[0].toLowerCase());
//                    user.setEnabled(true);
//                    user.setPassword(encoder.encode(signUPDTO.getPassword()));
//                    //remove temp code, as user is active now
//                    user.setTempPasscode("");
//                    userRepository.save(user);
//                    System.out.println("After user is saved, "+user.getRole().getRoleName());
//                    // return user rolename
//                    return user.getRole().getRoleName();
//                }
//            }
//        }
//        return "";
//    }
//
//}
