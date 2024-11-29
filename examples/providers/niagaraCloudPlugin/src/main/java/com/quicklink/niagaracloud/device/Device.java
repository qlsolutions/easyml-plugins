package com.quicklink.niagaracloud.device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Device - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 27/11/2024
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {

  private String systemGuid;

  private String hostId;
}
