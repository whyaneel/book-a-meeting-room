package com.prototype.booking.api.referencedata;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class UserInfo {

    @Id
    @Column
    private String empId;

    @Column
    private String email;

}
