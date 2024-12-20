package com.quicklink.sma;

import java.util.Arrays;
import java.util.List;

/**
 * SMASetType - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 08/10/2024
 */
public enum SMASetType {

  // Mappature degli oggetti set nel JSON ai rispettivi campi
  EnergyAndPowerBattery(
      "batteryCharging", "batteryDischarging", "batteryStateOfCharge", "batteryStateOfHealth",
      "batteryStateOfChargeArray", "batteryStateOfHealthArray", "batteryVoltage", "batteryCurrent",
      "batteryTemperature", "currentBatteryChargingSetVoltage"
  ),

  EnergyAndPowerConsumption(
      "consumption"
  ),

  EnergyAndPowerInOut(
      "gridFeedIn", "gridConsumption"
  ),

  EnergyAndPowerPv(
      "pvGeneration"
  ),

  EnergyMix(
      "pvConsumptionRate", "batteryConsumptionRate", "gridConsumptionRate", "totalConsumption"
  ),

  PowerAc(
      "voltagePhaseA2B", "voltagePhaseB2C", "voltagePhaseC2A", "voltagePhaseA", "voltagePhaseB", "voltagePhaseC",
      "currentPhaseA", "currentPhaseB", "currentPhaseC", "activePowerPhaseA", "activePowerPhaseB", "activePowerPhaseC",
      "activePower", "reactivePowerPhaseA", "reactivePowerPhaseB", "reactivePowerPhaseC", "reactivePower",
      "apparentPowerPhaseA", "apparentPowerPhaseB", "apparentPowerPhaseC", "apparentPower", "gridFrequency",
      "displacementPowerFactor"
  ),

  PowerDc(
      "dcPowerInput", "dcVoltageInput", "dcCurrentInput", "isolationResistance"
  ),

  Sensor(
      "externalInsolation", "ambientTemperature", "moduleTemperature", "windSpeed"
  );

  final List<String> series;

  SMASetType(String... series) {
    this.series = Arrays.asList(series);
  }



}
