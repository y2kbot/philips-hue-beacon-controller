package com.alterego.ibeaconapp.app.api.hue.data;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class HueBridgeInfo {

    @Getter @Setter public String id;

    //Editable name given to the bridge
    @Getter @Setter public String name;

    @Getter @Setter @SerializedName("internalipaddress") public String internalIPAddress;

    @Getter @Setter @SerializedName("macaddress") public String macAddress;


}
