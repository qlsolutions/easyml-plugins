/*
 *  Copyright 2024, QuickLink Solutions - All Rights Reserved.
 */

package com.quicklink.email;

import static com.quicklink.email.Keys.content_end;
import static com.quicklink.email.Keys.content_start;
import static com.quicklink.email.Keys.from_address;
import static com.quicklink.email.Keys.from_name;
import static com.quicklink.email.Keys.object_end;
import static com.quicklink.email.Keys.object_start;
import static com.quicklink.email.Keys.smtp_host;
import static com.quicklink.email.Keys.smtp_password;
import static com.quicklink.email.Keys.smtp_port;
import static com.quicklink.email.Keys.smtp_transport_strategy;
import static com.quicklink.email.Keys.smtp_username;
import static com.quicklink.email.Keys.to_address;
import static com.quicklink.email.Keys.to_name;
import static com.quicklink.email.Keys.tolerance_high;
import static com.quicklink.email.Keys.tolerance_low;

import com.quicklink.easyml.plugins.api.hooks.HookContext;
import com.quicklink.easyml.plugins.api.hooks.HookPlugin;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

/**
 * HookEmailPlugin - Plugin entrypoint.
 *
 * @author Denis Mehilli
 */
public class EmailPlugin extends HookPlugin {

  public EmailPlugin() {
    super("Email", "1.0.0",
        tolerance_high,
        tolerance_low,
        from_name,
        from_address,
        to_name,
        to_address,
        object_start,
        content_start,
        object_end,
        content_end,
        smtp_host,
        smtp_port,
        smtp_username,
        smtp_password,
        smtp_transport_strategy
    );
  }


  @Override
  public void onCreate(@NotNull HookContext hookContext) {

  }

  @Override
  public void run(@NotNull HookContext ctx) {

    TransportStrategy strategy;
    try {
      strategy = TransportStrategy.valueOf(ctx.param(smtp_transport_strategy));
    } catch (IllegalArgumentException e) {
      throw new RuntimeException("Invalid strategy of type " + ctx.param(smtp_transport_strategy), e);
    }

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

    var email = EmailBuilder.startingBlank()
        .from(ctx.param(from_name), Objects.requireNonNull(ctx.param(from_address)))
        .to(ctx.param(to_name), ctx.param(to_address))
        .withSubject(object)
        .withHTMLText(content)
        .buildEmail();

    try (var mailer = MailerBuilder
        .withSMTPServer(ctx.param(smtp_host), ctx.param(smtp_port), ctx.param(smtp_username),
            ctx.param(smtp_password))
        .withTransportStrategy(strategy).buildMailer()) {

      mailer.sendMail(email);
      getLogger().info("Sent email");

    } catch (Exception e) {
      throw new RuntimeException("Error sending the email", e);
    }
  }

  @Override
  public void onEnable() {
    getLogger().info("Loaded");
  }
}
