package com.khambhatpragati.constants;

/**
 * Created by bhagvatee1.gupta on 21-12-2016.
 */

public class URLs {

    //! Testing
//    private final static String DOMAIN = "http://khambhatpragati.com/sandbox/api/";
//    public static final String CATEGORY_URL = "http://khambhatpragati.com/sandbox/api/";

    //! live
    
    private final static String DOMAIN = "http://khambhatpragati.com/api/";
    public static final String CATEGORY_URL = "http://khambhatpragati.com/api/";

    public static final String MOBILE_VERIFICATION_URL = DOMAIN + "login";
    public static final String OTP_VERIFICATION_URL = DOMAIN + "otp";
    public static final String SEND_MESSAGES_URL = DOMAIN + "sendpushnotification";
    public static final String GET_ALL_MESSAGES_URL = DOMAIN + "allMessages";
    public static final String GET_GROUPS_URL = DOMAIN + "mandals";
    public static final String GET_VERSIONUPDATE_URL = DOMAIN + "verifyLatestBuild";
    public static final String USER_DIRECTORY_URL = DOMAIN + "userdirectory";
    public static final String ADD_USER_URL = DOMAIN + "addUser";
    public static final String UPDATE_FCM_TOKEN_URL = DOMAIN + "updateFCMToken";
    public static final String GET_ADS_URL = DOMAIN + "advertisement";

    public static final String GET_MEMBERPROFILE_URL = DOMAIN + "memberDetails";
    public static final String UPDATEMEMBER_URL = DOMAIN + "updateMember";
    public static final String ADDMEMBERIMAGE_URL = DOMAIN + "addImage";

    public static final String GET_DEPENDENTLIST_URL = DOMAIN + "dependentList";
    public static final String GET_DEPENDENTDETAILS_URL = DOMAIN + "dependentDetails";
    public static final String UPDATEDEPENDENT_URL = DOMAIN + "updateDependent";
    public static final String ADDDEPENDENT_URL = DOMAIN + "addDependent";
    public static final String DELETEDEPENDENT_URL = DOMAIN + "deleteDependent";

    public static final String DELETEBROTHER_URL = DOMAIN + "deleteBrother";
    public static final String UPDATEBROTHER_URL = DOMAIN + "updateBrother";
    public static final String ADDBROTHER_URL = DOMAIN + "addBrother";

    public static final String VASTIPATRA_URL = DOMAIN + "vastiPatra";
    public static final String GET_BROTHERDATA_URL = DOMAIN + "brotherData";
    public static final String GET_MARRIEDSONDATA_URL = DOMAIN + "marriedSonData";
    public static final String GET_PARENTDATA_URL = DOMAIN + "parentData";
    public static final String GET_MEMBERDATA_URL = DOMAIN + "memberData";

    /**APIs Name*/
    public static final String API_NAME_USER_DIRECTORY = "userDirectory";
}
