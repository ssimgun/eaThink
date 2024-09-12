package com.example.test2.dto;

import com.example.test2.entity.Weather_data;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Weather_dataForm {
    private String ID;
    private String firstName;
    private String secondName;
    private String nx;
    private String ny;
    private String t1h;
    private String rn1;
    private String sky;
    private String reh;
    private String pty;

    public Weather_data toEntity() {
       Weather_data wData = new Weather_data();
       wData.setID(this.ID);
       wData.setFirstName(this.firstName);
       wData.setSecondName(this.secondName);
       wData.setNx(this.nx);
       wData.setNy(this.ny);
       wData.setT1h(this.t1h);
       wData.setRn1(this.rn1);
       wData.setSky(this.sky);
       wData.setReh(this.reh);
       wData.setPty(this.pty);
       return wData;
    }
}
