package com.dt.entity;

import lombok.Data;

@Data
public class CampaignFilterRequest {
    private String searchText;
    private String client;
    private String status;
    private String user;
    private String type;
    private String date;
    private int page;
    private int size;
}	