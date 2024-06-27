package com.quicklink.hookemail;

import static com.quicklink.hookemail.Keys.*;


import com.quicklink.easyml.plugins.api.hooks.HookContext;
import com.quicklink.easyml.plugins.api.hooks.HookPlugin;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

public class HookEmailPlugin extends HookPlugin {

  public HookEmailPlugin() {
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
  public void run(@NotNull HookContext ctx) {

    TransportStrategy strategy ;
    try {
      strategy = TransportStrategy.valueOf(ctx.param(smtp_transport_strategy));
    } catch (IllegalArgumentException e) {

      getLogger().ifPresent(logger -> logger.error("Invalid strategy of type " + ctx.param(smtp_transport_strategy), e));

      return;
    }

    String object, content;
    if(ctx.getStatus().equalsIgnoreCase("started")) {
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


    try(var mailer =  MailerBuilder
        .withSMTPServer(ctx.param(smtp_host), ctx.param(smtp_port), ctx.param(smtp_username), ctx.param(smtp_password))
        .withTransportStrategy(strategy).buildMailer()) {

      mailer.sendMail(email);
      getLogger().ifPresent(logger -> logger.info("Sent email"));

    } catch (Exception e) {
      getLogger().ifPresent(logger -> logger.error("Error sending the email", e));
    }
  }

  @Override
  public void onEnable() {
    getLogger().ifPresent(logger -> logger.info("Loaded"));
  }
}
