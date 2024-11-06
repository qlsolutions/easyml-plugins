/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.resend;


import static com.quicklink.resend.Keys.api_key;
import static com.quicklink.resend.Keys.content_end;
import static com.quicklink.resend.Keys.content_start;
import static com.quicklink.resend.Keys.from_address;
import static com.quicklink.resend.Keys.from_name;
import static com.quicklink.resend.Keys.object_end;
import static com.quicklink.resend.Keys.object_start;
import static com.quicklink.resend.Keys.to_address;
import static com.quicklink.resend.Keys.tolerance_high;
import static com.quicklink.resend.Keys.tolerance_low;

import com.quicklink.easyml.plugins.api.hooks.HookContext;
import com.quicklink.easyml.plugins.api.hooks.HookPlugin;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.jetbrains.annotations.NotNull;


/**
 * ResendPlugin - Plugin entrypoint.
 *
 * @author Denis Mehilli
 * @creation 11/09/2024
 */
public class ResendPlugin extends HookPlugin {

  public ResendPlugin() {
    super("Resend", "1.0.0",
        tolerance_high,
        tolerance_low,
        from_name,
        from_address,
        to_address,
        object_start,
        content_start,
        object_end,
        content_end,
        api_key
    );
  }


  @Override
  public void onCreate(@NotNull HookContext hookContext) {

  }

  @Override
  public void run(@NotNull HookContext ctx) {

    String object, content;
    if (ctx.getStatus().equalsIgnoreCase("started")) {
      object = ctx.param(object_start);
      content = ctx.param(content_start);
    } else {
      object = ctx.param(object_end);
      content = ctx.param(content_end);
    }

    // parse variables
    assert object != null;
    object = ctx.parseString(object);

    assert content != null;
    content = ctx.parseString(content);


    Resend resend = new Resend(ctx.param(api_key));

    CreateEmailOptions params = CreateEmailOptions.builder()
        .from("%s <%s>".formatted(ctx.param(from_name), ctx.param(from_address)))
        .to(ctx.param(to_address))
        .subject(object)
        .html(content)
        .build();

    try {
      CreateEmailResponse data = resend.emails().send(params);
      getLogger().info("Sent email with id " + data.getId());

    } catch (ResendException e) {
      getLogger().error("Error sending the email", e);
    }
  }

  @Override
  public void onEnable() {
    getLogger().info("Loaded");
  }
}
