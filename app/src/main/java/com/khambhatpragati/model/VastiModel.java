package com.khambhatpragati.model;

import java.io.Serializable;

public class VastiModel implements Serializable {
    public String record_no;
    public String member_id;
    public String first_name;
    public String middle_name;
    public String last_name;
    public String email;
    public String mobile_no;
    public String photo_link;
    public String parent_member_id;
    public String dependent_count;
    public String brother_count;
    public String married_son_count;

    public String getRecord_no() {
        return record_no;
    }

    public void setRecord_no(String record_no) {
        this.record_no = record_no;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }

    public String getParent_member_id() {
        return parent_member_id;
    }

    public void setParent_member_id(String parent_member_id) {
        this.parent_member_id = parent_member_id;
    }

    public String getDependent_count() {
        return dependent_count;
    }

    public void setDependent_count(String dependent_count) {
        this.dependent_count = dependent_count;
    }

    public String getBrother_count() {
        return brother_count;
    }

    public void setBrother_count(String brother_count) {
        this.brother_count = brother_count;
    }

    public String getMarried_son_count() {
        return married_son_count;
    }

    public void setMarried_son_count(String married_son_count) {
        this.married_son_count = married_son_count;
    }
}

/*
package com.khambhatpragati.model;

import com.khambhatpragati.model.response.UserDirectoryBean;

import java.io.Serializable;
import java.util.Comparator;

public class VastiModel implements Serializable, Comparable<VastiModel> {
    public String record_no;
    public String member_id;
    public String first_name;
    public String middle_name;
    public String last_name;
    public String email;
    public String mobile_no;
    public String photo_link;
    public String parent_member_id;
    public String dependent_count;
    public String brother_count;
    public String married_son_count;

    public String getRecord_no() {
        return record_no;
    }

    public void setRecord_no(String record_no) {
        this.record_no = record_no;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getPhoto_link() {
        return photo_link;
    }

    public void setPhoto_link(String photo_link) {
        this.photo_link = photo_link;
    }

    public String getParent_member_id() {
        return parent_member_id;
    }

    public void setParent_member_id(String parent_member_id) {
        this.parent_member_id = parent_member_id;
    }

    public String getDependent_count() {
        return dependent_count;
    }

    public void setDependent_count(String dependent_count) {
        this.dependent_count = dependent_count;
    }

    public String getBrother_count() {
        return brother_count;
    }

    public void setBrother_count(String brother_count) {
        this.brother_count = brother_count;
    }

    public String getMarried_son_count() {
        return married_son_count;
    }

    public void setMarried_son_count(String married_son_count) {
        this.married_son_count = married_son_count;
    }

    public static Comparator<VastiModel> UserNameComparator = new Comparator<VastiModel>() {

        public int compare(VastiModel userBean1, VastiModel userBean2) {

            String userName1 = userBean1.getFirst_name().toUpperCase();
            String userName2 = userBean2.getFirst_name().toUpperCase();

            //ascending order
            return userName1.compareTo(userName2);

            //descending order
            //return userName2.compareTo(userName1);
        }
    };
    public int compareTo(VastiModel userDirectoryBean) {
//        int notificationId = ((MessagesBean) messageBean).getPushnotificationId();
        //ascending order
        return 0;
        //descending order
        //return compareQuantity - this.quantity;
    }
}
*/
