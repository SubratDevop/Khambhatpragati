package com.khambhatpragati.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.khambhatpragati.interfaces.IPreference;
import com.khambhatpragati.model.User;

public class UserPreference implements IPreference {
    private static UserPreference userPreference = null;

    private static final String USER_PREFERENCE = "USER_PREFERENCE";
    private static final String recordId = "recordId";
    private static final  String memberID = "memberID" ;
    private static final  String firstName = "firstName";
    private static final  String middleName = "middleName";
    private static final  String lastName = "lastName";
    private static final  String lastKnownName = "lastKnownName";
    private static final  String parentMemberId = "parentMemberId";
    private static final  String grandFatherName = "grandFatherName";
    private static final  String gender = "gender";
    private static final  String dateOfBirth = "dateOfBirth";
    private static final  String memberStatus = "memberStatus";
    private static final  String committeeMember = "committeeMember";
    private static final  String bloodGroup = "bloodGroup";
    private static final  String maritalStatus = "maritalStatus";
    private static final  String qualification = "qualification";
    private static final  String profession = "profession";
    private static final  String flatNoBuilding = "flatNoBuilding";
    private static final  String residenceArea = "residenceArea";
    private static final  String nearestStation = "nearestStation";
    private static final  String residenceZone = "residenceZone";
    private static final  String city = "city";
    private static final  String pincode = "pincode";
    private static final  String khambhatAddress = "khambhatAddress";
    private static final  String mobileNo = "mobileNo";
    private static final  String email = "email";
    private static final  String membershipStatus = "membershipStatus";
    private static final  String imageUrl = "imageUrl";
    private static final  String lastModifiedDate = "lastModifiedDate";

    private SharedPreferences sharedPreferences;

    private Context context;

    private UserPreference(Context context) {
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
    }

    public static UserPreference getInstance(Context context) {
        if (userPreference == null) {
            userPreference = new UserPreference(context);
        }
        return userPreference;
    }

    @Override
    public User getPreference() {
        User user = new User();
        user.setRecordId(sharedPreferences.getString(recordId, null));
        user.setMemberID(sharedPreferences.getString(memberID, null));
        user.setFirstName(sharedPreferences.getString(firstName, null));
        user.setLastName(sharedPreferences.getString(lastName, null));
        user.setMiddleName(sharedPreferences.getString(middleName, null));
        user.setLastKnownName(sharedPreferences.getString(lastKnownName, null));
        user.setParentMemberId(sharedPreferences.getString(parentMemberId, null));
        user.setGrandFatherName(sharedPreferences.getString(grandFatherName, null));
        user.setGender(sharedPreferences.getString(gender, null));
        user.setDateOfBirth(sharedPreferences.getString(dateOfBirth, null));
        user.setMemberStatus(sharedPreferences.getString(memberStatus, null));
        user.setCommitteeMember(sharedPreferences.getString(committeeMember, null));
        user.setBloodGroup(sharedPreferences.getString(bloodGroup, null));
        user.setMaritalStatus(sharedPreferences.getString(maritalStatus, null));
        user.setQualification(sharedPreferences.getString(qualification, null));
        user.setProfession(sharedPreferences.getString(profession, null));
        user.setFlatNoBuilding(sharedPreferences.getString(flatNoBuilding, null));
        user.setResidenceArea(sharedPreferences.getString(residenceArea, null));
        user.setNearestStation(sharedPreferences.getString(nearestStation, null));
        user.setResidenceZone(sharedPreferences.getString(residenceZone, null));
        user.setCity(sharedPreferences.getString(city, null));
        user.setPincode(sharedPreferences.getString(pincode, null));
        user.setKhambhatAddress(sharedPreferences.getString(khambhatAddress, null));
        user.setEmail(sharedPreferences.getString(email, null));
        user.setMobileNo(sharedPreferences.getString(mobileNo, null));
        user.setMembershipStatus(sharedPreferences.getString(membershipStatus, null));
        user.setImageUrl(sharedPreferences.getString(imageUrl, null));
        user.setLastModifiedDate(sharedPreferences.getString(lastModifiedDate, null));
        return user;
    }

    @Override
    public void savePreference(Object object) {
        if (object != null) {
            User user = (User) object;
            SharedPreferences.Editor editor;
            editor = sharedPreferences.edit();
            editor.putString(recordId, user.getRecordId());
            editor.putString(memberID, user.getMemberID());
            editor.putString(firstName, user.getFirstName());
            editor.putString(middleName, user.getMiddleName());
            editor.putString(lastName, user.getLastName());
            editor.putString(lastKnownName, user.getLastKnownName());
            editor.putString(parentMemberId, user.getParentMemberId());
            editor.putString(grandFatherName, user.getGrandFatherName());
            editor.putString(gender, user.getGender());
            editor.putString(dateOfBirth, user.getDateOfBirth());
            editor.putString(memberStatus, user.getMemberStatus());
            editor.putString(committeeMember, user.getCommitteeMember());
            editor.putString(bloodGroup, user.getBloodGroup());
            editor.putString(maritalStatus, user.getMaritalStatus());
            editor.putString(qualification, user.getQualification());
            editor.putString(profession, user.getProfession());
            editor.putString(flatNoBuilding, user.getFlatNoBuilding());
            editor.putString(residenceArea, user.getResidenceArea());
            editor.putString(nearestStation, user.getNearestStation());
            editor.putString(residenceZone, user.getResidenceZone());
            editor.putString(city, user.getCity());
            editor.putString(pincode, user.getPincode());
            editor.putString(khambhatAddress, user.getKhambhatAddress());
            editor.putString(mobileNo, user.getMobileNo());
            editor.putString(email, user.getEmail());
            editor.putString(membershipStatus, user.getMembershipStatus());
            editor.putString(imageUrl, user.getImageUrl());
            editor.putString(lastModifiedDate, user.getLastModifiedDate());
            editor.commit();
        } else {
            throw new NullPointerException("User object is null");
        }
    }

    @Override
    public boolean isPreferenceExist() {
        if (sharedPreferences.contains(memberID)) {
            return true;
        }

        return false;
    }

    @Override
    public void clearPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        userPreference = null;
    }
}
