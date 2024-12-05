package com.quicklink.sma.client.model;

/**
 * LoginResponse - Insert description here.
 *
 * @author Denis Mehilli
 * @creation 03/10/2024
 */

public record LoginResponse(String access_token,
                            int expires_in,
                            int refresh_expires_in,
                            String refresh_token) {

}
