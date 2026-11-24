package com.saucedemo.selenium.cucumber;

import com.saucelabs.saucebindings.options.SauceOptions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;

/**
 * Sauce Labs job tags shared by every test in this repo: {@code repo:demo-java}, plus a feature tag
 * for each capture feature the session's capabilities actually enable. Tags are derived from the
 * final capabilities right before the session starts, so they can't drift from what the job really
 * records.
 */
public final class SauceTags {
  public static final String REPO = "repo:demo-java";
  public static final String NETWORK_CAPTURE = "feature:network-capture";
  public static final String DEVICE_VITALS = "feature:device-vitals";

  private SauceTags() {}

  /** Adds the repo and feature tags to the {@code sauce:options} of {@code caps}, in place. */
  public static <T extends MutableCapabilities> T apply(T caps) {
    Map<String, Object> sauceOptions = new HashMap<>();
    Object raw = caps.getCapability("sauce:options");
    if (raw instanceof Capabilities) {
      sauceOptions.putAll(((Capabilities) raw).asMap());
    } else if (raw instanceof Map) {
      ((Map<?, ?>) raw).forEach((k, v) -> sauceOptions.put(String.valueOf(k), v));
    }

    Set<String> tags = new LinkedHashSet<>();
    if (sauceOptions.get("tags") instanceof Collection) {
      ((Collection<?>) sauceOptions.get("tags")).forEach(t -> tags.add(String.valueOf(t)));
    }
    tags.add(REPO);

    // Real devices record a HAR with networkCapture, desktop browsers with extendedDebugging.
    if (isTrue(sauceOptions.get("networkCapture"))
        || isTrue(sauceOptions.get("extendedDebugging"))) {
      tags.add(NETWORK_CAPTURE);
    }

    // Vitals need resigning (on by default) and are only collected for apps: real-device browser
    // sessions (Chrome on Android, Safari on iOS) produce no vitals even with vitals=true.
    boolean browserSession = caps.getBrowserName() != null && !caps.getBrowserName().isEmpty();
    if (isTrue(sauceOptions.get("vitals"))
        && !"false".equals(String.valueOf(sauceOptions.get("resigningEnabled")))
        && !browserSession) {
      tags.add(DEVICE_VITALS);
    }

    sauceOptions.put("tags", new ArrayList<>(tags));
    caps.setCapability("sauce:options", sauceOptions);
    return caps;
  }

  /** Same as {@link #apply(MutableCapabilities)} for Sauce Bindings options (desktop only). */
  public static SauceOptions apply(SauceOptions options) {
    Set<String> tags = new LinkedHashSet<>();
    if (options.sauce().getTags() != null) {
      tags.addAll(options.sauce().getTags());
    }
    tags.add(REPO);
    if (Boolean.TRUE.equals(options.sauce().getExtendedDebugging())) {
      tags.add(NETWORK_CAPTURE);
    }
    options.sauce().setTags(new ArrayList<>(tags));
    return options;
  }

  private static boolean isTrue(Object value) {
    return "true".equals(String.valueOf(value));
  }
}
