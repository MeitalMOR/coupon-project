package com.meital.couponproject.model;

import lombok.*;
import com.meital.couponproject.Enum.Category;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
@Data


public class Coupon {

    private Long id;
    private Long companyId;
    private Category category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer amount;
    private Double price;
    private String image;

    //------------------constructor for Object extraction util------------------------------
    public Coupon(Long id, Long companyId, Category category, String title, String description,
                  String startDate, String endDate, Integer amount, Double price, String image) throws ParseException {
        this.id = id;
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
        this.endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    //------------------constructor for new object------------------------------------------
    public Coupon(Long companyId, Category category, String title, String description,
                  String startDate, String endDate, Integer amount, Double price, String image) throws ParseException {
        this.id = null;
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = new SimpleDateFormat("dd-MM-yyyy").parse(startDate);
        this.endDate = new SimpleDateFormat("dd-MM-yyyy").parse(endDate);
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public void setStartDate(String startDate) throws ParseException {
        this.startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
    }

    public void setEndDate(String endDate) throws ParseException {
        this.endDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate);
    }

    @Override
    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return "Coupon - " + "Id: " + id + ", Company Id: " + companyId +
                ", Category: " + category + ", Title: " + title +
                ", Description: " + description  +
                ", Start Date: " + dateFormat.format(startDate) +
                ", End Date: " + dateFormat.format(endDate) +
                ", Amount: " + amount + ", Price: " + price +
                ", Image: " + image ;

    }
}
