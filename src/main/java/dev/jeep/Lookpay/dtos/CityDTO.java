package dev.jeep.Lookpay.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CityDTO {
  private Long id;
  private String name;
  private String provinceName;
}
