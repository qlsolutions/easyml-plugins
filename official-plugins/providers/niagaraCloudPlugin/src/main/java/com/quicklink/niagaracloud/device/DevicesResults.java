package com.quicklink.niagaracloud.device;



import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevicesResults {

  private List<Device> devices = new ArrayList<>();
}
